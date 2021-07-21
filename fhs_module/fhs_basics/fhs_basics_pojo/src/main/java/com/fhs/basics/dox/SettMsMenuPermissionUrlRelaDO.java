package com.fhs.basics.dox;

import com.fhs.core.base.dox.BaseDO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.valid.group.Add;
import com.fhs.core.valid.group.Delete;
import com.fhs.core.valid.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author qixiaobo
 * @version [版本号, 2018-09-30]
 * @Description: 菜单权限和URL关联
 * @versio 1.0 陕西小伙伴网络科技有限公司 Copyright (c) 2018 All Rights Reserved.
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "t_sett_ms_menu_permission_url_rela")
@ApiModel(value = "SettMsMenuPermissionUrlRelaDO", description = "SettMsMenuPermissionUrlRela参数")
public class SettMsMenuPermissionUrlRelaDO extends BaseDO<SettMsMenuPermissionUrlRelaDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 权限id
     */
    @Id
    @NotNull(message = "id字段不可为null ", groups = {Update.class, Delete.class})
    @ApiModelProperty("权限id")
    private Integer permissionId;

    /**
     * 关联url
     */
    @NotEmpty
    @NotNull(message = "关联url字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "关联url字段的长度最大为200", groups = {Add.class, Update.class}, max = 200)
    @Column(name = "url")
    @ApiModelProperty("关联url")
    private String url;

    /**
     * 旧的url
     */
    @Transient
    @ApiModelProperty("旧的url")
    private String oldUrl;

}