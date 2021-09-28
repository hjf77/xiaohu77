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
 * 流程列表-xml(FlowJbpmXml)实体类
 *
 * @author jackwong
 * @since 2019-11-11 14:29:04
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_flow_jbpm_xml")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FlowJbpmXmlDO", description = "FlowJbpmXml参数")
public class FlowJbpmXmlDO extends BaseDO<FlowJbpmXmlDO> {
    private static final long serialVersionUID = -26479945401118903L;
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 流程名称
     */
    @NotEmpty
    @NotNull(message = "流程名称字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "流程名称字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("name")
    @ApiModelProperty("流程名称")
    private String name;

    /**
     * 流程key
     */
    @NotEmpty
    @NotNull(message = "流程key字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "流程key字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("process_key")
    @ApiModelProperty("流程key")
    private String processKey;

    /**
     * 表单在哪个服务器上
     */
    @NotNull(message = "表单在哪个服务器上字段不可为null", groups = {Update.class, Delete.class})
    @TableField("server")
    @ApiModelProperty("表单在哪个服务器上")
    private String server;

    /**
     * 表单是否是pagex实现
     */
    @NotNull(message = "表单是否是pagex实现字段不可为null", groups = {Update.class, Delete.class})
    @TableField("is_pagex")
    @ApiModelProperty("表单是否时pagex实现")
    private Integer isPagex;

    /**
     * 如果不是pagex实现的话表单url是多少
     */
    @Length(message = "如果不是pagex实现的话表单url是多少字段的长度最大为255", groups = {Add.class, Update.class}, max = 255)
    @TableField("uri")
    @ApiModelProperty("如果不是pagex实现的话表单url是多少")
    private String uri;

    /**
     * 如果是pagex的话namespace是多少
     */
    @Length(message = "如果是pagex的话namespace是多少字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("namespace")
    @ApiModelProperty("如果是pagex的话namespace是多少")
    private String namespace;

    /**
     * 0草稿 1已发布 2 已禁用
     */
    @NotNull(message = "0草稿 1已发布 2 已禁用字段不可为null", groups = {Update.class, Delete.class})
    @TableField("status")
    @ApiModelProperty("状态")
    private Integer status;

    /**
     * xml
     */
    @TableField("xml")
    @ApiModelProperty("xml")
    private String xml;

    /**
     * xml图片
     */
    @TableField("img")
    @ApiModelProperty("xml图片")
    private String img;

    /**
     * 版本
     */
    @TableField("version")
    @ApiModelProperty("版本")
    private Integer version;

}
