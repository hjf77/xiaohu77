package com.fhs.common.tree;

public interface TreeableByPipeline {
    /**
     * 获取parentid
     *
     * @return
     */
    String getParentId();

    /**
     * 获取名称
     *
     * @return
     */
    String getName();

    /**
     * 获取主键
     *
     * @return
     */
    String getId();

    String getNumber();
}
