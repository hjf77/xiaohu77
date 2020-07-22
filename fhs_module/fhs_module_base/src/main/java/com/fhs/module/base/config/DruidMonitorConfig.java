package com.fhs.module.base.config;


import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库以及请求监控
 * @author user
 * @since 2019-05-18 11:30:35
 */
@Configuration("druidMonitorConfig")
@ConditionalOnProperty(prefix = "fhs.druid-monitor", name = "enable", havingValue = "true", matchIfMissing = false)
public class DruidMonitorConfig {

    /**
     * 慢sql标准3秒
     */
    @Value("${fhs.druid-monitor.slowSqlMillis:3}")
    private Integer slowSqlMillis;

    /**
     * 配置 Druid Monitor ==========================================
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String, String> parameters = new HashMap<>(5);
        parameters.put(StatViewServlet.PARAM_NAME_RESET_ENABLE, "false");
        bean.setInitParameters(parameters);
        return bean;
    }

    /**
     * SQL 监控 ==================================
     *
     * @return
     */
    @Bean
    public StatFilter statFilter() {
        StatFilter bean = new StatFilter();
        bean.setLogSlowSql(true);
        bean.setSlowSqlMillis(slowSqlMillis);
        bean.setMergeSql(true);
        return bean;
    }

    /**
     * URI 监控 =====================================
     *
     * @param webStatFilter
     * @return
     */
    @Bean
    public FilterRegistrationBean FilterRegistrationBean(WebStatFilter webStatFilter) {
        FilterRegistrationBean bean = new FilterRegistrationBean(webStatFilter);
        bean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return bean;
    }

    @Bean
    public WebStatFilter WebStatFilter() {
        WebStatFilter bean = new WebStatFilter();
        bean.setSessionStatEnable(true);
        return bean;
    }

    /**
     * 配置Spring拦截器 ====================================
     *
     * @return
     */
    @Bean
    public DruidStatInterceptor druidStatInterceptor() {
        DruidStatInterceptor bean = new DruidStatInterceptor();
        return bean;
    }


    /**
     * 日志记录 ==============================================
     * <p>
     * statement-create-after-log-enabled: false
     * statement-close-after-log-enabled: false
     * result-set-open-after-log-enabled: false
     * result-set-close-after-log-enabled: false
     *
     * @return
     */
    @Bean
    public Slf4jLogFilter log4jFilter() {
        Slf4jLogFilter bean = new Slf4jLogFilter();
        bean.setDataSourceLogEnabled(true);
        bean.setStatementExecutableSqlLogEnable(true);
        bean.setDataSourceLoggerName("druid");
        return bean;
    }
}
