package com.fhs;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.fhs","com.alicp.jetcache"})
@MapperScan(basePackages = {"com.fhs.*.dao", "com.fhs.*.*.dao"})
@EnableConfigurationProperties
@EnableRedisHttpSession
@EnableMethodCache(basePackages = "com.fhs")
@EnableFeignClients(basePackages = {"com.fhs"})
@ServletComponentScan(basePackages = {"com.fhs","org.apache.shiro"})
public class FhsFrameworkBaseBusinessApplication {

    public static void main(String[] args) {
        SpringApplication.run( FhsFrameworkBaseBusinessApplication.class, args);
    }
}
