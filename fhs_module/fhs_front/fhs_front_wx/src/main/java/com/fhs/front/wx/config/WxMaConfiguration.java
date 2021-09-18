package com.fhs.front.wx.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "fhs.wxmini.enable", havingValue = "true")
public class WxMaConfiguration {

    /**
     * 设置微信小程序的appid
     */
    @Value("${fhs.wxmini.appid}")
    private String appid;

    /**
     * 设置微信小程序的Secret
     */
    @Value("${fhs.wxmini.secret}")
    private String secret;

    /**
     * 设置微信小程序消息服务器配置的token
     */
    @Value("${fhs.wxmini.token:}")
    private String token;

    /**
     * 设置微信小程序消息服务器配置的EncodingAESKey
     */
    @Value("${fhs.wxmini.aesKey:}")
    private String aesKey;

    /**
     * 消息格式，XML或者JSON
     */
    @Value("${fhs.wxmini.msgDataFormat:json}")
    private String msgDataFormat;


    @Bean
    public WxMaService wxMaService() {
        WxMaInMemoryConfig config = new WxMaInMemoryConfig();
        // 使用上面的配置时，需要同时引入jedis-lock的依赖，否则会报类无法找到的异常
        config.setAppid(appid);
        config.setSecret(secret);
        config.setToken(token);
        config.setAesKey(aesKey);
        config.setMsgDataFormat(msgDataFormat);
        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(config);
        return service;
    }

}
