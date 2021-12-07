package com.fhs.basics.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.valid.group.Add;
import com.fhs.core.valid.group.Delete;
import com.fhs.core.valid.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 子系统(SettMsSystem)实体类
 *
 * @author user
 * @since 2020-05-18 15:12:29
 */

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName( "t_sett_ms_system")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SettMsSystemDO", description = "SettMsSystem参数")
public class SettMsSystemPO extends BasePO<SettMsSystemPO> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId
    @NotNull(message = "id字段不可为null ", groups = {Update.class, Delete.class})
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 子系统名称
     */
    @NotEmpty
    @NotNull(message = "子系统名称字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "子系统名称字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @ApiModelProperty("子系统名称")
    private String name;

    /**
     * 排序
     */
    @Max(message = "排序字段大于int最大值", value = 2147483647, groups = {Add.class, Update.class})
    @Min(message = "排序字段小于int最小值", value = -2147483648, groups = {Add.class, Update.class})
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 子系统logo
     */
    @NotEmpty
    @NotNull(message = "子系统logo字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "子系统logo字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @ApiModelProperty("子系统logo")
    private String logo;

    /**
     * 是否禁用 0:启用 1:禁用
     */
    @Max(message = "是否禁用 0:启用 1:禁用字段大于int最大值", value = 2147483647, groups = {Add.class, Update.class})
    @Min(message = "是否禁用 0:启用 1:禁用字段小于int最小值", value = -2147483648, groups = {Add.class, Update.class})
    @ApiModelProperty("是否禁用")
    @Trans(type = TransType.DICTIONARY, key = "is_enable")
    private Integer isEnable;

    /**
     * 0 全新 1 集成现有
     */
    @Max(message = "0 全新 1 集成现有字段大于int最大值", value = 2147483647, groups = {Add.class, Update.class})
    @Min(message = "0 全新 1 集成现有字段小于int最小值", value = -2147483648, groups = {Add.class, Update.class})
    @ApiModelProperty("类型 0 全新 1 集成现有")
    @Trans(type = TransType.DICTIONARY, key = "system_type")
    private Integer type;

    /**
     * 如果是集成第三方，则写第三方url
     */
    @NotEmpty
    @NotNull(message = "如果是集成第三方，则写第三方url字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "如果是集成第三方，则写第三方url字段的长度最大为255", groups = {Add.class, Update.class}, max = 255)
    @ApiModelProperty("如果是集成第三方，则写第三方url")
    private String url;

    /**
     * 子系统首页url
     */
    @NotEmpty
    @NotNull(message = "子系统首页url字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "子系统首页url字段的长度最大为255", groups = {Add.class, Update.class}, max = 255)
    @ApiModelProperty("子系统首页url")
    private String indexUrl;

}
