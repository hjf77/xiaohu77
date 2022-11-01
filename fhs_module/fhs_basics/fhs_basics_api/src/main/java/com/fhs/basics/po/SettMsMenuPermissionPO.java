/*
 * 文 件 名:  AdminMenuButton.java
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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 菜单权限(SettMsMenuPermission)实体类
 *
 * @author 朱俊
 * @version [版本号, 2015/08/13 11:37:45]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_sett_ms_menu_permission", autoResultMap = true)
@ApiModel(value = "SettMsMenuPermissionDO", description = "SettMsMenuPermission参数")
public class SettMsMenuPermissionPO extends BasePO<SettMsMenuPermissionPO> {

    private static final long serialVersionUID = 1L;

    /**
     * 按钮id
     */
    @TableId(value = "permission_id", type = IdType.ASSIGN_ID)
    @NotNull(message = "id不能为空", groups = {Update.class, Delete.class})
    @ApiModelProperty("按钮id")
    private String permissionId;

    /**
     * 按钮名称
     */
    @NotEmpty(message = "权限名称不能为空", groups = {Update.class, Add.class})
    @ApiModelProperty("按钮名称")
    @TableField("permission_name")
    private String permissionName;

    /**
     * 权限编码
     */
    @TableField("permission_code")
    @ApiModelProperty("权限编码")
    @NotEmpty(message = "权限编码不能为空", groups = {Update.class, Add.class})
    private String permissionCode;

    /**
     * 菜单id
     */
    @TableField("menu_id")
    @ApiModelProperty("菜单id")
    @NotEmpty(message = "菜单id不能为空", groups = {Update.class, Add.class})
    private String menuId;

    /**
     * 是否禁用 0:启用 1:禁用
     */
    @TableField("is_enable")
    @NotNull(message = "isDisable 不能为空", groups = {Update.class, Add.class})
    @Trans(type = TransType.DICTIONARY, key = "isEnable")
    @ApiModelProperty("是否禁用 0:启用 1:禁用")
    private Integer isEnable;

    /**
     * 权限类型12345，查询，添加，修改，删除，其他
     */
    @ApiModelProperty("权限类型")
    @NotNull(message = "权限类型不能为空", groups = {Update.class, Add.class})
    @TableField("permission_type")
    private Integer permissionType;

    /**
     * 状态
     */
    @TableField(exist = false)
    @ApiModelProperty("状态")
    private String state;


}
