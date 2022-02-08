package com.fhs.influxdb.annotation;


import com.fhs.influxdb.core.enums.Function;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Aggregate {
    /**
     * 字段名
     * @return
     */
    String value();

    /**
     * 字段使用的聚合函数
     * @return
     */
    Function tag();
}
