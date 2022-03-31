package com.fhs.module.base.filter;

import com.fhs.common.spring.FhsSpringContextUtil;
import com.fhs.core.config.EConfig;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 初始化模板引擎中的参数
 *
 * @author user
 * @since 2019-05-18 11:35:23
 */
@WebFilter(urlPatterns = {"/*"}, filterName = "templateConfigSettFilter", asyncSupported = true)
public class TemplateConfigSettFilter implements Filter {

    /**
     * 是否初始化过
     */
    private boolean isInit = false;

    /**
     * Default constructor.
     */
    public TemplateConfigSettFilter() {
    }


    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse rep, FilterChain filterChain)
            throws IOException, ServletException {
        if (!isInit) {
            BeetlGroupUtilConfiguration beetlGroupUtilConfiguration = FhsSpringContextUtil.getBeanByName(BeetlGroupUtilConfiguration.class);
            if (beetlGroupUtilConfiguration != null && beetlGroupUtilConfiguration.getGroupTemplate() != null) {
                Set<String> keys = EConfig.PATH.stringPropertyNames();
                Map<String, Object> shared = new HashMap<String, Object>();
                for (String key : keys) {
                    shared.put(key, EConfig.PATH.get(key));
                    req.getServletContext().setAttribute(key, EConfig.PATH.get(key));
                }
                keys = EConfig.OTHER_CONFIG.stringPropertyNames();
                for (String key : keys) {
                    shared.put(key, EConfig.OTHER_CONFIG.get(key));
                    req.getServletContext().setAttribute(key, EConfig.OTHER_CONFIG.get(key));
                }
                // beetl共享变量，所有的模板都能访问到
                beetlGroupUtilConfiguration.getGroupTemplate().setSharedVars(shared);
            }
            isInit = true;
        }
        filterChain.doFilter(req, rep);
    }


    @Override
    public void init(FilterConfig fConfig)
            throws ServletException {
    }
}
