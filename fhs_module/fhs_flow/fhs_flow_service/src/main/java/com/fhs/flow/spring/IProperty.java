package com.fhs.flow.spring;

/**
 *
 * @author user
 * @date 2020-05-18 14:05:14
 */
public interface IProperty {
    /**
     * 根据key查询数据
     * @param key
     * @return
     */
    public String getValue(String key);
}
