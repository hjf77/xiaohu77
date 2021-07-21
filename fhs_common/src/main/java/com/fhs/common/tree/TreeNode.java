package com.fhs.common.tree;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * <pre>
 * 描述：tree节点
 * 构建组：x7
 * 作者:wanglei
 * 邮箱:921888199@qq.com
 * 日期:2021-04-12 13:37:32
 * 版权：航天神舟智慧系统技术有限公司
 * </pre>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeNode<T> {
    /**
     * 节点id
     */
    @ApiModelProperty(value="id")
    private String id;
    /**
     * 节点标题/名称
     */
    @ApiModelProperty(value="节点标题/名称")
    private String name;
    /**
     * 父节点id
     */
    @ApiModelProperty(value="父节点id")
    private String parentId;
    /**
     * 节点数据
     */
    @ApiModelProperty(value="节点数据")
    private T data;

    private Integer isEditable = 1;

    private Integer isAddable = 1;

    private Integer isDelable = 1;

    /**
     * 是否加粗
     */
    @ApiModelProperty(value="是否加粗")
    private Integer isBold = 0;


    /**
     * 子节点
     */
    @ApiModelProperty(value="子节点")
    private List<TreeNode<T>> children = new ArrayList<>();
}
