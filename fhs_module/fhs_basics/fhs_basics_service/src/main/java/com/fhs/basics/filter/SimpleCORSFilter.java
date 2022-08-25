package com.fhs.basics.filter;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Objects;

/**
 * 全局路径filter
 *
 * @author jianbo.qin
 */
@WebFilter(urlPatterns = "/*", filterName = "SimpleCORSFilter")
public class SimpleCORSFilter implements Filter {

    /**
     * Default constructor.
     */
    public SimpleCORSFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

//    @Override
//    public void doFilter(ServletRequest req, ServletResponse rep, FilterChain filterChain)
//            throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) req;
//        HttpServletResponse response = (HttpServletResponse) rep;
//
//        // 设置允许跨域访问的域，*表示支持所有的来源
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        // 设置允许跨域访问的方法
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Max-Age", "36000");
//        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
//        response.setHeader("Access-Control-Allow-Headers", "X_Requested_With,jackToken");
//
//        filterChain.doFilter(req, rep);
//    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");

        /**cors modified start**/
        StringBuilder headers = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaders("Access-Control-Request-Headers");
        if(Objects.nonNull(headerNames)) {
            while (headerNames.hasMoreElements()) {
                headers.append(headerNames.nextElement()).append(",");
            }
        }
        response.setHeader("Access-Control-Allow-Headers", headers.toString());
        /**cors modified end**/


        if (HttpMethod.OPTIONS.toString().equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
        } else {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(FilterConfig fConfig)
            throws ServletException {

    }
}
