package com.fhs.module.base.swagger.controller;

import com.fhs.core.base.controller.BaseController;
import com.fhs.core.config.EConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * swagger转发控制器
 * @author user
 * @since 2019-05-18 11:39:05
 */
@Controller
@RequestMapping("ms/swagger")
public class SwaggerRedirectController extends BaseController {

    @Value("${fhs.swagger.enable:false}")
    private boolean isEnable;

    @RequestMapping("302")
    public void redirect() throws IOException {
        if(isEnable){
            super.getResponse().sendRedirect(EConfig.getPathPropertiesValue("basePath") + "/doc.html");
            return;
        }
        super.outWrite("swagger未开启,请配置:fhs.swagger.enable,将其设置为true");
    }
}