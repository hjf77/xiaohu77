package com.fhs.core.excel.register;

import com.fhs.common.spring.FhsSpringContextUtil;
import com.fhs.excel.service.TransRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 名称翻译注册中心
 */
@Component
public class TransRpcServiceRegister implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TransRpcServiceRegister.class);

    private Map<String, List<TransRpcService>> transRpsServiceMap = new HashMap<>();

    /**
     * springcloud 模式
     */
    @Value("${fhs.is-cloud-model:false}")
    private boolean isCloudModel;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        List<TransRpcService> services = null;
        if (isCloudModel) {
            services = FhsSpringContextUtil.getBeansByClass(TransRpcService.class);
        } else {
            String[] names = FhsSpringContextUtil.getApplicationContext().getBeanNamesForType(TransRpcService.class);
            services = new ArrayList<>();
            for (String name : names) {
                try {
                    Class clazz = Class.forName(name);
                    if (clazz.isInterface()) {
                        continue;
                    }
                    services.add((TransRpcService) FhsSpringContextUtil.getBeanByName(clazz));
                } catch (ClassNotFoundException e) {
                    services.add((TransRpcService) FhsSpringContextUtil.getBean(name));
                }
            }
        }
        services.forEach(service -> {
            String namespace = service.namespace();
            if (!transRpsServiceMap.containsKey(namespace)) {
                transRpsServiceMap.put(namespace, new ArrayList<>());
            }
            transRpsServiceMap.get(namespace).add(service);
        });
        LOG.info("TransRpcServiceRegister load service success");
    }

    /**
     * 根据namespace获取翻译服务
     *
     * @param namespace
     * @return
     */
    public TransRpcService getTransRpcService(String namespace) {
        if (transRpsServiceMap.containsKey(namespace)) {
            List<TransRpcService> rpcServices = transRpsServiceMap.get(namespace);
            if (rpcServices.size() == 1) {
                return rpcServices.get(0);
            }
            //有多个优先匹配
            for (TransRpcService rpcService : rpcServices) {
                if (!rpcService.isFeign()) {
                    return rpcService;
                }
            }
            return rpcServices.get(rpcServices.size() - 1);
        }
        return null;
    }
}
