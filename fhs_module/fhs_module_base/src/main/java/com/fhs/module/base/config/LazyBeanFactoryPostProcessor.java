package com.fhs.module.base.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * 实现全局延迟加载
 */
@Slf4j
@Component
public class LazyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            if(!beanName.contains("Trans") && !beanName.contains("satoken") && !beanName.contains("fhs") ) {
                BeanDefinition definition = beanFactory.getBeanDefinition(beanName);
                definition.setLazyInit(true);
            }
        }
    }
}
