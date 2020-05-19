package com.fhs.core.config;

import java.util.Properties;

/**
 * 当配置文件改变的时候触发(阿波罗模式适用)
 * @author user
 * @date 2020-05-19 15:45:18
 */
@FunctionalInterface
public  interface  ConfigChangeListener{
     void refresh(Properties pathConfig, Properties otherConfig);
}