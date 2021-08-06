package com.fhs.core.base.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheUpdateManager;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fhs.bislogger.api.context.BisLoggerContext;
import com.fhs.bislogger.constant.LoggerConstant;
import com.fhs.common.constant.Constant;
import com.fhs.common.spring.SpringContextUtil;
import com.fhs.common.utils.*;
import com.fhs.core.base.anno.NotRepeatDesc;
import com.fhs.core.base.anno.NotRepeatField;
import com.fhs.core.base.autodel.service.AutoDelService;
import com.fhs.core.base.dox.BaseDO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.base.service.BaseService;
import com.fhs.core.base.vo.FhsPager;
import com.fhs.core.cache.annotation.Cacheable;
import com.fhs.core.cache.annotation.Namespace;
import com.fhs.core.cache.service.RedisCacheService;
import com.fhs.core.trans.anno.AutoTrans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.trans.service.AutoTransAble;
import com.fhs.core.trans.service.impl.TransService;
import com.fhs.logger.Logger;
import com.github.liangbaika.validate.exception.ParamsInValidException;
import com.mybatis.jpa.annotation.CatTableFlag;
import com.mybatis.jpa.cache.JpaTools;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务层base类，主要提供对数据库的CRUD操作
 *
 * @author wanglei
 * @version [版本号, 2015年5月27日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class BaseServiceImpl<V extends VO, D extends BaseDO> implements BaseService<V, D>, AutoTransAble<V>,  InitializingBean {

    protected final Logger log = Logger.getLogger(this.getClass());

    /**
     * 缓存 默认时间：半个小时
     */
    @CreateCache(expire = 1800, name = "docache:", cacheType = CacheType.BOTH)
    private Cache<String, D> doCache;

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


    /**
     * 缓存的namespace
     */
    private String naspaces;


    /**
     * 利用spring4新特性泛型注入
     */
    @Autowired
    protected FhsBaseMapper<D> baseMapper;

    @Autowired
    private CacheUpdateManager cacheUpdateManager;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private AutoDelService autoDelService;

    private static final Set<String> exist = new HashSet<>();

    @Autowired
    private SqlSessionTemplate sqlsession;



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
    @Deprecated
    public int addFromMap(Map<String, Object> info) {
        int result = baseMapper.addFromMap(info);
        this.refreshCache();
        return result;
    }

    @Override
    public int add(D bean) {
        this.initPkeyAndIsDel(bean);
        checkIsExist(bean,  false);
        int result = baseMapper.insertSelective(bean);
        BisLoggerContext.addExtParam(this.namespace, bean.getPkey(), LoggerConstant.OPERATOR_TYPE_ADD);
        BisLoggerContext.addHistoryData(bean, this.namespace);
        this.refreshCache();
        this.addCache(bean);
        return result;
    }

    @Override
    @Deprecated
    public boolean updateFormMap(Map<String, Object> map) {
        boolean result = baseMapper.updateFormMap(map) > 0;
        this.refreshCache();
        return result;
    }

    @Override
    public boolean update(D bean) {
        checkIsExist(bean,  true);
        boolean result = this.updateJpa(bean);
        this.refreshCache();
        this.updateCache(bean);
        return result;
    }


    @Override
    @Deprecated
    public boolean updateJpa(D bean) {
        checkIsExist(bean,  true);
        boolean result = baseMapper.updateSelectiveById(bean) > 0;
        if (BisLoggerContext.isNeedLogger()) {
            BisLoggerContext.addExtParam(this.namespace, bean.getPkey(), LoggerConstant.OPERATOR_TYPE_UPDATE);
            BisLoggerContext.addHistoryData(this.selectById(bean.getPkey()), this.namespace);
        }
        this.refreshCache();
        this.updateCache(bean);
        return result;
    }

    @Override
    @Deprecated
    public boolean deleteFromMap(Map<String, Object> map) {
        boolean result = baseMapper.deleteFromMap(map) > 0;
        this.refreshCache();
        return result;
    }

    @Override
    public boolean delete(D bean) {
        boolean result = baseMapper.deleteBean(bean) > 0;
        BisLoggerContext.addExtParam(this.namespace, bean.getPkey(), LoggerConstant.OPERATOR_TYPE_DEL);
        this.refreshCache();
        return result;
    }

    @Override
    @Deprecated
    public int findCountFromMap(Map<String, Object> map) {
        int result = baseMapper.findCountFromMap(map);
        return result;
    }

    @Override
    public int findCount(D bean) {
        bean.setIsDelete(Constant.INT_FALSE);
        return (int) baseMapper.selectCountJpa(bean);
    }

    @Override
    public int findCountJpa(D bean) {
        bean.setIsDelete(Constant.INT_FALSE);
        String extWhereSql = bean.getAdvanceSearchSql();
        if(CheckUtils.isNullOrEmpty(extWhereSql)){
            extWhereSql = null;
        }
        return (int)baseMapper.selectCountAdvance(bean,extWhereSql);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public List<V> findForList(D bean) {
        bean.setIsDelete(Constant.INT_FALSE);
        List<D> dos = baseMapper.selectPageJpa(bean, -1, -1);
        return dos2vos(dos);
    }

    /**
     * 查询数据 参数为object
     *
     * @param bean bean
     * @return 查询出来的数据集合
     */
    @SuppressWarnings({"unchecked"})
    @Override
    public List<V> findForList(D bean, int pageStart, int pageSize) {
        bean.setIsDelete(Constant.INT_FALSE);
        List<D> dos = baseMapper.selectPageJpa(bean, pageStart, pageSize);
        return dos2vos(dos);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    @Deprecated
    public List<V> findForListFromMap(Map<String, Object> map) {
        List<D> dos = baseMapper.findForListFromMap(map);
        return dos2vos(dos);
    }

    @Override
    public List<Map<String, Object>> findMapList(Map<String, Object> map) {
        return baseMapper.findMapList(map);
    }

    @Override
    public V findBeanFromMap(Map<String, Object> map) {
        return d2v(baseMapper.findBeanFromMap(map));
    }

    @Override
    public V findBean(D bean) {
        bean.setIsDelete(Constant.INT_FALSE);
        return d2v(baseMapper.selectBean(bean));
    }


    @Override
    @Deprecated
    public int updateBatch(List<Map<String, Object>> list) {
        int result = baseMapper.updateBatch(list);
        this.refreshCache();
        return result;
    }

    @Override
    @Deprecated
    public int addBatch(Map<String, Object> paramMap) {
        int result = baseMapper.addBatch(paramMap);
        this.refreshCache();
        return result;
    }


    @Override
    public int insertSelective(D entity) {
        initPkeyAndIsDel(entity);
        addCache(entity);
        checkIsExist(entity,  false);
        int result = baseMapper.insertSelective(entity);
        this.refreshCache();
        BisLoggerContext.addExtParam(this.namespace, entity.getPkey(), LoggerConstant.OPERATOR_TYPE_ADD);
        BisLoggerContext.addHistoryData(entity, this.namespace);
        return result;
    }

    private void initPkeyAndIsDel(D entity) {
        entity.setIsDelete(Constant.INT_FALSE);
        Field field = entity.getIdField(false);
        if (field != null) {
            field.setAccessible(true);
            try {
                if (field.get(entity) == null && field.getType() == String.class) {
                    field.set(entity, StringUtil.getUUID());
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
    protected void addCache(D entity) {
        if (this.isCacheable && JpaTools.persistentMetaMap.containsKey(entity.getClass().getName())) {
            String pkey = getPkeyVal(entity);
            this.doCache.put(namespace + ":" + pkey, entity);
        }
    }

    /**
     * 刷新缓存,包括do缓存,autotrans缓存,以及其他模块依赖的缓存
     */
    protected void refreshCache() {
        cacheUpdateManager.clearCache(namespace);
        AutoTrans autoTrans = this.getClass().getAnnotation(AutoTrans.class);
        if (autoTrans != null) {
            //发送刷新的消息
            Map<String, String> message = new HashMap<>();
            message.put("transType", TransType.AUTO_TRANS);
            message.put("namespace", autoTrans.namespace());
            redisCacheService.convertAndSend("trans", JSONUtils.toJSONString(message));
        }
        if (this.naspaces != null) {
            this.cacheUpdateManager.clearCache(naspaces);
        }
    }

    /**
     * 清除缓存
     *
     * @param pkey 主键
     */
    protected void removeCache(Object pkey) {
        if (this.isCacheable) {
            this.doCache.remove(namespace + ":" + pkey);
        }
    }

    /**
     * 获取主键
     *
     * @param entity do
     * @return 主键值
     */
    private String getPkeyVal(D entity) {
        return ConverterUtils.toString(entity.getPkey());
    }


    @Override
    public int batchInsert(List<D> list) {
        if(list == null || list.isEmpty()){
            return 0;
        }
        for (D d : list) {
            initPkeyAndIsDel(d);
        }
        int result = baseMapper.batchInsert(list);
        for (D d : list) {
            BisLoggerContext.addExtParam(this.namespace, d.getPkey(), LoggerConstant.OPERATOR_TYPE_ADD);
            BisLoggerContext.addHistoryData(d, this.namespace);
        }
        this.refreshCache();
        return result;
    }

    @Override
    public int deleteById(Object primaryValue) {
        autoDelService.deleteCheck(this.naspaces, primaryValue);
        D d = baseMapper.selectByIdJpa(primaryValue);
        d.setIsDelete(Constant.INT_TRUE);
        int result = baseMapper.updateByIdJpa(d);
        autoDelService.deleteItemTBL(this.naspaces, primaryValue);
        this.refreshCache();
        removeCache(primaryValue);
        BisLoggerContext.addExtParam(this.namespace, primaryValue, LoggerConstant.OPERATOR_TYPE_DEL);
        return result;
    }

    @Override
    public int deleteByIdsMp(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            BisLoggerContext.addExtParam(this.namespace, id, LoggerConstant.OPERATOR_TYPE_DEL);
        }
        return baseMapper.deleteBatchIds(idList);
    }

    @Override
    public int deleteMp(Wrapper<D> wrapper) {
        return baseMapper.delete(wrapper);
    }

    @Override
    public int updateSelectiveById(D entity) {
        checkIsExist(entity,  true);
        updateCache(entity);
        this.refreshCache();
        int reuslt = baseMapper.updateSelectiveById(entity);
        if (BisLoggerContext.isNeedLogger()) {
            BisLoggerContext.addExtParam(this.namespace, entity.getPkey(), LoggerConstant.OPERATOR_TYPE_UPDATE);
            BisLoggerContext.addHistoryData(this.selectById(entity.getPkey()), this.namespace);
        }
        return reuslt;
    }

    /**
     * 缓存更新
     *
     * @param entity
     */
    protected void updateCache(D entity) {
        if (this.isCacheable) {
            String pkey = this.getPkeyVal(entity);
            this.doCache.remove(namespace + ":" + pkey);
            this.doCache.put(namespace + ":" + pkey, entity);
        }
    }

    @Override
    public int batchUpdate(List<D> entitys) {
        if (entitys == null || entitys.isEmpty()) {
            return 0;
        }
        int result = baseMapper.batchUpdateById(entitys);
        for (D entity : entitys) {
            updateCache(entity);
        }

        if (BisLoggerContext.isNeedLogger()) {
            List<Object> ids = entitys.stream().map(D::getPkey).collect(Collectors.toList());
            List<V> vos = this.findByIds(ids);
            for (V vo : vos) {
                BisLoggerContext.addExtParam(this.namespace, vo.getPkey(), LoggerConstant.OPERATOR_TYPE_UPDATE);
                BisLoggerContext.addHistoryData(this.selectById(vo.getPkey()), this.namespace);
            }
        }
        this.refreshCache();
        return result;
    }

    @Override
    public V selectById(Object primaryValue) {
        if (this.isCacheable) {
            String pkey = ConverterUtils.toString(primaryValue);
            D result = this.doCache.get(namespace + ":" + pkey);
            if (result == null) {
                result = baseMapper.selectByIdJpa(primaryValue);
                if (result != null) {
                    this.doCache.put(namespace + ":" + pkey, result);
                }
            }
            return d2v(result,false);
        }
        return d2v(baseMapper.selectByIdJpa(primaryValue),false);
    }

    @Override
    public List<V> selectPage(D entity, long pageStart, long pageSize) {
        entity.setIsDelete(Constant.INT_FALSE);
        return dos2vos(baseMapper.selectPageJpa(entity, pageStart, pageSize));
    }

    @Override
    public List<V> selectPageForOrder(D entity, long pageStart, long pageSize, String orderBy) {
        entity.setIsDelete(Constant.INT_FALSE);
        String extWhereSql = entity.getAdvanceSearchSql();
        if(CheckUtils.isNullOrEmpty(extWhereSql)){
            extWhereSql = null;
        }
        return dos2vos(baseMapper.selectAdvance(entity, extWhereSql,pageStart, pageSize, orderBy));
    }


    @Override
    public long selectCount(D entity) {
        entity.setIsDelete(Constant.INT_FALSE);
        return baseMapper.selectCountJpa(entity);
    }

    @Override
    public List<V> select() {
        return ListUtils.copyListToList(baseMapper.select(), this.getVOClass());
    }

    @Override
    public int batchInsertCatTable(List<D> list, @CatTableFlag String flag) {
        return baseMapper.batchInsertCatTable(list, flag);
    }

    @Override
    public V selectByIdCatTable(String id, @CatTableFlag String catTableFlag) {
        return d2v(baseMapper.selectByIdCatTable(id, catTableFlag));
    }

    @Override
    public V selectBean(D param) {
        param.setIsDelete(Constant.INT_FALSE);
        return d2v(baseMapper.selectBean(param));
    }


    @Override
    public int deleteBean(D entity) {
        List<D> dos = baseMapper.selectPageJpa(entity, -1, -1);
        if (dos.isEmpty()) {
            return 0;
        }
        for (D d : dos) {
            d.setIsDelete(Constant.INT_TRUE);
            autoDelService.deleteCheck(this.naspaces, d.getPkey());
            autoDelService.deleteItemTBL(this.naspaces, d.getPkey());
            BisLoggerContext.addExtParam(this.namespace, d.getPkey(), LoggerConstant.OPERATOR_TYPE_DEL);
        }
        //批量修改为已删除
        return baseMapper.batchUpdateById(dos);
    }


    @Override
    public Object callSqlIdForOne(String sqlId, Object param) {
        return sqlsession.selectOne(naspaces + "." + sqlId, param);
    }

    /**
     * 初始化namespace
     */
    public void initNamespace() {
        if (naspaces == null) {
            String modelName = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
            naspaces = JpaTools.statementAdapterMap.get(modelName).getNameSpace();
        }
    }

    @Override
    public List<V> callSqlIdForMany(String sqlId, Object param) {
        return sqlsession.selectList(naspaces + "." + sqlId, param);
    }

    @Override
    public int callSqlIdForInt(String sqlId, Object param) {
        return sqlsession.selectOne(naspaces + "." + sqlId, param);
    }


    @Override
    public int deleteBatchIds(List<?> idList) {
        if (idList == null || idList.isEmpty()) {
            return 0;
        }
        for (Object id : idList) {
            autoDelService.deleteCheck(this.naspaces, id);
            autoDelService.deleteItemTBL(this.naspaces, id);
            BisLoggerContext.addExtParam(this.namespace, id, LoggerConstant.OPERATOR_TYPE_DEL);
        }
        List<D> dos = baseMapper.selectByIds(idList);
        if (dos.isEmpty()) {
            return 0;
        }
        for (D d : dos) {
            d.setIsDelete(Constant.INT_TRUE);
        }
        return baseMapper.batchUpdateById(dos);
    }

    @Override
    public List<V> selectBatchIdsMP(Collection<? extends Serializable> idList) {
        return dos2vos(baseMapper.selectBatchIds(idList));
    }

    @Override
    public V selectOneMP(Wrapper<D> queryWrapper) {
        return d2v(baseMapper.selectOne(queryWrapper));
    }

    @Override
    public Integer selectCountMP(Wrapper<D> queryWrapper) {
        return baseMapper.selectCount(queryWrapper);
    }

    @Override
    public List<V> selectListMP(Wrapper<D> queryWrapper) {
        return dos2vos(baseMapper.selectList(queryWrapper));
    }

    @Override
    public List<Map<String, Object>> selectMapsMP(Wrapper<D> queryWrapper) {
        return baseMapper.selectMaps(queryWrapper);
    }

    @Override
    public List<Object> selectObjsMP(Wrapper<D> queryWrapper) {
        return baseMapper.selectObjs(queryWrapper);
    }

    @Override
    public IPage<V> selectPageMP(IPage<D> page, Wrapper<D> queryWrapper) {
        page = baseMapper.selectPage(page, queryWrapper);
        FhsPager<V> result = new FhsPager<V>();
        result.setTotal(page.getTotal());
        result.setPageSize(page.getSize());
        result.setPage(page.getCurrent());
        result.setRecords(this.dos2vos(page.getRecords()));
        return result;
    }

    @Override
    public IPage<Map<String, Object>> selectMapsPageMP(IPage<D> page, Wrapper<D> queryWrapper) {
        return null;
    }

    @Override
    public List<V> findByIds(List<?> ids) {
        return ListUtils.copyListToList(baseMapper.selectByIds(ids), this.getVOClass());
    }


    protected Class<D> doClass;

    protected Class<V> voClass;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.doClass = getTypeArgumentsClass(1);
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
    public D v2d(V vo) {
        return (D) vo;
    }

    @Override
    public Class<D> getDOClass() {
        return this.doClass;
    }

    @Override
    public Class<V> getVOClass() {
        return this.voClass;
    }

    /**
     * po转vo
     *
     * @param d d
     * @return vo
     */
    public V d2v(D d) {
        return d2v( d, true);
    }

    /**
     * po转vo
     *
     * @param d d
     * @return vo
     */
    public V d2v(D d,boolean needTrans) {
        try {
            if (d == null) {
                return null;
            }
            V vo = voClass.newInstance();
            BeanUtils.copyProperties(d, vo);
            if(needTrans){
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
     * @param dos
     * @return
     */
    public List<V> dos2vos(List<D> dos) {
        List<V> vos = ListUtils.copyListToList(dos, this.getVOClass());
        transService.transMore(vos);
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
            D d = this.getDOClass().newInstance();
            ReflectUtils.setValue(d, field, mainTblPkey);
            return this.deleteBean(d);
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
    public int findCountForMainTblPkey(String field, Object mainTblPkey) {
        try {
            D d = this.getDOClass().newInstance();
            ReflectUtils.setValue(d, field, mainTblPkey);
            return this.findCount(d);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 判断存在重复数据-需要配合NotRepeatField 与 NotRepeatDesc 配套注解一起用
     *
     * @param newData  新数据
     * @param isUpdate 是否是更新(更新会排除掉自己)
     */
    @Transactional(rollbackFor = Exception.class)
    protected void checkIsExist(D newData, boolean isUpdate) {
        QueryWrapper<D> wrapper = new QueryWrapper<>();
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
                if(CheckUtils.isNullOrEmpty(field.get(newData))){
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
                wrapper.ne(tableId.value(), ConverterUtils.toString(fieldList.get(0).get(newData)));
            }
        } catch (IllegalAccessException e) {
            log.error("反射错误", e);
        }
        if (this.selectCountMP(wrapper) > 0) {
            throw new ParamsInValidException(newData.getClass().getAnnotation(NotRepeatDesc.class).value());
        }
    }



}
