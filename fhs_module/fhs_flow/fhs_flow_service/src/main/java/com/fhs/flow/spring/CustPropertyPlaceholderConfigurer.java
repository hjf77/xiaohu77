package com.fhs.flow.spring;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 系统配置属性
 * TODO 支持加密、支持不同环境配置
 * @author user
 * @date 2020-05-18 14:04:23
 */
public class CustPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements IProperty {

    /**
     * 属性key,value
     */
    private static Map<String, String> properties;

    public CustPropertyPlaceholderConfigurer() {
        properties = new HashMap<String, String>();
    }

    /**
     *
     */
    @Override
    protected void convertProperties(Properties props) {
        Set<String> keys = props.stringPropertyNames();
        for (String key : keys) {
            String value = props.getProperty(key);
            properties.put(key, value);
        }
        super.convertProperties(props);
    }

    /**
     * 根据建获取属性中的值。
     */
    @Override
    public String getValue(String key) {
        return properties.get(key);
    }

}
