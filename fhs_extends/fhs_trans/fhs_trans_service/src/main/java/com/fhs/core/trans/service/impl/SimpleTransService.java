package com.fhs.core.trans.service.impl;

import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.common.utils.ReflectUtils;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 简单翻译
 */
@Service
public class SimpleTransService implements ITransTypeService, InitializingBean {

    public static final Logger LOGGER = LoggerFactory.getLogger(SimpleTransService.class);

    /**
     * 如果直接去表里查询，放到这个cache中
     */
    private ThreadLocal<Map<String, Map<String, String>>> threadLocalCache = new ThreadLocal<>();


    @Autowired
    private SimpleTransDiver transDiver;

    @Override
    public void transOne(VO obj, List<Field> toTransList) {
        Trans tempTrans = null;
        for (Field tempField : toTransList) {
            tempTrans = tempField.getAnnotation(Trans.class);
            String alias = tempTrans.alias();
            String pkey = ConverterUtils.toString(ReflectUtils.getValue(obj, tempField.getName()));
            if (StringUtils.isEmpty(pkey)) {
                continue;
            }
            Map<String, String> transCache = null;
            // 主键可能是数组
            pkey = pkey.replace("[", "").replace("]", "");
            if (pkey.contains(",")) {
                String[] pkeys = pkey.split(",");
                transCache = new LinkedHashMap<>();
                Map<String, String> tempTransCache = null;
                for (String tempPkey : pkeys) {
                    tempTransCache = getTempTransCacheMap(tempTrans, tempPkey);
                    if (tempTransCache == null) {
                        LOGGER.error("auto trans缓存未命中:" + tempTrans.target().getName() + "_" + tempPkey);
                        continue;
                    }
                    // 比如学生表  可能有name和age 2个字段
                    for (String key : tempTransCache.keySet()) {
                        transCache.put(key, transCache.containsKey(key) ? transCache.get(key) + "," + tempTransCache.get(key) : tempTransCache.get(key));
                    }
                }
            } else {
                transCache = getTempTransCacheMap(tempTrans, pkey);
                if (transCache == null) {
                    LOGGER.error("auto trans缓存未命中:" + tempTrans.target().getName() + "_" + pkey);
                    continue;
                }
            }
            setRef(tempTrans, obj, transCache);
            Map<String, String> transMap = obj.getTransMap();
            if (transMap == null) {
                continue;
            }
            if (!CheckUtils.isNullOrEmpty(alias)) {
                Map<String, String> tempMap = new HashMap<>();
                Set<String> keys = transCache.keySet();
                for (String key : keys) {
                    tempMap.put(alias + key.substring(0, 1).toUpperCase() + key.substring(1), transCache.get(key));
                }
                transCache = tempMap;
            }
            Set<String> keys = transCache.keySet();
            for (String key : keys) {
                if (CheckUtils.isNullOrEmpty(transMap.get(key))) {
                    transMap.put(key, transCache.get(key));
                }
            }
        }
    }

    @Override
    public void transMore(List<? extends VO> objList, List<Field> toTransList) {
        threadLocalCache.set(new HashMap<>());
        // 由于一些表数据比较多，所以部分数据不是从缓存取的，是从db先放入缓存的，翻译完了释放掉本次缓存的数据
        for (Field tempField : toTransList) {
            tempField.setAccessible(true);
            Trans tempTrans = tempField.getAnnotation(Trans.class);
            String namespace = tempTrans.key();
            // 如果是 good#student  翻译出来应该是 goodStuName goodStuAge  customer#customer  customerName
            if (namespace.contains("#")) {
                namespace = namespace.substring(0, namespace.indexOf("#"));
            }
            Set<String> ids = new HashSet<>();
            objList.forEach(obj -> {
                try {
                    Object tempId = tempField.get(obj);
                    if (CheckUtils.isNotEmpty(tempId)) {
                        String pkey = ConverterUtils.toString(tempId).replace("[", "").replace("]", "");
                        if (pkey.contains(",")) {
                            String[] pkeys = pkey.split(",");
                            for (String id : pkeys) {
                                ids.add(id);
                            }
                        } else {
                            ids.add(pkey);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
            if (!ids.isEmpty()) {
                List<? extends VO> dbDatas = transDiver.findByIds(new ArrayList<String>(ids), tempTrans.target());
                for (VO vo : dbDatas) {
                    threadLocalCache.get().put(tempTrans.target().getName() + "_" + vo.getPkey(),
                            createTempTransCacheMap(vo, tempTrans));
                }
            }
        }
        objList.forEach(obj -> {
            this.transOne(obj, toTransList);
        });
        threadLocalCache.set(null);
    }

    /**
     * 获取用于翻译的缓存
     *
     * @param tempTrans tempTrans
     * @param pkey      主键
     * @return 缓存
     */
    private Map<String, String> getTempTransCacheMap(Trans tempTrans, String pkey) {
        if (this.threadLocalCache.get() == null) {
            if (CheckUtils.isNullOrEmpty(pkey)) {
                return new HashMap<>();
            }
            VO vo = this.transDiver.findById(pkey, tempTrans.target());
            return createTempTransCacheMap(vo, tempTrans);
        }
        return this.threadLocalCache.get().get(tempTrans.target().getName() + "_" + pkey);
    }

    /**
     * 创建一个临时缓存map
     *
     * @param po    po
     * @param trans 配置
     * @return
     */
    private Map<String, String> createTempTransCacheMap(Object po, Trans trans) {
        String fielVal = null;
        Map<String, String> tempCacheTransMap = new LinkedHashMap<>();
        if (po == null) {
            return tempCacheTransMap;
        }
        for (String field : trans.fields()) {
            fielVal = ConverterUtils.toString(ReflectUtils.getValue(po, field));
            tempCacheTransMap.put(field, fielVal);
        }
        return tempCacheTransMap;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        TransService.registerTransType(TransType.SIMPLE, this);
    }

    /**
     * 简单翻译数据驱动
     */
    public static interface SimpleTransDiver {
        /**
         * 根据ids获取集合
         *
         * @param ids ids
         * @return
         */
        List<? extends VO> findByIds(List<? extends Serializable> ids, Class<? extends VO> targetClass);

        /**
         * 根据id查询对象
         *
         * @param id id
         * @return
         */
        VO findById(Serializable id, Class<? extends VO> targetClass);
    }
}