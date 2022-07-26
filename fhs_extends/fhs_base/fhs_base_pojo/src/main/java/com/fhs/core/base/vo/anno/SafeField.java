package com.fhs.core.base.vo.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 安全字段修饰注解，添加之后前端高级查询传了此参数后端也不支持
 * @Author: Wanglei
 * @Date: Created in 10:14 2022/07/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface SafeField {

}
