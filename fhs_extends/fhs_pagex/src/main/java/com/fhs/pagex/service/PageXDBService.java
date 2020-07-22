package com.fhs.pagex.service;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.CacheUpdateManager;
import com.fhs.bislogger.api.context.BisLoggerContext;
import com.fhs.bislogger.constant.LoggerConstant;
import com.fhs.bislogger.vo.LogHistoryDataVO;
import com.fhs.common.utils.*;
import com.fhs.core.base.autodel.service.AutoDelService;
import com.fhs.core.base.pojo.SuperBean;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.cache.service.RedisCacheService;
import com.fhs.core.exception.BusinessException;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.trans.service.impl.TransService;
import com.fhs.logger.Logger;
import com.fhs.pagex.dox.DefaultPageXDO;
import com.fhs.pagex.vo.PagexAddVO;
import com.google.gson.JsonObject;
import com.mybatis.jpa.cache.JpaTools;
import com.mybatis.jpa.common.ColumnNameUtil;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * pagex dbservice
 * 1 根据配置文件拼接sql
 * 2 根据namespace 执行sql返回结果
 *
 * @ProjectName: framework_v2_idea2
 * @Package: com.fhs.pagex.service
 * @ClassName: PageXDBService
 * @Author: JackWang
 * @CreateDate: 2018/12/3 0003 20:01
 * @UpdateUser: JackWang
 * @UpdateDate: 2018/12/3 0003 20:01
 * @Version: 1.0
 */
@Service
public class PageXDBService {


    private static final Logger LOG = Logger.getLogger(PageXDBService.class);

    @Autowired
    private SqlSessionTemplate sqlsession;


    @Autowired
    private TransService transService;

    @Autowired
    private AutoDelService autoDelService;

    @Autowired
    private RedisCacheService redisCacheService;

    private static final String DO_CACHE_KEY = "docache:";

    @Autowired
    private CacheUpdateManager cacheUpdateManager;


    /**
     * 需要翻译的namespace集合
     */
    private Set<String> needTransNamespaceSet = Collections.synchronizedSet(new HashSet<>());

    /**
     * key namespace value 对应namespace的PO class
     */
    private Map<String, Class> namespaceClassMap = new ConcurrentHashMap<>();



    /**
     * 插入一条数据到db
     *
     * @param paramMap  参数
     * @param namespace namespace
     * @return
     */
    public int insert(EMap<String, Object> paramMap, String namespace) {
        insertAndUpdateX(paramMap, namespace, true);
        int result =  sqlsession.insert(getSqlNamespace() + namespace + "_insertPageX", paramMap);
        refreshCache(namespace);
        return result;
    }

