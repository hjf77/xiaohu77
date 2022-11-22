package com.fhs.app;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClientAutoConfiguration;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication(scanBasePackages = {"com.fhs", "com.alicp.jetcache"}, exclude = {
       // org.activiti.spring.boot.SecurityAutoConfiguration.class,
        SecurityAutoConfiguration.class,
        // 下面这2个没啥用，而且会拖慢启动速度
        SimpleDiscoveryClientAutoConfiguration.class,
        CompositeDiscoveryClientAutoConfiguration.class
})
@MapperScan(basePackages = {"com.fhs.*.mapper", "com.fhs.*.*.mapper", "com.fhs.flow.dal.mysql.definition"
        , "com.fhs.flow.dal.mysql.oa","com.fhs.flow.dal.mysql.task"})
@EnableConfigurationProperties
@EnableRedisHttpSession
@EnableMethodCache(basePackages = "com.fhs")
@EnableFeignClients(basePackages = {"com.fhs"})
@ServletComponentScan(basePackages = {"com.fhs"})
public class SingleApplication {

    public static void main(String[] args) {
        try{
            // 建议仅在开发或者排除时开启此配置
           /* new SpringApplicationBuilder(SingleApplication.class)
                    .applicationStartup(new BufferingApplicationStartup(20480))
                    .run(args);*/
            SpringApplication.run(SingleApplication.class, args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
