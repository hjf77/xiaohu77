package com.fhs.log;

import com.fhs.core.trans.vo.VO;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 日志上下文
 */
public class LoggerContext {

    private static Method proxyMehtod;

    /**
     * 添加历史数据
     *
     * @param vo        vo
     * @param namespace namespace
     */
    public static void addHistoryData(VO vo, String namespace) {
        if (proxyMehtod != null) {
            try {
                proxyMehtod.invoke(null, vo,namespace);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setProxyMehtod(Method method){
        proxyMehtod = method;
    }
}
