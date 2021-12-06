/*
package com.fhs.app.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.fhs.common.utils.ConverterUtils;
import io.seata.common.holder.ObjectHolder;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class SeataConfig implements EnvironmentAware , ApplicationContextAware {

    */
/**
     * 坏境
     *//*

    private Environment env;

    */
/**
     * 设置当前环境
     * @param environment 环境
     *//*

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

    @Primary
    @Bean("dataSource")
    public DataSource dataSource() {
        DataSource dataSource  = getDataSource("spring.datasource");
        return new DataSourceProxy(dataSource);
    }


    */
/**
     * 设置数据源
     *
     * @param dataSourceName 库名称
     * @return 数据源
     *//*

    private DataSource getDataSource(String dataSourceName) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setName(env.getProperty(dataSourceName + ".name"));
        druidDataSource.setUrl(env.getProperty(dataSourceName + ".url"));
        druidDataSource.setUsername(env.getProperty(dataSourceName + ".username"));
        druidDataSource.setPassword(env.getProperty(dataSourceName + ".password"));
        druidDataSource.setDbType(env.getProperty(dataSourceName + ".type"));
        druidDataSource.setDriverClassName(env.getProperty(dataSourceName + ".driverClassName"));
        try {
            druidDataSource.setFilters(env.getProperty(dataSourceName + ".filters"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        druidDataSource.setMaxActive(ConverterUtils.toInteger(env.getProperty(dataSourceName + ".maxActive")));
        druidDataSource.setInitialSize(ConverterUtils.toInteger(env.getProperty(dataSourceName + ".initialSize")));
        druidDataSource.setMaxWait(ConverterUtils.toLong(env.getProperty(dataSourceName + ".maxWait")));
        druidDataSource.setMinIdle(ConverterUtils.toInteger(env.getProperty(dataSourceName + ".minIdle")));
        druidDataSource.setTimeBetweenEvictionRunsMillis(ConverterUtils.toLong(env.getProperty(dataSourceName + ".timeBetweenEvictionRunsMillis")));
        druidDataSource.setMinEvictableIdleTimeMillis(ConverterUtils.toLong(env.getProperty(dataSourceName + ".minEvictableIdleTimeMillis")));
        druidDataSource.setValidationQuery(env.getProperty(dataSourceName + ".validationQuery"));
        druidDataSource.setTestWhileIdle(ConverterUtils.toBoolean(env.getProperty(dataSourceName + ".testWhileIdle")));
        druidDataSource.setTestOnBorrow(ConverterUtils.toBoolean(env.getProperty(dataSourceName + ".testOnBorrow")));
        druidDataSource.setTestOnReturn(ConverterUtils.toBoolean(env.getProperty(dataSourceName + ".testOnReturn")));
        druidDataSource.setPoolPreparedStatements(ConverterUtils.toBoolean(env.getProperty(dataSourceName + ".poolPreparedStatements")));
        druidDataSource.setMaxOpenPreparedStatements(ConverterUtils.toInteger(env.getProperty(dataSourceName + ".maxOpenPreparedStatements")));
        return druidDataSource;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ObjectHolder.INSTANCE.setObject("ApplicationContext",applicationContext);
    }
}
*/
