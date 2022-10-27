package com.fhs.basics.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.basics.constant.BaseTransConstant;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.base.valid.group.Add;
import com.fhs.core.base.valid.group.Delete;
import com.fhs.core.base.valid.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 省市区字典
 *
 * @Filename: ServiceAreaDO.java
 * @Description:
 * @Version: 1.0
 * @Author: wanglei
 * @Email: qxb@sxpartner.com
 * @History:<br> 陕西小伙伴网络科技有限公司
 * Copyright (c) 2017 All Rights Reserved.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_service_area", autoResultMap = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ServiceAreaDO", description = "ServiceArea参数")
public class ServiceAreaPO extends BasePO<ServiceAreaPO> {
    private static final long serialVersionUID = 1L;

    /**
     * 区域主键
     */
    @TableId(value = "id",type = IdType.NONE)
    @NotNull(message = "area的id字段 不可为null ", groups = {Update.class, Delete.class})
    @Max(message = "area的id超过int最大值", value = 2147483647, groups = {Delete.class, Update.class})
    @Min(message = "area的id小于int最大值", value = -2147483648, groups = {Delete.class, Update.class})
    @ApiModelProperty("区域主键id")
    private Integer id;

    /**
     * 区域名称
     */
    @Length(message = "area的areaName字段的长度最大为16", groups = {Add.class, Update.class}, max = 16, min = 0)
    @TableField(value = "area_name")
    @ApiModelProperty("区域名称")
    private String areaName;

    /**
     * 区域代码
     */
    @Length(message = "area的areaCode字段的长度最大为128", groups = {Add.class, Update.class}, max = 128, min = 0)
    @ApiModelProperty("区域代码")
    @TableField(value = "area_code")
    private String areaCode;

    /**
     * 区域简称
     */
    @Length(message = "area的areaShort字段的长度最大为32", groups = {Add.class, Update.class}, max = 32, min = 0)
    @ApiModelProperty("区域简称")
    @TableField(value = "area_short")
    private String areaShort;

    /**
     * 是否热门(0:否、1:是)
     */
    @Length(message = "area的areaIsHot字段的长度最大为1", groups = {Add.class, Update.class}, max = 1, min = 0)
    @ApiModelProperty("是否热门")
    @TableField(value = "area_is_hot")
    private String areaIsHot;

    /**
     * 区域序列
     */
    @Max(message = "{area的areaSequence字段大于int最大值}", value = 2147483647, groups = {Add.class, Update.class})
    @Min(message = "{area的areaSequence字段小于int小值", value = -2147483648, groups = {Add.class, Update.class})
    @ApiModelProperty("区域序列")
    @TableField("area_sequence")
    private Integer areaSequence;

    /**
     * 上级主键
     */
    @Max(message = "{area的areaParentId字段大于int最大值}", value = 2147483647, groups = {Add.class, Update.class})
    @Min(message = "{area的areaParentId字段小于int小值", value = -2147483648, groups = {Add.class, Update.class})
    @Trans(type = TransType.AUTO_TRANS, key = BaseTransConstant.AREA + "#area")
    @ApiModelProperty("上级主键")
    @TableField("area_parent_id")
    private Integer areaParentId;

    /**
     * 初始时间
     */
    @ApiModelProperty("初始时间")
    @TableField("init_date")
    private String initDate;

    /**
     * 区域编码
     */
    @ApiModelProperty("区域编码")
    @TableId(value = "code")
    private Integer code;

    /**
     * 初始地址
     */
    @Length(message = "area的initAddr字段的长度最大为16", groups = {Add.class, Update.class}, max = 16, min = 0)
    @ApiModelProperty("初始地址")
    @TableId(value = "init_addr")
    private String initAddr;

}
