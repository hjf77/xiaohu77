package com.fhs.module.base.config;

import com.fhs.core.jsonfilter.aop.ParamArrayHandle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  fhsAop配置
 * @author user
 * @since 2019-05-18 11:30:46
 */
@Configuration
public class FHSAopConfig {

    /**
     * 前端传数组后端自动解析为 字符串的处理器 前端[1,2,3] 后端接收 1,2,3
     * @return
     */
    @Bean
    public ParamArrayHandle paramArrayHandle(){
        return new ParamArrayHandle();
    }

}
