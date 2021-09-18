package com.fhs.core.base.anno;


import com.fhs.core.base.service.BaseService;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 关联删除校验  场景：删除班级的时候必须保证班级里没学生
 * wanglei
 */
@Documented
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface DelCheckTable {
    /**
     * 指定mapper
     *
     * @return
     */
    Class<? extends BaseService> manager();

    /**
     * 指定字段名字(数据库字段名)
     *
     * @return
     */
    String fieldName();

    /**
     * 对这个表进行一个描述，如果有关联数据存在则提示这个信息
     *
     * @return
     */
    String message();
}
