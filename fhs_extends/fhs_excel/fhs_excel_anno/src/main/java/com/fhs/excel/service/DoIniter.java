package com.fhs.excel.service;

@FunctionalInterface
public interface DoIniter<D> {
    /**
     * 初始化do的一些属性
     * @param d
     */
    void init(D d);
}
