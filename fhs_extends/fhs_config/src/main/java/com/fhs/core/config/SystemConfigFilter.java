package com.fhs.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 如果请求 的是systemConfig.js 则读取配置文件给浏览器写
 *
 * @author jianbo.qin
 */
@Configuration
@WebFilter(urlPatterns = "/*", filterName = "systemConfigFilter")
@Component
public class SystemConfigFilter implements Filter {

    private static String fileName = "systemConfig.js";

    /**
     * systemconfig js内容
     */
    private static String jsContent;


    private boolean isInit;

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!isInit) {
            try {
                initJsContent();
                isInit = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (httpServletRequest.getRequestURI().contains(fileName)) {
            httpServletResponse.getWriter().println(jsContent);
            httpServletResponse.getWriter().flush();
            httpServletResponse.getWriter().close();
            return;
        }
        chain.doFilter(request, response);
    }

    @Override

    public void init(FilterConfig FilterConfig)
            throws ServletException {

    }

    public void initJsContent() throws Exception {
        final StringBuilder jsBuilder = new StringBuilder();
        jsBuilder.append("var basePath" + " = '" + EConfig.getPathPropertiesValue("basePath") + "';");
        jsBuilder.append("var fhs_static_url" + " = '" + EConfig.getPathPropertiesValue("fhs_static_url") + "';");
        jsBuilder.append("var fileService" + " = '" + EConfig.getPathPropertiesValue("fhs_file_url") + "';");
        jsBuilder.append("var fileDownUrl" + " = '${fhs_file_url}/downLoad/fileByName';");
        jsBuilder.append("var fileDelUrl" + " = '${fhs_file_url}/file/del.do';");
        jsBuilder.append("var fileListUrl" + " = '${fhs_file_url}/downLoad/listData';");
        jsBuilder.append("var fileUploadUrl" + " = '${fhs_file_url}/upload/file';");
        jsBuilder.append("var downForId" + " = '${fhs_file_url}/downLoad/file';");
        jsBuilder.append("var IMG" + " = 'image/gif,image/jpeg,image/jpg,image/png,image/svg';");
        jsBuilder.append("var IMG_REG" + " = '/\\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/';");
        jsContent = jsBuilder.toString();
        jsContent = jsContent.replace("${fhs_file_url}",EConfig.getPathPropertiesValue("fhs_file_url"));
    }
}
