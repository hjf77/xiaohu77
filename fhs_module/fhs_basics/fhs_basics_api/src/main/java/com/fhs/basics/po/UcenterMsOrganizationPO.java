package com.fhs.basics.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.common.tree.Treeable;
import com.fhs.core.base.anno.NotRepeatDesc;
import com.fhs.core.base.anno.NotRepeatField;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.base.valid.group.Add;
import com.fhs.core.base.valid.group.Delete;
import com.fhs.core.base.valid.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author wanglei
 * @version [版本号, 2018-09-04]
 * @Description:机构管理表
 * @versio 1.0 陕西小伙伴网络科技有限公司 Copyright (c) 2018 All Rights Reserved.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_ucenter_ms_organization", autoResultMap = true)
@NotRepeatDesc("同一个父部门下的子部门名称不可重复")
@ApiModel(value = "UcenterMsOrganizationDO", description = "UcenterMsOrganization参数")
public class UcenterMsOrganizationPO extends BasePO<UcenterMsOrganizationPO> implements Treeable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @NotNull(message = "id字段不可为null ", groups = {Update.class, Delete.class})
    @ApiModelProperty(value = "主键id")
    private String id;

    /**
     * 机构名称
     */
    @NotNull(message = "机构名称字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "机构名称字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("name")
    @ApiModelProperty(value = "机构名称")
    @NotRepeatField
    private String name;

    /**
     * 机构简称
     */
    @TableField("alias_name")
    @ApiModelProperty(value = "机构简称")
    private String aliasName;

    /**
     * 父类编号
     */
    @NotNull(message = "父类编号字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "父类编号字段的长度最大为32", groups = {Add.class, Update.class}, max = 255)
    @TableField("parent_id")
    @ApiModelProperty(value = "父类编号")
    @NotRepeatField
    private String parentId;

    /**
     * 同级菜单排行第几
     */
    @TableField("ranking")
    @ApiModelProperty(value = "同级菜单排行第几")
    private Integer ranking;

    /**
     * 是否启用(0:启用 1:禁用)
     */
    @TableField("is_enable")
    @ApiModelProperty(value = "是否启用")
    @Trans(type = TransType.DICTIONARY, key = "isEnable")
    private Integer isEnable;

    /**
     * 集团编码
     */
    @TableField("group_code")
    @ApiModelProperty(value = "集团编码")
    private String groupCode;

    /**
     * 扩展字段
     */
    @TableField("ext_json")
    @ApiModelProperty(value = "扩展字段")
    private String extJson;


    @TableField("is_company")
    @ApiModelProperty(value = "是否是单位")
    private Integer isCompany;

    @TableField("company_id")
    @ApiModelProperty(value = "所属单位id")
    private String companyId;

    @Override
    public Serializable getTreeNodeParentId() {
        return this.parentId;
    }

    @Override
    public Serializable getTreeNodeId() {
        return this.id;
    }
}
