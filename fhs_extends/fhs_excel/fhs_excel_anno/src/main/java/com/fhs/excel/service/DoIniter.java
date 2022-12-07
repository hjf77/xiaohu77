package com.fhs.excel.service;

@FunctionalInterface
public interface DoIniter<V> {
    /**
     * 初始化do的一些属性
     *
     * @param d
     */
    void init(V v);
}