    /**
     * 处理一对多
     *
     * @param paramMap  参数
     * @param namespace namespace
     */
    private void insertAndUpdateX(EMap<String, Object> paramMap, String namespace, boolean isAdd) {
        PagexAddVO addDTO = PagexDataService.SIGNEL.getPagexAddDTOFromCache(namespace);
        Map<String, Object> modelConfig = addDTO.getModelConfig();
        //是否存在一对多
        if (ConverterUtils.toBoolean(addDTO.getModelConfig().get("isOne2X"))) {
            String createUser = isAdd ? paramMap.getStr("createUser") : paramMap.getStr("updateUser");
            String groupCode = paramMap.getStr("groupCode");
            String pkey = isAdd ? paramMap.getStr("pkey") : paramMap.getStr(modelConfig.get("pkey"));
            List<String> namespaces = new ArrayList<>();
            List<Map<String, Object>> fields = addDTO.getFormFieldSett();
            //把所有的namespace拿到
            for (Map<String, Object> field : fields) {
                if ("one2x".equals(field.get("type"))) {
                    Object allowEdit = field.get("allowEdit");
                    if(allowEdit == null || (boolean)allowEdit){
                        namespaces.add(ConverterUtils.toString(field.get("namespace")));
                    }
                }
            }
            // ConverterUtils.toString(modelConfig.get("xNamespaces")).split(",");
            JSONArray tempJsonArray = null;
            Map<String, String> deleteFKeyParam = new HashMap<>();
            deleteFKeyParam.put("fkey", pkey);
            //遍历一对多的数据然后插入
            for (String xNamespace : namespaces) {
                int n = sqlsession.delete(getSqlNamespace() + xNamespace + "_delFkeyPageX", deleteFKeyParam);
                // 数据库有改动才会产生日志
                if (n > 0 && BisLoggerContext.isNeedLogger()) {
                    BisLoggerContext.addExtParam(xNamespace,pkey,LoggerConstant.OPERATOR_TYPE_DEL);
                    BisLoggerContext.addHistoryData(JSON.parseObject(JSON.toJSONString(deleteFKeyParam),LogHistoryDataVO.class),xNamespace);
                }
                //取出fkey的列名字
                String fkeyField = ColumnNameUtil.underlineToCamel(ConverterUtils.toString(PagexDataService.SIGNEL.getPagexAddDTOFromCache(xNamespace).getModelConfig().get("fkey")));
                String pkeyField = ConverterUtils.toString(PagexDataService.SIGNEL.getPagexAddDTOFromCache(xNamespace).getModelConfig().get("pkey"));
                tempJsonArray = JSON.parseArray(ConverterUtils.toString(paramMap.get(xNamespace)));
                for (int i = 0; i < tempJsonArray.size(); i++) {
                    JSONObject extendsChild = tempJsonArray.getJSONObject(i);
                    //没有id就生成一个
                    if (!extendsChild.containsKey("pkey")) {
                        extendsChild.put("pkey", StringUtil.getUUID());
                    }
                    redisCacheService.remove(DO_CACHE_KEY + xNamespace + ":" + extendsChild.get("pkey"));
                    extendsChild.put(fkeyField, pkey);
                    extendsChild.put("updateUser", createUser);
                    extendsChild.put("createUser", createUser);
                    extendsChild.put("groupCode", groupCode);
                    sqlsession.insert(getSqlNamespace() + xNamespace + "_insertPageX", extendsChild);
                    if (BisLoggerContext.isNeedLogger()) {
                        BisLoggerContext.addExtParam(xNamespace, extendsChild.get(fkeyField), isAdd?LoggerConstant.OPERATOR_TYPE_ADD:LoggerConstant.OPERATOR_TYPE_UPDATE);
                        BisLoggerContext.addHistoryData(JSONObject.parseObject(extendsChild.toJSONString(),LogHistoryDataVO.class),xNamespace);
                    }
                }
            }
        }
        refreshCache(namespace);
    }

    /**
     * 刷新缓存
     * @param namespace 命名空间
     */
    private void refreshCache(String namespace){
        cacheUpdateManager.clearCache(namespace);
        //发送刷新的消息
        Map<String, String> message = new HashMap<>();
        message.put("transType", TransType.AUTO_TRANS);
        message.put("namespace", namespace);
        redisCacheService.convertAndSend("trans", JSONUtils.toJSONString(message));
    }

    /**
     * 获取数据带分页
     *
     * @param param     参数
     * @param namespace namespace
     * @return json
     */
    public String findListPage(Map<String, Object> param, String namespace) {
        List<Map<String, Object>> rows = sqlsession.selectList(getSqlNamespace() + namespace + "_findPageX", param);
        //如果不包含的话就直接转换为json
        if (!needTransNamespaceSet.contains(namespace)) {
            return JsonUtils.list2json(rows);
        }
        final Class clazz = namespaceClassMap.get(namespace);
        final List<VO> superBeans = new ArrayList<>();
        // List map 转List SuperBean
        rows.forEach(row -> {
            superBeans.add(map2VO(row, clazz));
        });
        transService.transMore(superBeans);
        return JsonUtils.list2json(superBeans);
    }

