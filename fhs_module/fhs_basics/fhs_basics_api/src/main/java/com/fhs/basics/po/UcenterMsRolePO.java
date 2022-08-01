/*
 * 文 件 名:  AdminRole.java
 * 版    权:  sxpartner Technology International Ltd.
 * 描    述:  &lt;描述&gt;.
 * 修 改 人:  wanglei
 * 修改时间:  ${date}
 * 跟踪单号:  &lt;跟踪单号&gt;
 * 修改单号:  &lt;修改单号&gt;
 * 修改内容:  &lt;修改内容&gt;
 */
package com.fhs.basics.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.base.valid.group.Add;
import com.fhs.core.base.valid.group.Delete;
import com.fhs.core.base.valid.group.Update;
import com.fhs.basics.constant.BaseTransConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 角色表(UcenterMsRole)实体类
 *
 * @author 朱俊
 * @version [版本号, 2015/08/13 11:37:58]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_ucenter_ms_role")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UcenterMsRoleDO", description = "UcenterMsRole参数")
public class UcenterMsRolePO extends BasePO<UcenterMsRolePO> {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    @TableId(value="role_id")
    @ApiModelProperty("角色id")
    private Integer roleId;

    /**
     * 角色名称
     */
    @TableField("role_name")
    @ApiModelProperty("角色名")
    @NotNull(message = "角色名称不能为空", groups = {Update.class, Add.class})
    @Length(message = "角色名称长度最大20", max = 20, min = 0)
    private String roleName;

    /**
     * 备注
     */
    @TableField("remark")
    @ApiModelProperty("备注")
    @Length(message = "备注长度最大200", max = 200, min = 0)
    private String remark;

    /**
     * 是否禁用 0:启用 1:禁用
     */
    @TableField("is_enable")
    @NotNull(message = "状态不能为空", groups = {Update.class, Add.class})
    @Trans(type = TransType.DICTIONARY, key = "is_enable")
    @ApiModelProperty("是否禁用")
    private Integer isEnable;

    /**
     * 所属机构
     */
    @NotNull(message = "所属机构字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "所属机构字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("organization_id")
    @Trans(type = TransType.AUTO_TRANS, key = BaseTransConstant.ORG)
    @ApiModelProperty("组织机构id")
    private String organizationId;

    /**
     * 数据权限(资源类型，部门及小区)
     */
    @TableField("data_permissions")
    @ApiModelProperty("数据权限")
    private String dataPermissions;

    /**
     * 集团编码
     */
    @TableField("group_code")
    @ApiModelProperty("集团编码")
    private String groupCode;

    /**
     * 菜单按钮数据
     */
    @Transient
    @TableField(exist = false)
    private String[] methods;


}
