package com.fhs.module.base.config;

import com.fhs.basics.api.aop.OperatorLogAop;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 操作日志配置
 *
 * @author user
 * @since 2019-05-18 11:33:36
 */
@Configuration("operatorLogConfig")
@ConditionalOnProperty(prefix = "fhs.operator-log", name = "enable", havingValue = "true", matchIfMissing = false)
public class OperatorLogConfig {

    @Bean("fhsOperatorLogAop")
    public OperatorLogAop OperatorLogAop() {
        return new OperatorLogAop();
    }
}
