package com.fhs.pub.config;

import com.fhs.pub.service.FileStorage;
import com.fhs.pub.service.impl.AliyunOSSFileStorage;
import com.fhs.pub.service.impl.DiskFileStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 文件服务配置类
 * @author user
 * @since 2019-05-18 11:21:18
 */
@Configuration
public class FileServiceConfig implements WebMvcConfigurer {


    /**
     * 储存类型默认是硬盘储存
     */
    @Value("${fhs.file.storageType:disk}")
    private String storageType;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new ByteArrayHttpMessageConverter());
    }


    /**
     * 初始化文件储存器
     * @return  根据条件初始化文件储存器
     */
    @Bean
    public FileStorage fileStorage(){
        //如果是阿里云则使用阿里云oss文件储存器
        if("oss".equals(storageType))
        {
            return new AliyunOSSFileStorage();
        }
        return new DiskFileStorage();
    }
}
