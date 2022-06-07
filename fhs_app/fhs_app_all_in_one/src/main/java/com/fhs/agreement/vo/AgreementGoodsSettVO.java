package com.fhs.agreement.vo;

import com.fhs.agreement.po.AgreementGoodsSettPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 采购协议商品(AgreementGoodsSett)实体类
 *
 * @author caiyu
 * @since 2022-06-01 11:16:32
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "采购协议商品", description = "采购协议商品")
public class AgreementGoodsSettVO extends AgreementGoodsSettPO {

    @ApiModelProperty(value = "商品code")
    private String goodsCode;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品单位")
    private String unit;

    @ApiModelProperty(value = "商品条码")
    private String goodsBarcode;

}
    
