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


   private Integer id;

   /**
    * 商品id
    */
   private Integer goodsId;

   /**
    * 采购协议id
    */
   private Integer agreementId;

   /**
    * 商品规格id
    */
   private Integer specificationId;

   /**
    * 税率
    */
   private Integer rate;

   /**
    * 含税单价
    */
   private Double taxUnitPrice;

   /**
    * 去税单价
    */
   private Double excludeTaxUnitPrice;

   /**
    * 采购单价
    */
   private Double purchasePrice;

   /**
    * 开始时间
    */
   private Date startTime;

   /**
    * 结束时间
    */
   private Date endTime;

   /**
    * 是否可退
    */
   private Integer isReturn;

   /**
    * 最小订单数量
    */
   private Integer minOrderNum;


   private List<AgreementSelectData> selectDataList;

 }
    
