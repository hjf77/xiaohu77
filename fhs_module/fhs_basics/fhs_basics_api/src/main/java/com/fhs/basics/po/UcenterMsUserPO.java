/*
 * 文 件 名:  AdminUser.java
 * 版    权:  sxpartner Technology International Ltd.
 * 描    述:  &lt;描述&gt;.
 * 修 改 人:  wanglei
 * 修改时间:  ${date}
 * 跟踪单号:  &lt;跟踪单号&gt;
 * 修改单号:  &lt;修改单号&gt;
 * 修改内容:  &lt;修改内容&gt;
 */
package com.fhs.basics.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.common.utils.CheckUtils;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.anno.TransDefaultSett;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.base.valid.group.Add;
import com.fhs.core.base.valid.group.Delete;
import com.fhs.core.base.valid.group.Update;
import com.fhs.basics.constant.BaseTransConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户管理(UcenterMsUser)实体类
 *
 * @author 朱俊
 * @version [版本号, 2015/08/13 11:31:39]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_ucenter_ms_user")
@EqualsAndHashCode(callSuper = true)
@TransDefaultSett(isUseCache = true,cacheSeconds = 100,isAccess = true)
@ApiModel(value = "UcenterMsUserDO", description = "UcenterMsUser参数")
public class UcenterMsUserPO extends BasePO<UcenterMsUserPO> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @NotNull(message = "userid不能为空", groups = {Update.class, Delete.class})
    @TableId(value = "user_id")
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 登录名
     */
    @NotNull(message = "登录名不能为空", groups = {Update.class, Add.class})
    @Length(message = "登录名不能最大为20", max = 20, min = 0)
    @TableField("user_login_name")
    @ApiModelProperty("登录名")
    private String userLoginName;

    /**
     * 用户的名字
     */
    @NotNull(message = "姓名不能为空", groups = {Update.class, Add.class})
    @Length(message = "姓名最大20", max = 20, min = 0)
    @TableField("user_name")
    @ApiModelProperty("姓名")
    private String userName;

    /**
     * 密码
     */
    @TableField("password")
    @JSONField(serialize = false)
    @ApiModelProperty("密码")
    private String password;

    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空", groups = {Update.class, Add.class})
    @TableField("mobile")
    @ApiModelProperty("手机号")
    private String mobile;

    /**
     * 集团编码-saas模式适用
     */
    @TableField("group_code")
    @ApiModelProperty("集团编码")
    private String groupCode;

    /**
     * 邮箱
     */
    @NotNull(message = "邮箱不能为空", groups = {Update.class, Add.class})
    @Length(message = "邮箱最大为255", max = 255, min = 0)
    @TableField("email")
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 是否禁用 0:启用 1:禁用
     */
    @NotNull(message = "{test.isDisable.null}", groups = {Update.class, Add.class})
    @Max(message = "{test.isDisable.max}", value = 2147483647, groups = {Add.class, Update.class})
    @Min(message = "{test.isDisable.min}", value = -2147483648, groups = {Add.class, Update.class})
    @Trans(type = TransType.DICTIONARY, key = "isEnable")
    @ApiModelProperty("是否禁用")
    private Integer isEnable;

    /**
     * 是否超管 0:否 1:是
     */
    @ApiModelProperty("是管理员")
    private Integer isAdmin;

    /**
     * 性别
     */
    @Max(message = "前端用户的sex字段大于int最大值", value = 2147483647, groups = {Add.class, Update.class})
    @Min(message = "前端用户的sex字段小于int小值", value = -2147483648, groups = {Add.class, Update.class})
    @TableField("sex")
    @Trans(type = TransType.DICTIONARY, key = "sex")
    @ApiModelProperty("性别")
    private Integer sex;

    /**
     * 组织机构ID
     */
    @Length(message = "{test.organizationId.length}", groups = {Add.class, Update.class}, max = 32, min = 0)
    @NotNull(message = "{test.organizationId.null}", groups = {Update.class, Add.class})
    @TableField("organization_id")
    @Trans(type = TransType.SIMPLE, target = UcenterMsOrganizationPO.class, ref = "orgName")
    @ApiModelProperty("组织机构编号")
    private String organizationId;

    /**
     * 头像
     */
    @TableField("header")
    @ApiModelProperty("头像")
    private String header;

    /**
     * 状态
     */

    @TableField(exist = false)
    @ApiModelProperty("状态")
    private String state;

    /**
     * 当前用户的角色
     */
    @TableField(exist = false)
    @ApiModelProperty("用户角色")
    private Long[] roleList;

    /**
     * 角色id逗号分隔
     */
    @TableField(exist = false)
    @ApiModelProperty("角色编号")
    private String roleIds;

    /**
     * 原始密码
     */
    @TableField(exist = false)
    @ApiModelProperty("原始密码")
    private String oldPassword;

    /**
     * 新密码
     */
    @TableField(exist = false)
    @ApiModelProperty("新密码")
    private String newPassword;

    /**
     * 菜单类型
     */
    @TableField(exist = false)
    @ApiModelProperty("菜单类型")
    private Integer menuType;

    /**
     * 角色集合
     */
    @TableField(exist = false)
    @ApiModelProperty("角色")
    private List<UcenterMsRolePO> roles;

    public String[] getRoleList() {
        if (CheckUtils.isNullOrEmpty(this.roleIds)) {
            return new String[]{};
        }
        return this.roleIds.split(",");
    }
}
