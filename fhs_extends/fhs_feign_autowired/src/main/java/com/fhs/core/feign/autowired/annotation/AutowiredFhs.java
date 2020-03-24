package com.fhs.core.feign.autowired.annotation;

import java.lang.annotation.*;

/**
 * @Filename: AutowiredFhs.java
 * @Description: fegin 代理出来的接口自动注入
 * @Version: 1.0
 * @Author: qixiaobo
 * @Email: qxb@sxpartner.com
 * @History:<br>
 * 陕西小伙伴网络科技有限公司
 * Copyright (c) 2017 All Rights Reserved.
 *
 */
@Inherited
@Documented
@Target(value={ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutowiredFhs
{
    /**
     * 获取值
     * @return
     */
    public String value() default "";
}

