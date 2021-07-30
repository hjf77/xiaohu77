package com.fhs.core.base.anno;

import java.lang.annotation.*;

/**
 * 不可重复的表描述
 * wanglei
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotRepeatDesc {
    String value() default "名称不可重复";
}
