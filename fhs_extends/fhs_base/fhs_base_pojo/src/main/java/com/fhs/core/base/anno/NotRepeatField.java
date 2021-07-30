package com.fhs.core.base.anno;

import java.lang.annotation.*;


/**
 * 不可重复的字段
 * wanglei
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotRepeatField {
}
