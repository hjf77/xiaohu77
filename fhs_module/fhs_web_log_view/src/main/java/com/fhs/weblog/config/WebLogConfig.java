package com.fhs.weblog.config;

import ch.qos.logback.classic.LoggerContext;
import com.fhs.weblog.filter.ProcessLogFilter;
import com.fhs.weblog.handle.LoggerEventHandler;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.simp.config.TaskExecutorRegistration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurationSupport;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * websocket配置
 *
 * @author jackwang
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebLogConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .setAllowedOrigins("*")
                .addInterceptors()
                .withSockJS();
    }

    @Bean
    public LoggerEventHandler LoggerEventHandler() {
        return new LoggerEventHandler();
    }


}
