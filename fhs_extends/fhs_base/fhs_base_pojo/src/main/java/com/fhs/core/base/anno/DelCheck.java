package com.fhs.core.base.anno;

import java.lang.annotation.*;


/**
 * 关联删除校验  场景：删除班级的时候必须保证班级里没学生
 * wanglei
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DelCheck {
    /**
     * 配置要check哪些表
     * @return
     */
    DelCheckTable[] checkTables();
}
