package com.fhs.core.config;

import com.fhs.common.utils.JsonUtils;
import com.fhs.common.utils.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;


/**
 * 配置文件工具，自定义配置文件使用此工具类读取
 *
 * @author jackwong
 * @version [版本号, 2015年8月3日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Component("eConfig")
public class EConfig implements InitializingBean{

    private static final Logger LOG = Logger.getLogger(EConfig.class);

    /**
     * 不允许外部直接访问静态常量
     */
    public static final Properties PATH = new Properties();

    /**
     * 零散的配置信息
     */
    public static final Properties OTHER_CONFIG = new Properties();

    @Value("#{${fhs.path}}")
    private Map<String,String> pathMap;

    @Value("#{${fhs.other}}")
    private Map<String,String> otherMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        initConfig();
    }

    /**
     * 初始化配置文件
     */
    private void initConfig() {
        new Thread() {
            @Override
            public void run() {
                PATH.putAll(pathMap);
                OTHER_CONFIG.putAll(otherMap);
                LOG.info("成功加载path properties:" + JsonUtils.map2json(PATH));
                LOG.info("成功加载other properties:" + JsonUtils.map2json(OTHER_CONFIG));
            }
        }.start();
    }

    /**
     * 获取properties value
     *
     * @param properties 参数为:java.util.Properties 对象
     * @param key        传入一个java.util.Properties 对象所包含的键
     * @return 对应的value 如果不包含此key返回null
     */
    private static String getValue(Properties properties, String key) {
        return properties.containsKey(key) ? properties.getProperty(key) : null;
    }

    /**
     * 获取路径配置
     *
     * @param key 路径类型
     * @return 路径
     */
    public static String getPathPropertiesValue(String key) {
        return getValue(PATH, key);
    }


    /**
     * 获取其他杂项配置信息
     *
     * @param key 配置的key
     * @return 配置的值
     */
    public static String getOtherConfigPropertiesValue(String key) {
        return getValue(OTHER_CONFIG, key);
    }

}

