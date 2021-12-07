package com.fhs.excel.service;

@FunctionalInterface
public interface DoIniter<P> {
    /**
     * 初始化do的一些属性
     *
     * @param d
     */
    void init(P d);
}
