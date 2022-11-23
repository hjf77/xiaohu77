package com.fhs.module.base.config;

import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * sa token配置用于权限验证
 */
@Configuration()
public class SaTokenConfig  implements WebMvcConfigurer {

    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册Sa-Token的路由拦截器
//        registry.addInterceptor(new SaRouteInterceptor((req, res, handler)->{
//            // 根据路由划分模块，不同模块不同鉴权
//            SaRouter.match("/ms/**", r -> StpUtil.checkLogin());
//        })).addPathPatterns("/ms/**");
    }
}
