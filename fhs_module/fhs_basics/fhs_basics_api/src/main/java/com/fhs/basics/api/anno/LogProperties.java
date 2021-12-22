package com.fhs.basics.api.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于注到控制器方法上,标记此方法需要记录日志
 *
 * @Author: Wanglei
 * @date 2020-05-18 14:18:23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface LogProperties {
    /**
     * 描述
     *
     * @return
     */
    String desc() default "";
}
