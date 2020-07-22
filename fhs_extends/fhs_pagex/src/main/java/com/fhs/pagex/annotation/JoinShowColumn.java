package com.fhs.pagex.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记此注解的字段，在join此表的时候，会放到对方的transmap中
 * @author user
 * @date 2020-05-19 13:35:55
 */
@Target({ ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JoinShowColumn {

}

