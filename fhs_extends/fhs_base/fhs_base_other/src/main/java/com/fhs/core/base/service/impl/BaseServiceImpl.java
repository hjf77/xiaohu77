package com.fhs.core.base.service.impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheUpdateManager;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaJoinQueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.*;
import com.fhs.core.base.anno.NotRepeatDesc;
import com.fhs.core.base.anno.NotRepeatField;
import com.fhs.core.base.autodel.service.AutoDelService;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import com.fhs.core.base.service.BaseService;
import com.fhs.core.base.vo.FhsPager;
import com.fhs.core.cache.annotation.Cacheable;
import com.fhs.core.cache.annotation.Namespace;
import com.fhs.core.cache.service.RedisCacheService;
import com.fhs.core.trans.anno.AutoTrans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.trans.vo.VO;
import com.fhs.core.valid.checker.ParamChecker;
import com.fhs.core.logger.Logger;
import com.fhs.log.LoggerContext;
import com.fhs.trans.service.AutoTransAble;
import com.fhs.trans.service.impl.TransService;
import com.github.liangbaika.validate.exception.ParamsInValidException;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * 业务层base类，主要提供对数据库的CRUD操作
 *
 * @author wanglei
 * @version [版本号, 2015年5月27日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class BaseServiceImpl<V extends VO, P extends BasePO> implements BaseService<V, P>, AutoTransAble<V>, InitializingBean {

    public static final int DEFAULT_BATCH_SIZE = 1000;

    protected final Logger log = Logger.getLogger(this.getClass());

    protected Log mybatisLog = LogFactory.getLog(getClass());

    /**
     * 缓存 默认时间：半个小时
     */
    @CreateCache(expire = 1800, name = "poCache:", cacheType = CacheType.BOTH)
    private Cache<String, P> poCache;

    /**
     * do的namespace
     */
    private String namespace;

    /**
     * 判断自己是否需要支持自动缓存
     */
    private boolean isCacheable;

    @Autowired
    protected TransService transService;

    @Autowired
    protected IdHelper idHelper;

    /**
     * 利用spring4新特性泛型注入
     */
    @Autowired
    protected FhsBaseMapper<P> baseMapper;

    @Autowired
    private CacheUpdateManager cacheUpdateManager;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private AutoDelService autoDelService;

    public BaseServiceImpl() {
        //判断自己是否需要支持缓存
        this.isCacheable = this.getClass().isAnnotationPresent(Cacheable.class);
        if (isCacheable) {
            this.namespace = this.getClass().getAnnotation(Cacheable.class).value();
        } else if (this.getClass().isAnnotationPresent(Namespace.class)) {
            this.namespace = this.getClass().getAnnotation(Namespace.class).value();
        } else if (this.getClass().isAnnotationPresent(AutoTrans.class)) {
            this.namespace = this.getClass().getAnnotation(AutoTrans.class).namespace();
        } else if (this.getTypeArgumentsClass(1).isAnnotationPresent(Table.class)) {
            this.namespace = this.getTypeArgumentsClass(1).getAnnotation(Table.class).name().replace("t_", "");
        } else if (this.getTypeArgumentsClass(1).isAnnotationPresent(TableName.class)) {
            this.namespace = this.getTypeArgumentsClass(1).getAnnotation(TableName.class).value().replace("t_", "");
        }
    }


    @Override
    public Long findCount(P bean) {
        bean.setIsDelete(Constant.INT_FALSE);
        LambdaJoinQueryWrapper<P>  wrapper = bean.asWrapper();
        wrapper.setIsCount(true);
        return baseMapper.selectCount(wrapper);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<V> findForList(P bean) {
        bean.setIsDelete(Constant.INT_FALSE);
        List<P> dos = baseMapper.selectList(bean.asWrapper());
        return pos2vos(dos);
    }


    @Override
    public int insert(P entity) {
        initPkeyAndIsDel(entity);
        addCache(entity);
        checkIsExist(entity, false);
        int result = baseMapper.insert(entity);
        this.refreshCache();
        addHistory(entity);
        return result;
    }

    private void initPkeyAndIsDel(P entity) {
        entity.setIsDelete(Constant.INT_FALSE);
        Field field = entity.getIdField(false);
        if (field != null) {
            field.setAccessible(true);
            try {
                if (field.get(entity) == null && field.getType() == String.class) {
                    field.set(entity, StringUtils.getUUID());
                }
                if (field.get(entity) == null && field.getType() == Long.class) {
                    field.set(entity, idHelper.nextId());
                }
                if (entity.getCreateTime() == null) {
                    entity.setCreateTime(new Date());
                    entity.setUpdateTime(new Date());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加缓存
     *
     * @param entity 实体类
     */
    protected void addCache(P entity) {
        if (this.isCacheable) {
            String pkey = getPkeyVal(entity);
            this.poCache.put(namespace + ":" + pkey, entity);
        }
    }

    /**
     * 刷新缓存,包括do缓存,autotrans缓存,以及其他模块依赖的缓存
     */
    protected void refreshCache() {
        cacheUpdateManager.clearCache(namespace);
        AutoTrans autoTrans = this.getClass().getAnnotation(AutoTrans.class);
        if (autoTrans != null && (autoTrans.useCache() || autoTrans.useRedis())) {
            //发送刷新的消息
            Map<String, String> message = new HashMap<>();
            message.put("transType", TransType.AUTO_TRANS);
            message.put("namespace", autoTrans.namespace());
            redisCacheService.convertAndSend("trans", JsonUtil.map2json(message));
        }
        if (this.namespace != null) {
            this.cacheUpdateManager.clearCache(namespace);
        }
    }

    /**
     * 清除缓存
     *
     * @param pkey 主键
     */
    protected void removeCache(Object pkey) {
        if (this.isCacheable) {
            this.poCache.remove(namespace + ":" + pkey);
        }
    }

    /**
     * 获取主键
     *
     * @param entity do
     * @return 主键值
     */
    private String getPkeyVal(P entity) {
        return ConverterUtils.toString(entity.getPkey());
    }


    @Override
    public boolean batchInsert(List<P> list) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        //如果po标记了重复数据校验,则进行重复数据校验
        if (this.getPoClass().isAnnotationPresent(NotRepeatDesc.class)) {
            Set<String> hasData = new HashSet<>();
            List<Field> fields = ReflectUtils.getAnnotationField(this.getPoClass(), NotRepeatField.class);
            StringBuilder errorMsg = new StringBuilder();
            boolean isHasError = false;
        }
        for (P d : list) {
            initPkeyAndIsDel(d);
        }
        String sqlStatement = getSqlStatement(SqlMethod.INSERT_ONE);
        boolean result = executeBatch(list, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
        this.refreshCache();
        for (P d : list) {
            addHistory(d);
        }
        return result;
    }

    @Override
    public int deleteById(Serializable primaryValue) {
        autoDelService.deleteCheck(this.namespace, primaryValue);
        int result = baseMapper.deleteById(primaryValue);
        autoDelService.deleteItemTBL(this.namespace, primaryValue);
        this.refreshCache();
        removeCache(primaryValue);
        return result;
    }


    @Override
    public int updateSelectiveById(P entity) {
        P oldEntity = baseMapper.selectById(entity.getPkey());
        BeanUtils.copyProperties(entity, oldEntity, ReflectUtils.getNullPropertyNames(entity));
        return updateById(oldEntity);
    }


    @Override
    public int updateById(P entity) {
        checkIsExist(entity, true);
        updateCache(entity);
        this.refreshCache();
        int reuslt = baseMapper.updateById(entity);
        this.addHistory(entity);
        return reuslt;
    }

    /**
     * 添加历史
     *
     * @param entity 数据
     */
    protected void addHistory(P entity) {
        if (this.namespace != null) {
            LoggerContext.addHistoryData(entity, namespace);
        }
    }

    /**
     * 缓存更新
     *
     * @param entity
     */
    protected void updateCache(P entity) {
        if (this.isCacheable) {
            String pkey = this.getPkeyVal(entity);
            this.poCache.remove(namespace + ":" + pkey);
            this.poCache.put(namespace + ":" + pkey, entity);
        }
    }

    @Override
    public boolean batchUpdate(List<P> entitys) {
        if (entitys == null || entitys.isEmpty()) {
            return false;
        }
        String sqlStatement = getSqlStatement(SqlMethod.UPDATE_BY_ID);
        boolean result = executeBatch(entitys, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
            MapperMethod.ParamMap<P> param = new MapperMethod.ParamMap<>();
            param.put(Constants.ENTITY, entity);
            sqlSession.update(sqlStatement, param);
        });
        for (P entity : entitys) {
            updateCache(entity);
            addHistory(entity);
        }
        this.refreshCache();
        return result;
    }

    @Override
    public V selectById(Serializable primaryValue) {
        if (this.isCacheable) {
            String pkey = ConverterUtils.toString(primaryValue);
            P result = this.poCache.get(namespace + ":" + pkey);
            if (result == null) {
                result = baseMapper.selectById(primaryValue);
                if (result != null) {
                    this.poCache.put(namespace + ":" + pkey, result);
                }
            }
            return p2v(result, false);
        }
        return p2v(baseMapper.selectById(primaryValue), false);
    }

    @Override
    public V selectById(Object primaryValue) {
        return p2v(baseMapper.selectById((Serializable) primaryValue), false);
    }

    @Override
    public List<V> select() {
        return ListUtils.copyListToList(baseMapper.selectList(new QueryWrapper<>()), this.getVOClass());
    }


    @Override
    public V selectBean(P param) {
        return p2v(baseMapper.selectOne((LambdaJoinQueryWrapper<P>) param.asWrapper()));
    }


    @Override
    public int deleteBean(P entity) {
        List<P> pos = baseMapper.selectList(entity.asWrapper());
        if (pos.isEmpty()) {
            return 0;
        }
        for (P p : pos) {
            autoDelService.deleteCheck(this.namespace, p.getPkey());
            autoDelService.deleteItemTBL(this.namespace, p.getPkey());
        }
        //批量修改为已删除
        return baseMapper.delete(entity.asWrapper());
    }


    @Override
    public int deleteBatchIds(Collection<? extends Serializable> idList) {
        if (idList == null || idList.isEmpty()) {
            return 0;
        }
        for (Object id : idList) {
            autoDelService.deleteCheck(this.namespace, id);
            autoDelService.deleteItemTBL(this.namespace, id);
        }
        return baseMapper.deleteBatchIds(idList);
    }

    @Override
    public List<V> selectBatchIdsMP(Collection<? extends Serializable> idList) {
        return pos2vos(baseMapper.selectBatchIds(idList));
    }

    @Override
    public V selectOneMP(Wrapper<P> queryWrapper) {
        return p2v(baseMapper.selectOne(queryWrapper));
    }

    @Override
    public Long selectCountMP(Wrapper<P> queryWrapper) {
        return baseMapper.selectCount(queryWrapper);
    }

    @Override
    public List<V> selectListMP(Wrapper<P> queryWrapper) {
        return pos2vos(baseMapper.selectList(queryWrapper));
    }

    @Override
    public List<Map<String, Object>> selectMapsMP(Wrapper<P> queryWrapper) {
        return baseMapper.selectMaps(queryWrapper);
    }

    @Override
    public List<Object> selectObjsMP(Wrapper<P> queryWrapper) {
        return baseMapper.selectObjs(queryWrapper);
    }

    @Override
    public IPage<V> selectPageMP(IPage<P> page, Wrapper<P> queryWrapper) {
        ParamChecker.isNotNull(page, "前端调用接口的时候没传分页信息");
        page = baseMapper.selectPage(page, queryWrapper);
        return pagerConverter(page);
    }

    /**
     * 分页数据转换-由po转换为vo
     *
     * @param page 分页数据
     * @return 转换后的结果
     */
    protected FhsPager<V> pagerConverter(IPage<P> page) {
        FhsPager<V> result = new FhsPager<V>();
        result.setTotal(page.getTotal());
        result.setPageSize(page.getSize());
        result.setCurrent(page.getCurrent());
        result.setRecords(this.pos2vos(page.getRecords()));
        return result;
    }

    @Override
    public IPage<Map<String, Object>> selectMapsPageMP(IPage<P> page, Wrapper<P> queryWrapper) {
        return null;
    }

    @Override
    public List<V> findByIds(List ids) {
        return pos2vos(baseMapper.selectBatchIds(ids), false);
    }


    protected Class<P> poClass;

    protected Class<V> voClass;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.poClass = getTypeArgumentsClass(1);
        this.voClass = getTypeArgumentsClass(0);
        init();
    }

    /**
     * 初始化
     */
    public void init() {
    }

    /**
     * 获取泛型class
     *
     * @param index 第几个
     * @return 泛型
     */
    private <T> Class<T> getTypeArgumentsClass(int index) {
        Class<T> tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[index];
        return tClass;
    }

    /**
     * vo转DO
     *
     * @param vo vo
     * @return
     */
    @Override
    public P v2p(V vo) {
        return (P) vo;
    }

    @Override
    public Class<P> getPoClass() {
        return this.poClass;
    }

    @Override
    public Class<V> getVOClass() {
        return this.voClass;
    }

    /**
     * po转vo
     *
     * @param p p
     * @return vo
     */
    public V p2v(P p) {
        return p2v(p, false);
    }

    /**
     * po转vo
     *
     * @param p p
     * @return vo
     */
    public V p2v(P p, boolean needTrans) {
        try {
            if (p == null) {
                return null;
            }
            V vo = voClass.newInstance();
            BeanUtils.copyProperties(p, vo);
            if (needTrans) {
                transService.transOne(vo);
            }
            return vo;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * do集合转vo集合
     *
     * @param pos
     * @return
     */
    public List<V> pos2vos(List<P> pos) {
        return pos2vos(pos, false);
    }

    public List<V> pos2vos(List<P> pos, boolean isTrans) {
        List<V> vos = ListUtils.copyListToList(pos, this.getVOClass());
        if (isTrans) {
            transService.transMore(vos);
        }
        return vos;
    }

    /**
     * 自动删除子表数据
     *
     * @param field       字段
     * @param mainTblPkey 主表pkey
     * @return
     */
    public int deleteForMainTblPkey(String field, Object mainTblPkey) {
        try {
            P p = this.getPoClass().newInstance();
            ReflectUtils.setValue(p, field, mainTblPkey);
            return this.deleteBean(p);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据主表id和 子表字段查询子表数据
     *
     * @param field       子表字段
     * @param mainTblPkey 主表id
     * @return 多少条子表数据
     */
    public Long findCountForMainTblPkey(String field, Object mainTblPkey) {
        try {
            P p = this.getPoClass().newInstance();
            ReflectUtils.setValue(p, field, mainTblPkey);
            return this.findCount(p);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 判断存在重复数据-需要配合NotRepeatField 与 NotRepeatDesc 配套注解一起用
     *
     * @param newData  新数据
     * @param isUpdate 是否是更新(更新会排除掉自己)
     */
    @Transactional(rollbackFor = Exception.class)
    protected void checkIsExist(P newData, boolean isUpdate) {
        QueryWrapper<P> wrapper = new QueryWrapper<>();
        List<Field> fieldList = ReflectUtils.getAnnotationField(newData.getClass(), NotRepeatField.class);
        // 如果没配置直接return false
        if (fieldList.isEmpty()) {
            return;
        }
        TableField tableField = null;
        try {
            for (Field field : fieldList) {
                field.setAccessible(true);
                tableField = field.getAnnotation(TableField.class);
                //如果被校验字段为null或者空则不校验
                if (CheckUtils.isNullOrEmpty(field.get(newData))) {
                    return;
                }
                wrapper.eq(tableField.value(), ConverterUtils.toString(field.get(newData)));
            }
            if (isUpdate) {
                fieldList = ReflectUtils.getAnnotationField(newData.getClass(), TableId.class);
                if (fieldList.isEmpty()) {
                    throw new ParamsInValidException(newData.getClass() + "没有配置TableId注解");
                }
                fieldList.get(0).setAccessible(true);
                TableId tableId = fieldList.get(0).getAnnotation(TableId.class);
                wrapper.ne(tableId.value(), fieldList.get(0).get(newData));
            }
        } catch (IllegalAccessException e) {
            log.error("反射错误", e);
        }
        if (this.selectCountMP(wrapper) > 0) {
            throw new ParamsInValidException(newData.getClass().getAnnotation(NotRepeatDesc.class).value());
        }
    }

    public boolean isFeign() {
        return false;
    }

    protected <E> boolean executeBatch(Collection<E> list, int batchSize, BiConsumer<SqlSession, E> consumer) {
        return SqlHelper.executeBatch(this.poClass, this.mybatisLog, list, batchSize, consumer);
    }


    /**
     * mapper的类名
     */
    private String mapperClassName;

    protected synchronized String getSqlStatement(SqlMethod sqlMethod) {
        String mapperClass = mapperClassName;
        // 如果没有指定mapperClass，则取根据po类名自动拼接
        if (StringUtils.isEmpty(mapperClassName)) {
            mapperClass = this.getPoClass().getName().replace(".po", ".mapper")
                    .replace("PO", "Mapper").replace("Po", "Mapper");
            try {
                Class.forName(mapperClass);
                mapperClassName = mapperClass;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("批处理需要mapper类:" + mapperClass + ",请按照规范放置po位置和mapper位置，以及检查命名规范");
            }
        }
        return mapperClass + "." + sqlMethod.getMethod();
    }
}
