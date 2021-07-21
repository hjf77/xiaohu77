package com.fhs.basics.dox;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.common.tree.Treeable;
import com.fhs.core.base.dox.BaseDO;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.valid.group.Add;
import com.fhs.core.valid.group.Delete;
import com.fhs.core.valid.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

/**
 * @author qixiaobo
 * @version [版本号, 2018-09-04]
 * @Description:机构管理表
 * @versio 1.0 陕西小伙伴网络科技有限公司 Copyright (c) 2018 All Rights Reserved.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName( "t_ucenter_ms_organization")
@ApiModel(value = "UcenterMsOrganizationDO", description = "UcenterMsOrganization参数")
public class UcenterMsOrganizationDO extends BaseDO<UcenterMsOrganizationDO> implements Treeable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.UUID)
    @NotNull(message = "id字段不可为null ", groups = {Update.class, Delete.class})
    @ApiModelProperty(value = "主键id")
    private String id;

    /**
     * 机构名称
     */
    @NotNull(message = "机构名称字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "机构名称字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField( "name")
    @ApiModelProperty(value = "机构名称")
    private String name;

    /**
     * 父类编号
     */
    @NotNull(message = "父类编号字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "父类编号字段的长度最大为32", groups = {Add.class, Update.class}, max = 255)
    @TableField( "parent_id")
    @ApiModelProperty(value = "父类编号")
    private String parentId;

    /**
     * 同级菜单排行第几
     */
    @NotNull(message = "同级菜单排行第几字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "同级菜单排行第几字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField( "ranking")
    @ApiModelProperty(value = "同级菜单排行第几")
    private String ranking;

    /**
     * 是否启用(0:启用 1:禁用)
     */
    @TableField( "is_enable")
    @ApiModelProperty(value = "是否启用")
    @Trans(type = TransType.WORD_BOOK, key = "is_enable")
    private Integer isEnable;

    /**
     * 集团编码
     */
    @TableField( "group_code")
    @ApiModelProperty(value = "集团编码")
    private String groupCode;

    /**
     * 扩展字段
     */
    @TableField("ext_json")
    @ApiModelProperty(value = "扩展字段")
    private String extJson;

}