    /**
     * map的数据转换为vo
     * @param mapData
     * @param clazz
     * @return
     */
    public VO map2VO(Map<String, Object> mapData,Class clazz){
        try {
            Object tempObj = clazz.newInstance();
            VO tempSuperBenn = (VO) tempObj;
            Field field = null;
            for (String key : mapData.keySet()) {
                field = ReflectUtils.getDeclaredField(clazz, key);
                if(field == null){
                    continue;
                }
                if (field.getType() == Date.class) {
                    ReflectUtils.setValue(tempObj, key, DateUtils.parseStr(mapData.get(key).toString(),DateUtils.DATETIME_PATTERN));
                } else if (field.getType() == Integer.class) {
                    ReflectUtils.setValue(tempObj, key, ConverterUtils.toInteger(mapData.get(key)));
                }
                else{
                    ReflectUtils.setValue(tempObj, key, ConverterUtils.toString(mapData.get(key)));
                }
            }
            return tempSuperBenn;
        } catch (InstantiationException e) {
            LOG.error(this, e);
            throw new BusinessException("findListPager反射错误");
        } catch (IllegalAccessException e) {
            LOG.error(this, e);
            throw new BusinessException("findListPager反射错误参数错误");
        }
    }


    /**
     * 查询总数
     *
     * @param param     参数
     * @param namespace namespace
     * @return 总数
     */
    public Integer findPageCount(Map<String, Object> param, String namespace) {
        Map<String, Object> countRow = sqlsession.selectOne(getSqlNamespace() + namespace + "_findPageCountPageX", param);
        return ConverterUtils.toInt(countRow.get("countNum"));
    }

    /**
     * 获取bena的json
     *
     * @param param     过滤参数
     * @param namespace namespace
     * @return bean的json
     */
    public String findBean(Map<String, Object> param, String namespace) {
        return JsonUtils.bean2json(sqlsession.selectOne(getSqlNamespace() + namespace + "_findBeanPageX", param));
    }

    /**
     * 获取bean的map
     *
     * @param param     过滤参数
     * @param namespace namespace
     * @return bean的json
     */
    public Map<String,Object> findBeanMap(Map<String, Object> param, String namespace) {
        return sqlsession.selectOne(getSqlNamespace() + namespace + "_findBeanPageX", param);
    }



    /**
     * 获取bena的json
     *
     * @param paramMap  过滤参数
     * @param namespace namespace
     * @return 更新了几行默认是1
     */
    public int update(EMap<String, Object> paramMap, String namespace) {
        insertAndUpdateX(paramMap, namespace, false);
        redisCacheService.remove(DO_CACHE_KEY + namespace + ":" + paramMap.get("id"));
        int result = sqlsession.update(getSqlNamespace() + namespace + "_updatePageX", paramMap);
        refreshCache(namespace);
        return result;
    }

    /**
     * 获取bena的json
     *
     * @param pkey      主键
     * @param namespace namespace
     * @return 删除了几行 理论来说是1
     */
    public int del(String pkey, String namespace) {
        autoDelService.deleteCheck(namespace,pkey);
        redisCacheService.remove(DO_CACHE_KEY + namespace + ":" + pkey);
        int result =  sqlsession.delete(getSqlNamespace() + namespace + "_delPageX", pkey);
        autoDelService.deleteItemTBL(namespace,pkey);
        refreshCache(namespace);
        return result;
    }

    /**
     * 获取mybatis 命名空间
     *
     * @return
     */
    private String getSqlNamespace() {
        String sqlNameSpace = JpaTools.statementAdapterMap.get(DefaultPageXDO.class.getName()).getNameSpace();
        return sqlNameSpace + ".";
    }


    public Map<String, Class> getNamespaceClassMap() {
        return this.namespaceClassMap;
    }

    public Set<String> getNeedTransNamespaceSet() {
        return needTransNamespaceSet;
    }
}
