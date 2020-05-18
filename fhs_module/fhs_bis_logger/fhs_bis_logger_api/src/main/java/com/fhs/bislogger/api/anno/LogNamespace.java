package com.fhs.bislogger.api.anno;

import com.fhs.bislogger.constant.LoggerConstant;

import java.lang.annotation.*;

/**
 * 用于注到控制器方法上,标记此方法需要记录日志
 * @Author: Wanglei
 * @date 2020-05-18 14:18:05
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ ElementType.TYPE})
public @interface LogNamespace {
    /**
     * 命名空间
     * @return
     */
    String namespace() ;

    /**
     * 描述
     * @return
     */
    String module();
}
