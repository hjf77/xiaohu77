package com.fhs.basics.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单 + 权限按钮 Tree
 *
 * @author yutao
 * @date 2020 -04-28 09:47:46
 */
@Data
public class TreeMenuPermissionVO {
    /**
     * 菜单子节点
     */
    private String id;
    /**
     * 菜单父节点
     */
    private String parentId;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 儿子集合
     */
    private List<TreeMenuPermissionVO> children = new ArrayList<>();
}
