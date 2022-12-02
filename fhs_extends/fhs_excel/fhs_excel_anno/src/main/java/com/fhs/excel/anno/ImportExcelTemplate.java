package com.fhs.excel.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 设置Excel导入模板
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ImportExcelTemplate {

    /**
     * 导入的模板名称
     * @return
     */
    String template() default "";
}