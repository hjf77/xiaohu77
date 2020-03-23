package com.fhs.common.spring;

import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.lang.annotation.Annotation;

/**
 * 构造class过滤器
 */
public class AnnotationTypeFilterBuilder {
    public static TypeFilter build(Class<? extends Annotation> annotionType) {
        return new AnnotationTypeFilter(annotionType, false);
    }
}