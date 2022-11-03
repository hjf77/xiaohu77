package com.fhs.excel.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 设置对应的列名
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ImportExcelTitle {
    String value() default "";
}

// @ImportExcelTitle("管道名称")
// private Long xxId;