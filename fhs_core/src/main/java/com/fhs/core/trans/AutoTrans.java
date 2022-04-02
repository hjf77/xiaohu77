package com.fhs.core.trans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 此注解只可标记到service上，意思为本服务所标记的PO可自动翻译
 * @Author: Wanglei
 * @Date: Created in 10:14 2019/10/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface AutoTrans {
    /**
     * 命名空间
     *
     * @return
     */
    String namespace();

    /**
     * 字段集合
     *
     * @return
     */
    String[] fields();

    /**
     * 是否使用缓存翻译
     *
     * @return 默认为true 如果是false的话
     */
    boolean useCache() default true;
}
