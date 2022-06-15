package com.fhs.ucenter.tools;

import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.Logger;
import com.fhs.core.exception.ParamException;
import com.fhs.ucenter.bean.UcenterMpSett;
import com.fhs.ucenter.service.UcenterMpSettService;
import com.fhs.wx.FhsRedisTemplateWxRedisOps;
import me.chanjar.weixin.common.redis.RedisTemplateWxRedisOps;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpRedisConfigImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信工具 by jackwong
 */
@Component
public class WxTools implements InitializingBean {

    /**
     * 日志
     */
    private static final Logger LOG = Logger.getLogger(WxTools.class);

    @Autowired
    private UcenterMpSettService mpSettService;

    /**
     * key 编码 val 是对应的WxMpService
     */
    private WxMpService wxMpService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * key 编码 val 是对应的WxMpMessageRouter
     */
    private Map<String, WxMpMessageRouter> wxMpMessageRouterMap = new HashMap<>();

    /**
     * key 编码 val 是对应的WxMpService
     */
    private Map<String, WxMpConfigStorage> wxMpConfigStorageMap = new HashMap<>();

    @Value("${mp.app-id}")
    private String appId;

    @Value("${mp.app-secret}")
    private String appSecret;

    @Value("${mp.token}")
    private String token;

    @Value("${mp.aes-key}")
    private String aesKey;

    public WxMpService getWxMpService() {
        return wxMpService;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        wxMpService = new WxMpServiceImpl();
        RedisTemplateWxRedisOps redisTemplateWxRedisOps = new FhsRedisTemplateWxRedisOps(redisTemplate);
        WxMpRedisConfigImpl config = new WxMpRedisConfigImpl(redisTemplateWxRedisOps, "mp:");
        config.setAppId(appId); // 设置微信公众号的appid
        config.setSecret(appSecret); // 设置微信公众号的app corpSecret
        config.setToken(token); // 设置微信公众号的token
        config.setAesKey(aesKey); // 设置微信公众号的EncodingAESKey
        wxMpService.setWxMpConfigStorage(config);

    }
}
