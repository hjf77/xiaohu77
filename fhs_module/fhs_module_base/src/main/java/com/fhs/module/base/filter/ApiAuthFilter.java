package com.fhs.module.base.filter;

import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.JsonUtil;
import com.fhs.core.config.EConfig;
import com.fhs.core.result.PubResult;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * api鉴权
 *
 * @author wanglei
 * @since 2019-05-18 11:34:35
 */
@WebFilter(urlPatterns = {"/api/*","/easyTrans/proxy/*","/easyCloud/proxy/*"}, filterName = "apiAuthFilter", asyncSupported = true)
public class ApiAuthFilter implements Filter {

    String apiToken = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (apiToken == null) {
            apiToken = EConfig.getOtherConfigPropertiesValue("apiToken");
        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String reqApiToken = request.getHeader("apiToken");
        if (CheckUtils.isNullOrEmpty(reqApiToken) || !reqApiToken.equals(apiToken)) {
            JsonUtil.outJson((HttpServletResponse) servletResponse, PubResult.NO_PERMISSION.asResult().asJson());
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
