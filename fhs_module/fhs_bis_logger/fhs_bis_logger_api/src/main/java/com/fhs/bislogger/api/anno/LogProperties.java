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
@Target({ ElementType.FIELD})
public @interface LogProperties {
    /**
     * 描述
     * @return
     */
    String desc() default "";
}
