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
package com.fhs.basics.dox;

import com.fhs.common.constant.Constant;
import com.fhs.common.utils.EncryptUtils;
import com.fhs.core.base.dox.BaseDO;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.valid.group.Add;
import com.fhs.core.valid.group.Delete;
import com.fhs.core.valid.group.Update;
import com.fhs.basics.constant.BaseTransConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * &lt;描述一下Bean&gt;
 *
 * @author 朱俊
 * @version [版本号, 2015/08/13 11:37:58]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
@Builder
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "t_ucenter_ms_role")
public class UcenterMsRoleDO extends BaseDO<UcenterMsRoleDO> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    @Id
    @NotNull(message = "{test.roleId.null}", groups = {Update.class, Delete.class})
    @Max(message = "{test.roleId.max}", value = 2147483647, groups = {Add.class, Update.class})
    @Min(message = "{test.roleId.min}", value = -2147483648, groups = {Add.class, Update.class})
    @ApiModelProperty("角色id")
    private Integer roleId;



    /**
     * 角色名称
     */
    @NotNull(message = "{test.roleName.null}", groups = {Update.class, Add.class})
    @Length(message = "{test.roleName.length}", max = 20, min = 0)
    @ApiModelProperty("角色名")
    private String roleName;

    /**
     * 备注
     */
    @NotNull(message = "{test.remark.null}")
    @Length(message = "{test.remark.length}", max = 200, min = 0)
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 是否禁用 0:启用 1:禁用
     */
    @NotNull(message = "{test.isDisable.null}", groups = {Update.class, Add.class})
    @Max(message = "{test.isDisable.max}", value = 2147483647, groups = {Add.class, Update.class})
    @Min(message = "{test.isDisable.min}", value = -2147483648, groups = {Add.class, Update.class})
    @Trans(type = TransType.WORD_BOOK, key = "is_enable")
    @ApiModelProperty("是否禁用")
    private Integer isEnable;

    /**
     * 所属机构
     */
    @NotNull(message = "所属机构字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "所属机构字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @Column(name = "organization_id")
    @Trans(type = TransType.AUTO_TRANS,key = BaseTransConstant.ORG )
    @ApiModelProperty("组织机构id")
    private String organizationId;

    /**
     * 数据权限(资源类型，部门及小区)
     */
    @NotNull(message = "数据权限(资源类型，部门及小区)字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "数据权限(资源类型，部门及小区)字段的长度最大为500", groups = {Add.class, Update.class}, max = 500)
    @Column(name = "data_permissions")
    @ApiModelProperty("数据权限")
    private String dataPermissions;

    /**
     * 集团编码
     */
    @Column(name = "group_code")
    @ApiModelProperty("集团编码")
    private String groupCode;


    /**
     * 菜单按钮数据
     */
    @Transient
    private String[] methods;


    /**
     * 状态
     */
    @Transient
    private String state;






    public UcenterMsRoleDO() {
    }

    public UcenterMsRoleDO(Integer roleId, String roleName, String remark, Integer isEnable, String organizationId, String dataPermissions, String groupCode, String[] methods, String state) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.remark = remark;
        this.isEnable = isEnable;
        this.organizationId = organizationId;
        this.dataPermissions = dataPermissions;
        this.groupCode = groupCode;
        this.methods = methods;
        this.state = state;
    }
}
