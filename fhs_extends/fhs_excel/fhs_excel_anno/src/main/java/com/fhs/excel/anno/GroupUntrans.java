package com.fhs.excel.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * excel导入的时候，有的时候无法通过某一个字段来确定
 * 唯一一条数据进行反向翻译，可以通过此注解来进行多个字段联合翻译
 * 比如用户 ，是无法通过姓名来确定唯一的一条数据的
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface GroupUntrans {

    /**
     * 根据哪几个字段关联反向翻译
     *
     * @return
     */
    String[] value();


    /**
     * 如果关联不到提示的消息
     *
     * @return
     */
    String msg() default "";
}
