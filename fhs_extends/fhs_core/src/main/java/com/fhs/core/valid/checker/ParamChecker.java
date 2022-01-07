package com.fhs.core.valid.checker;

import com.fhs.common.utils.CheckUtils;
import com.fhs.core.exception.ParamException;

/**
 * 用来检查参数的
 *
 * @author user
 * @date 2020-05-19 10:04:26
 */
public class ParamChecker {

    /**
     * 如果为空返回paramexception
     *
     * @param obj 字符串
     * @param msg 消息
     */
    public static void isNotNullOrEmpty(Object obj, String msg) {
        if (CheckUtils.isNullOrEmpty(obj)) {
            throw new ParamException(msg);
        }
    }

    /**
     * 如果为空返回paramexception
     *
     * @param obj 对象
     * @param msg 消息
     */
    public static void isNotNull(Object obj, String msg) {
        if (obj == null) {
            throw new ParamException(msg);
        }
    }
}