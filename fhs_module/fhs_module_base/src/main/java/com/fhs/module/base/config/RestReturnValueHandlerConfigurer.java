package com.fhs.module.base.config;

import com.fhs.core.result.HttpResult;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class RestReturnValueHandlerConfigurer implements InitializingBean {
    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;
    @Autowired
    private ApplicationContext applicationContext;

    public RestReturnValueHandlerConfigurer() {
    }

    public void afterPropertiesSet() {
        List<HandlerMethodReturnValueHandler> list = this.handlerAdapter.getReturnValueHandlers();
        if (!CollectionUtils.isEmpty(list)) {
            List<HandlerMethodReturnValueHandler> newList = (List)list.stream().map((x) -> {
                return (HandlerMethodReturnValueHandler)(x instanceof RequestResponseBodyMethodProcessor ? new HandlerMethodReturnValueHandlerProxy(x, this.applicationContext) : x);
            }).collect(Collectors.toList());
            this.handlerAdapter.setReturnValueHandlers(newList);
        }
    }
}

class HandlerMethodReturnValueHandlerProxy implements HandlerMethodReturnValueHandler {
    private static final Logger log = LoggerFactory.getLogger(HandlerMethodReturnValueHandlerProxy.class);
    private HandlerMethodReturnValueHandler proxyObject;
    private ApplicationContext applicationContext;
    private static final Set<Class> EXTRA_PRIMITIVE_TYPE = Sets.newHashSet(new Class[]{String.class, Integer.class, Long.class, Double.class, Boolean.class, Byte.class, Short.class, Character.class});

    public HandlerMethodReturnValueHandlerProxy(HandlerMethodReturnValueHandler proxyObject, ApplicationContext applicationContext) {
        this.proxyObject = proxyObject;
        this.applicationContext = applicationContext;
    }

    public boolean supportsReturnType(MethodParameter methodParameter) {
        return this.proxyObject.supportsReturnType(methodParameter);
    }

    public void handleReturnValue(Object returnValue, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        HttpServletRequest request = (HttpServletRequest)nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        if (request.getRequestURI().startsWith("/actuator")) {
            this.proxyObject.handleReturnValue(returnValue, methodParameter, modelAndViewContainer, nativeWebRequest);
        } else {
            Object data = returnValue;
            String isVue =request.getParameter("isVue");
            if(!(data instanceof HttpResult) && "true".equals(isVue)){
                HttpResult result = HttpResult.success(data);
                this.proxyObject.handleReturnValue(result, methodParameter, modelAndViewContainer, nativeWebRequest);
                return;
            }

            this.proxyObject.handleReturnValue(data, methodParameter, modelAndViewContainer, nativeWebRequest);
        }
    }


}
