package com.fhs.core.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 代表此类对应的service需要自动缓存
 * @author user
 * @date 2020-05-19 16:15:52
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE})
public @interface Cacheable {
    String value();
}
