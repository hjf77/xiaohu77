package com.fhs.basics.po;

import com.fhs.core.base.po.BasePO;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.valid.group.Add;
import com.fhs.core.valid.group.Delete;
import com.fhs.core.valid.group.Update;
import com.mybatis.jpa.annotation.Like;
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
@Table(name = "t_sett_ms_system")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SettMsSystemDO", description = "SettMsSystem参数")
public class SettMsSystemPO extends BasePO<SettMsSystemPO> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @NotNull(message = "id字段不可为null ", groups = {Update.class, Delete.class})
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 子系统名称
     */
    @Like
    @NotEmpty
    @NotNull(message = "子系统名称字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "子系统名称字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @Column(name = "name")
    @ApiModelProperty("子系统名称")
    private String name;

    /**
     * 排序
     */
    @Max(message = "排序字段大于int最大值", value = 2147483647, groups = {Add.class, Update.class})
    @Min(message = "排序字段小于int最小值", value = -2147483648, groups = {Add.class, Update.class})
    @Column(name = "sort")
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 子系统logo
     */
    @NotEmpty
    @NotNull(message = "子系统logo字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "子系统logo字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @Column(name = "logo")
    @ApiModelProperty("子系统logo")
    private String logo;

    /**
     * 是否禁用 0:启用 1:禁用
     */
    @Max(message = "是否禁用 0:启用 1:禁用字段大于int最大值", value = 2147483647, groups = {Add.class, Update.class})
    @Min(message = "是否禁用 0:启用 1:禁用字段小于int最小值", value = -2147483648, groups = {Add.class, Update.class})
    @Column(name = "is_enable")
    @ApiModelProperty("是否禁用")
    @Trans(type = TransType.DICTIONARY, key = "is_enable")
    private Integer isEnable;

    /**
     * 0 全新 1 集成现有
     */
    @Max(message = "0 全新 1 集成现有字段大于int最大值", value = 2147483647, groups = {Add.class, Update.class})
    @Min(message = "0 全新 1 集成现有字段小于int最小值", value = -2147483648, groups = {Add.class, Update.class})
    @Column(name = "type")
    @ApiModelProperty("类型 0 全新 1 集成现有")
    @Trans(type = TransType.DICTIONARY, key = "system_type")
    private Integer type;

    /**
     * 如果是集成第三方，则写第三方url
     */
    @NotEmpty
    @NotNull(message = "如果是集成第三方，则写第三方url字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "如果是集成第三方，则写第三方url字段的长度最大为255", groups = {Add.class, Update.class}, max = 255)
    @Column(name = "url")
    @ApiModelProperty("如果是集成第三方，则写第三方url")
    private String url;

    /**
     * 子系统首页url
     */
    @NotEmpty
    @NotNull(message = "子系统首页url字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "子系统首页url字段的长度最大为255", groups = {Add.class, Update.class}, max = 255)
    @Column(name = "index_url")
    @ApiModelProperty("子系统首页url")
    private String indexUrl;

}
