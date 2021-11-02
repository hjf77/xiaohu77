package com.fhs.core.db.filter;

import com.fhs.common.constant.Constant;
import com.mybatis.jpa.context.DataPermissonContext;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 数据权限过滤器
 * 会给DataPermissonContext设置数据权限
 *
 * @author user
 * @date 2020-05-19 15:31:19
 */
@WebFilter(urlPatterns = {"/ms/*"}, filterName = "zDataPermissonFilter", asyncSupported = true)
public class ZDataPermissonFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //初始化

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //没用户的话直接放行
        /*if (UserContext.getSessionuser() == null || (!request.getServletPath().startsWith("/ms/"))) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }*/
        /*Map<String, String> dataPermissonMap = (Map<String, String>) request.getSession().getAttribute(Constant.SESSION_USER_DATA_PERMISSION);
        DataPermissonContext.setDataPermissonMap(dataPermissonMap);*/
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        //处理资源销毁相关逻辑
    }
}
