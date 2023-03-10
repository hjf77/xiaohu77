package com.fhs.core.trans;

import com.fhs.core.base.bean.SuperBean;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 翻译服务
 * 根据类的需要翻译的type 调用对应的trans服务翻译一个或者多个bean
 *
 * @author wanglei
 */
@Service("transService")
public class TransService {
    /**
     * key type  val是对应type的service
     */
    private static Map<String, ITransTypeService> transTypeServiceMap = new HashMap<String, ITransTypeService>();

    /**
     * 注册一个trans服务
     *
     * @param type             类型
     * @param transTypeService 对应的trans接口实现
     */
    public static void registerTransType(String type, ITransTypeService transTypeService) {
        transTypeServiceMap.put(type, transTypeService);
    }

    /**
     * 翻译一个字段
     *
     * @param obj 需要翻译的对象
     */
    public void transOne(SuperBean<?> obj) {
        if (obj == null) {
            return;
        }
        ClassInfo info = ClassManager.getClassInfoByName(obj.getClass());
        String[] transTypes = info.getTransTypes();
        if (transTypes == null) {
            return;
        }
        List<Field> transFieldList = null;
        for (String type : transTypes) {
            transFieldList = info.getTransField(type);
            if (transFieldList == null || transFieldList.size() == 0) {
                continue;
            }
            transTypeServiceMap.get(type).transOne(obj, transFieldList);
        }
    }

    /**
     * 翻译多个 字段
     *
     * @param objList 需要翻译的对象集合
     * @param objList 需要翻译的字段集合
     */
    public void transMore(List<? extends SuperBean<?>> objList) {
        if (objList == null || objList.size() == 0) {
            return;
        }
        Object object = objList.get(0);
        ClassInfo info = ClassManager.getClassInfoByName(object.getClass());
        String[] transTypes = info.getTransTypes();
        if (transTypes == null) {
            return;
        }
        List<Field> transFieldList = null;
        for (String type : transTypes) {
            transFieldList = info.getTransField(type);
            if (transFieldList == null || transFieldList.size() == 0) {
                continue;
            }
            transTypeServiceMap.get(type).transMore(objList, transFieldList);
        }
    }

}
