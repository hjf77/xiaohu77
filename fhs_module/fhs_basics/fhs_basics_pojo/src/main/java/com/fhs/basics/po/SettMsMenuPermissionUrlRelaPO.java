package com.fhs.basics.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.valid.group.Add;
import com.fhs.core.valid.group.Delete;
import com.fhs.core.valid.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author wanglei
 * @version [版本号, 2018-09-30]
 * @Description: 菜单权限和URL关联
 * @versio 1.0 陕西小伙伴网络科技有限公司 Copyright (c) 2018 All Rights Reserved.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_sett_ms_menu_permission_url_rela")
@ApiModel(value = "SettMsMenuPermissionUrlRelaDO", description = "SettMsMenuPermissionUrlRela参数")
public class SettMsMenuPermissionUrlRelaPO extends BasePO<SettMsMenuPermissionUrlRelaPO> {

    private static final long serialVersionUID = 1L;

    /**
     * 权限id
     */
    @TableId(type = IdType.AUTO)
    @NotNull(message = "id字段不可为null ", groups = {Update.class, Delete.class})
    @ApiModelProperty("权限id")
    private Integer permissionId;

    /**
     * 关联url
     */
    @NotEmpty
    @NotNull(message = "关联url字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "关联url字段的长度最大为200", groups = {Add.class, Update.class}, max = 200)
    @ApiModelProperty("关联url")
    private String url;

    /**
     * 旧的url
     */
    @TableField(exist = false)
    @ApiModelProperty("旧的url")
    private String oldUrl;

}
