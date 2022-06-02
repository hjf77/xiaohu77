package com.fhs.agreement.vo;

import com.fhs.agreement.po.AgreementGoodsSettPO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 采购协议商品(AgreementGoodsSett)实体类
 *
 * @author caiyu
 * @since 2022-06-01 11:16:32
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="采购协议商品",description ="采购协议商品")
public class AgreementGoodsSettVO extends AgreementGoodsSettPO {

   private List<AgreementSelectData> selectDataList;

 }
    
