package com.fhs.core.trans;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.ReflectUtils;
import com.fhs.common.utils.StringUtil;
import com.fhs.core.base.bean.SuperBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 翻译接口,将此接口实现类注册到transservice即可用
 *
 * @author wanglei
 */
public interface ITransTypeService {
    org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ITransTypeService.class);
    /**
     * 翻译一个字段
     *
     * @param obj         需要翻译的对象
     * @param toTransList 需要翻译的字段
     */
    void transOne(SuperBean<?> obj, List<Field> toTransList);

    /**
     * 翻译多个 字段
     *
     * @param objList     需要翻译的对象集合
     * @param toTransList 需要翻译的字段集合
     */
    void transMore(List<? extends SuperBean<?>> objList, List<Field> toTransList);

    /**
     * 设置ref
     *
     * @param trans 注解对象
     * @param vo    等待被翻译的数据
     * @param val   翻译的值
     */
    default void setRef(Trans trans, SuperBean vo, String val) {
        if (CheckUtils.isNotEmpty(trans.ref())) {
            ReflectUtils.setValue(vo, trans.ref(), val);
        }
        if (CheckUtils.isNotEmpty(trans.refs())) {
            Stream.of(trans.refs()).forEach(ref -> ReflectUtils.setValue(vo, ref, val));
        }
    }

    default void setRef(Trans trans, SuperBean vo, Map<String, String> valMap) {
        if (CheckUtils.isNotEmpty(trans.ref())) {
            setRef(trans.ref(), vo, valMap);
        }
        if (CheckUtils.isNotEmpty(trans.refs())) {
            Stream.of(trans.refs()).forEach(ref -> {
                setRef(ref, vo, valMap);
            });
        }
    }

    default void setRef(Trans trans, SuperBean vo, Map<String, String> valMap, SuperBean target) {
        if (CheckUtils.isNotEmpty(trans.ref())) {
            boolean isSetRef =false;
            if(target!=null){
                Field field= ReflectUtils.getDeclaredField(vo.getClass(),trans.ref());
                if(field.getType() == target.getClass()){
                    isSetRef = true;
                    ReflectUtils.setValue(vo, trans.ref(), target);
                }
            }
            //没有set才去set
            if (!isSetRef){
                setRef(trans.ref(), vo, valMap);
            }
        }
        if (CheckUtils.isNotEmpty(trans.refs())) {
            Stream.of(trans.refs()).forEach(ref -> {
                setRef(ref, vo, valMap);
            });
        }
    }

    default void setRef(String ref, SuperBean vo, Map<String, String> valMap) {
        String[] refSetting = ref.split("#");
        if (refSetting.length == 1) {
            if (valMap.size() > 0) {
                String key = valMap.keySet().iterator().next();
                ReflectUtils.setValue(vo, refSetting[0], valMap.get(key));
            }
        } else if (refSetting.length == 2) {
            ReflectUtils.setValue(vo, refSetting[0], valMap.get(refSetting[1]));
        }
    }

    /**
     * 支持多库
     *
     * @param callable
     * @param dataSourceName
     * @return
     */
    default List<? extends  SuperBean> findByIds(Callable<List<? extends  SuperBean>> callable, String dataSourceName) {
        try {
            return callable.call();
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return null;
    }

    /**
     * 支持多库
     *
     * @param callable
     * @param dataSourceName
     * @return
     */
    default SuperBean findById(Callable<SuperBean> callable, String dataSourceName) {
        try {
            return callable.call();
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return null;
    }
}