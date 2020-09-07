package com.fhs.common.utils;

import com.mybatis.jpa.common.scanner.AnnotationTypeFilterBuilder;
import com.mybatis.jpa.common.scanner.SpringClassScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description: 包扫描工具
 * @Author: Wanglei
 * @Date: Created in 10:14 2019/10/15
 */
public class ScannerUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScannerUtils.class);

    /**
     * 类扫描器
     *
     * @param annotationClass 注解
     * @param packageNames    包
     * @return 符合条件的类
     */
    public static Set<Class<?>> scan(Class<? extends Annotation> annotationClass, String[] packageNames) {
        TypeFilter entityFilter = AnnotationTypeFilterBuilder.build(annotationClass);
        SpringClassScanner entityScanner = new SpringClassScanner.Builder().typeFilter(entityFilter).build();
        for (String packageName : packageNames) {
            entityScanner.getScanPackages().add(packageName);
        }
        Set<Class<?>> entitySet = null;
        try {
            entitySet = entityScanner.scan();
        } catch (ClassNotFoundException | IOException e) {
            LOGGER.error("包扫描错误", e);
            // log or throw runTimeExp
            throw new RuntimeException(e);
        }
        return entitySet;
    }


}

