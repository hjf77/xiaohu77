package com.fhs.common.tree;

import java.io.Serializable;

/**
 * <pre>
 * 描述：标识tree
 * 构建组：x7
 * 作者:wanglei
 * 邮箱:921888199@qq.com
 * 日期:2021-04-12 13:37:32
 * 版权：航天神舟智慧系统技术有限公司
 * </pre>
 */
public interface Treeable {

    /**
     * 获取parentid
     *
     * @return
     */
    Serializable getTreeNodeParentId();

    /**
     * 获取名称
     *
     * @return
     */
    String getName();

    /**
     * 获取主键
     * @return
     */
    Serializable getTreeNodeId();

    /**
     * 获取格式化的名称
     * @return
     */
    default String getTreeNodeName(){
        return null;
    }
}
