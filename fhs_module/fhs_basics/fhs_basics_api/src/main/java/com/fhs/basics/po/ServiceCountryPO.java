package com.fhs.basics.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.base.valid.group.Delete;
import com.fhs.core.base.valid.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * (ServiceCountry)实体类
 *
 * @author miyaxin
 * @since 2022-08-17 17:18:34
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_service_country")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ServiceCountryPO", description = "ServiceCountry参数")
public class ServiceCountryPO extends BasePO<ServiceCountryPO> {
    private static final long serialVersionUID = 832168911611845208L;
    @TableId(value = "id", type = IdType.NONE)
    @NotNull(message = "${column.comment}字段不可为空", groups = {Update.class, Delete.class})
    @ApiModelProperty(value = "")
    private Integer id;

    /**
     * 国家所在的七大洲对应的id,对应于表continent的主键
     */
    @TableField("continent_id")
    @ApiModelProperty(value = "国家所在的七大洲对应的id,对应于表continent的主键")
    private Integer continentId;

    /**
     * 国家英文常用标准名称，主要用于显示
     */
    @TableField("name")
    @ApiModelProperty(value = "国家英文常用标准名称，主要用于显示")
    private String name;

    /**
     * 国家英文标准名称的小写，主要用于搜索与比较
     */
    @TableField("lower_name")
    @ApiModelProperty(value = "国家英文标准名称的小写，主要用于搜索与比较")
    private String lowerName;

    /**
     * 国家英文代码,国家名称缩写
     */
    @TableField("country_code")
    @ApiModelProperty(value = "国家英文代码,国家名称缩写")
    private String countryCode;

    /**
     * 国家英文名称全称
     */
    @TableField("full_name")
    @ApiModelProperty(value = "国家英文名称全称")
    private String fullName;

    /**
     * 国家中文常用标准名称
     */
    @TableField("cname")
    @ApiModelProperty(value = "国家中文常用标准名称")
    private String cname;

    /**
     * 国家中文全称名称，非缩写
     */
    @TableField("full_cname")
    @ApiModelProperty(value = "国家中文全称名称，非缩写")
    private String fullCname;

    /**
     * 备注，国家的一些说明
     */
    @TableField("remark")
    @ApiModelProperty(value = "备注，国家的一些说明")
    private String remark;

    /**
     * 国家首字母
     */
    @TableField("fb_country_code")
    @ApiModelProperty(value = "国家首字母")
    private String fbCountryCode;


    @TableField("lang")
    @ApiModelProperty(value = "${column.comment}")
    private String lang;

    @TableField("is_gulf_country")
    @ApiModelProperty(value = "是否是海湾7国成员(0否,1是)")
    private Integer isGulfCountry;

}
