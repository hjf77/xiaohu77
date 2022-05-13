package com.fhs.basics.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.base.valid.group.Add;
import com.fhs.core.base.valid.group.Delete;
import com.fhs.core.base.valid.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * 租户管理(UcenterMsTenant)实体类
 *
 * @author wanglei
 * @since 2019-05-15 14:21:04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_ucenter_ms_tenant")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UcenterMsTenantDO", description = "UcenterMsTenant参数")
public class UcenterMsTenantPO extends BasePO<UcenterMsTenantPO> {
    private static final long serialVersionUID = -45809111607636131L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键id")
    private String id;

    /**
     * 租户集团编码
     */
    @NotEmpty
    @NotNull(message = "租户集团编码字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "租户集团编码字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("group_code")
    @ApiModelProperty(value = "租户集团编码")
    private String groupCode;

    /**
     * 租户名称
     */
    @NotEmpty
    @NotNull(message = "租户名称字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "租户名称字段的长度最大为100", groups = {Add.class, Update.class}, max = 100)
    @TableField("tenant_name")
    @ApiModelProperty(value = "租户名称")
    private String tenantName;

    /**
     * 联系人
     */
    @Length(message = "联系人字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("contacts")
    @ApiModelProperty(value = "联系人")
    private String contacts;

    /**
     * 电话
     */
    @Length(message = "电话字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("mobile")
    @ApiModelProperty(value = "电话")
    private String mobile;

    /**
     * logo
     */
    @TableField("logo")
    @ApiModelProperty(value = "图片")
    private String logo;

    /**
     * 备注
     */
    @TableField("remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 邮箱
     */
    @TableField("email")
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 子系统ids
     */
    @TableField("system_ids")
    @ApiModelProperty(value = "子系统ids")
    private String systemIds;

}
