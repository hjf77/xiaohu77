package com.fhs.app;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.fhs", "com.alicp.jetcache"}, exclude = {
        SecurityAutoConfiguration.class
})
@MapperScan(basePackages = {"com.fhs.*.mapper", "com.fhs.*.*.mapper"})
@EnableConfigurationProperties
@EnableMethodCache(basePackages = "com.fhs")
@ServletComponentScan(basePackages = {"com.fhs"})
public class BasicsApplication {

    public static void main(String[] args) {
        SpringApplication.run( BasicsApplication.class, args);
    }
}
