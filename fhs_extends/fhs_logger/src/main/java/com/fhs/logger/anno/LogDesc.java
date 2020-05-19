package com.fhs.logger.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志描述
 *
 * @Filename: LogDesc.java
 * @Description:
 * @Version: 1.0
 * @Author: jackwong
 * @Email: jackwong@sxpartner.com
 * @History:<br>
 * 陕西小伙伴网络科技有限公司 Copyright (c) 2017 All Rights Reserved.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface LogDesc
{
    /**
     * 获取描述内容
     *
     * @return 描述内容
     */
    String value();

    /**
     * 日志类型
     * @return
     */
    int type();

    /**
     * 增加
     */
    int ADD = 0;

    /**
     * 删除
     */
    int DEL = 1;

    /**
     * 修改
     */
    int UPDATE = 2;

    /**
     * 查询
     */
    int SEE = 3;

    /**
     * 其他
     */
    int OTHER = 4;
}
