package com.fhs.basics.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.base.valid.group.Add;
import com.fhs.core.base.valid.group.Delete;
import com.fhs.core.base.valid.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * @author jianbo.qin
 * @version [版本号, 2018/05/10 15:09:42]
 * @Description:生成订单号
 * @versio 1.0
 * 陕西小伙伴网络科技有限公司
 * Copyright (c) 2017 All Rights Reserved.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_service_order_log", autoResultMap = true)
@ApiModel(value = "OrderNumberDO", description = "OrderNumber参数")
public class OrderNumberPO extends BasePO<OrderNumberPO> {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId("id")
    @NotNull(message = "pubservice的id字段 不可为null ", groups = {Update.class, Delete.class})
    @Column(name = "id", nullable = false, length = 32)
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 类型
     */
    @TableField("type")
    @ApiModelProperty("类型")
    private String type;

    /**
     * 时间
     */
    @TableField("time")
    @ApiModelProperty("时间")
    private String time;

    /**
     * 数字
     */
    @Max(message = "{pubservice的number字段大于int最大值}", value = 2147483647, groups = {Add.class, Update.class})
    @Min(message = "{pubservice的number字段小于int小值", value = -2147483648, groups = {Add.class, Update.class})
    @Column(name = "number", nullable = true, length = 10)
    @ApiModelProperty("数字")
    private Integer number;

}
