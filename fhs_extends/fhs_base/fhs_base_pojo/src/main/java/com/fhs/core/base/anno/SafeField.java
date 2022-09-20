package com.fhs.core.base.anno;

import java.lang.annotation.*;

/**
 * 安全字段，标记此字段的po字段将不会参与前端advancequery 过滤
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SafeField {
}
