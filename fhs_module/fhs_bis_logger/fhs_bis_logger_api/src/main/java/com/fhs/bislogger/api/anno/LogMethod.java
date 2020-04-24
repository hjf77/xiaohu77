package com.fhs.bislogger.api.anno;

import com.fhs.bislogger.constant.LoggerConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于注到控制器方法上,标记此方法需要记录日志
 * @Author: Wanglei
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD})
public @interface LogMethod {
    /**
     * 类型
     * @return
     */
    int type() default LoggerConstant.METHOD_TYPE_VIEW;

    /**
     * 描述
     * @return
     */
    String desc() default "";

    /**
     * vo是第几个参数
     * @return
     */
    int voParamIndex() default LoggerConstant.INDEX_NOT;

    /**
     * requestbody参数索引
     * @return
     */
    int reqBodyParamIndex() default LoggerConstant.INDEX_NOT;
}
