package com.fhs.core.excel.register;

import com.fhs.common.spring.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 名称翻译注册中心
 *
 */
@Component
public class TransRpcServiceRegister implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TransRpcServiceRegister.class);

    private Map<String, TransRpcService> transRpsServiceMap = new HashMap<>();

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        List<TransRpcService> services = SpringContextUtil.getBeansByClass(TransRpcService.class);
        services.forEach(service -> {
            String namespace = service.namespace();
            if (!transRpsServiceMap.containsKey(namespace)) {
                transRpsServiceMap.put(namespace, service);
            }
        });
        LOG.info("TransRpcServiceRegister load service success");
    }

    public TransRpcService getTransRpcService(String namespace){
        if (transRpsServiceMap.containsKey(namespace)) {
            return transRpsServiceMap.get(namespace);
        }
        return null;
    }
}
