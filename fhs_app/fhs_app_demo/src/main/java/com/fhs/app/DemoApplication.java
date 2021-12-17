package com.fhs.app;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.fhs.easycloud.anno.EnableEasyCloud;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 本项目作为一个微服务项目用来测试sa-token的sso是否可用
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.fhs"}, exclude = {
        SecurityAutoConfiguration.class
})
@EnableMethodCache(basePackages = "com.fhs")
@EnableEasyCloud(basePackages = "com.fhs")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run( DemoApplication.class, args);
    }
}
