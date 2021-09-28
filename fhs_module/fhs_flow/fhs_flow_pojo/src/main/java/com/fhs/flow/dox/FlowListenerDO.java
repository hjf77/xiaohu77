package com.fhs.flow.dox;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.base.dox.BaseDO;
import com.fhs.core.valid.group.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 监听器(FlowListener)实体类
 *
 * @author jackwong
 * @since 2019-11-11 14:28:44
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_flow_listener")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FlowListenerDO", description = "FlowListener参数")
public class FlowListenerDO extends BaseDO<FlowListenerDO> {
    private static final long serialVersionUID = -34427919221391088L;
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 标题
     */
    @NotEmpty
    @NotNull(message = "标题字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "标题字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("title")
    @ApiModelProperty("标题")
    private String title;

    /**
     * url
     */
    @NotEmpty
    @NotNull(message = "url字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "url字段的长度最大为255", groups = {Add.class, Update.class}, max = 255)
    @TableField("uri")
    @ApiModelProperty("url")
    private String uri;

    /**
     * 备注
     */
    @Length(message = "备注字段的长度最大为255", groups = {Add.class, Update.class}, max = 255)
    @TableField("remark")
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 类型0 全局 2 连线 3 节点
     */
    @NotNull(message = "类型0 全局 2 连线 3 节点字段不可为null", groups = {Update.class, Delete.class})
    @TableField("type")
    @ApiModelProperty("类型")
    private Integer type;

    /**
     * 在哪个服务器上
     */
    @NotNull(message = "在哪个服务器上字段不可为null", groups = {Update.class, Delete.class})
    @TableField("server")
    @ApiModelProperty("在哪个服务器上")
    private Integer server;


}
