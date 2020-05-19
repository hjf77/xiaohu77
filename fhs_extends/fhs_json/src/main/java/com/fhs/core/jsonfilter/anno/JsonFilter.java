package com.fhs.core.jsonfilter.anno;


import java.lang.annotation.*;

/**
 * json过滤器
 * @author user
 * @date 2020-05-19 14:35:33
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonFilter {
    ObjFilter[] value();
}