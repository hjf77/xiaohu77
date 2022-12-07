package com.fhs.common.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ClassUtils {
    public static void fatherToChild(Object father, Object child)
            throws Exception {
        if (!(child.getClass().getSuperclass() == father.getClass())) {
            throw new Exception("child不是father的子类");
        }
        Class<?> fatherClass = father.getClass();
        Field ff[] = fatherClass.getDeclaredFields();
        for (int i = 0; i < ff.length; i++) {
            Field f = ff[i];// 取出每一个属性，如deleteDate
            f.setAccessible(true); // 该方法表示取消java语言访问检查

            if (ConverterUtils.toString(f).contains("static")) {
                continue;
            }

            Method m = fatherClass.getMethod("get" + upperHeadChar(f.getName()));// 方法getDeleteDate
            Object obj = m.invoke(father);// 取出属性值
            f.set(child, obj);
        }
    }

    /**
     * 首字母大写，in:deleteDate，out:DeleteDate
     */
    public static String upperHeadChar(String in) {
        String head = in.substring(0, 1);
        String out = head.toUpperCase() + in.substring(1, in.length());
        return out;
    }

    /**
     * bolwable 分页接口参数转换
     *
     * @param queryParamMap 参数键值对
     * @param clz           转换目标类型
     * @param <T>
     * @return
     */
    public static <T> T convert2Clz(Map<String, Object> queryParamMap, Class<T> clz) {
        T t = JSONObject.parseObject(JSONObject.toJSONString(queryParamMap), clz);
        // 分页参数
        if (null != queryParamMap.get("pageNumber") && null != queryParamMap.get("pageSize")) {
            Method[] methods = clz.getSuperclass().getMethods();
            List<String> superMethodsNames = Arrays.stream(methods).map(Method::getName).collect(Collectors.toList());
            if (superMethodsNames.contains("setPageNo")) {
                try {
                    clz.getSuperclass().getMethod("setPageNo", Integer.class).invoke(t, Integer.valueOf(String.valueOf(queryParamMap.get("pageNumber"))));
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    log.error("ClassUtils.convert2Clz::" + e.getMessage());
                    throw new RuntimeException("请求参数错误！");
                }
            }
        }
        return t;
    }

}
