package com.fhs.agreement.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.base.valid.group.Delete;
import com.fhs.core.base.valid.group.Update;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 采购协议商品(AgreementGoodsSett)实体类
 *
 * @author caiyu
 * @since 2022-06-01 11:16:33
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_agreement_goods_sett")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="AgreementGoodsSettPO",description ="采购协议商品")
public class AgreementGoodsSettPO extends BasePO<AgreementGoodsSettPO> {
    
    private static final long serialVersionUID = -40708952674519768L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "id字段不能为空", groups = {Update.class, Delete.class})
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 商品id
     */
    @TableField("goods_id")
    @ApiModelProperty(value = "商品id")
    private Integer goodsId;

    /**
     * 采购协议id
     */
    @TableField("agreement_id")
    @ApiModelProperty(value = "采购协议id")
    private Integer agreementId;

    /**
     * 商品规格id
     */
    @TableField("specification_id")
    @ApiModelProperty(value = "商品规格id")
    private Integer specificationId;

    /**
     * 税率
     */
    @TableField("rate")
    @ApiModelProperty(value = "税率")
    @Trans(type = TransType.DICTIONARY,key = "rate")
    private Integer rate;

    /**
     * 含税单价
     */
    @TableField("tax_unit_price")
    @ApiModelProperty(value = "含税单价")
    private Double taxUnitPrice;

    /**
     * 去税单价
     */
    @TableField("exclude_tax_unit_price")
    @ApiModelProperty(value = "去税单价")
    private Double excludeTaxUnitPrice;

    /**
     * 采购单价
     */
    @TableField("purchase_price")
    @ApiModelProperty(value = "采购单价")
    private Double purchasePrice;

    /**
     * 开始时间
     */
    @TableField("start_time")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 是否可退
     */
    @TableField("is_return")
    @ApiModelProperty(value = "是否可退")
    @Trans(type = TransType.DICTIONARY, key = "yesOrNo")
    private Integer isReturn;

    /**
     * 最小订单数量
     */
    @TableField("min_order_num")
    @ApiModelProperty(value = "最小订单数量")
    private Integer minOrderNum;



}
