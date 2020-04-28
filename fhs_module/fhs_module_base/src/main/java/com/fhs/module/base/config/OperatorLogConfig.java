package com.fhs.module.base.config;

import com.fhs.bislogger.api.aop.OperatorLogAop;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 操作日志配置
 */
@Configuration("operatorLogConfig")
@ConditionalOnProperty(prefix = "fhs.operator-log", name = "enable", havingValue = "true", matchIfMissing = false)
public class OperatorLogConfig {

    @Bean
    public OperatorLogAop OperatorLogAop(){        return new OperatorLogAop();
    }
}
