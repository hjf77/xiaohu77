package com.fhs.basics.api.trans;

import com.fhs.basics.po.UcenterMsUserPO;
import com.fhs.cache.service.TransCacheManager;
import com.fhs.trans.service.impl.SimpleTransService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户翻译加缓存
 */
@Component
public class BasicRpcTransConfig implements InitializingBean {

    @Autowired
    private TransCacheManager transCacheManager;

    @Override
    public void afterPropertiesSet() throws Exception {
        transCacheManager.setRpcTransCache(UcenterMsUserPO.class.getName(), SimpleTransService.TransCacheSett
                .builder()
                .maxCache(1000)
                .cacheSeconds(1000)
                .isAccess(true)
                .build());
    }
}
