package com.fhs.agreement.vo;

import com.fhs.agreement.po.AgreementAgreementPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * 采购协议(AgreementAgreement)实体类
 *
 * @author caiyu
 * @since 2022-06-01 10:09:45
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="采购协议",description ="采购协议")
public class AgreementAgreementVO extends AgreementAgreementPO implements VO {

     private Integer id;

     /**
      * 协议号
      */
     private String no;

     /**
      * 状态 0未审核 1已审核 2已驳回
      */
     private Integer status;

     /**
      * 订单方id
      */
     private Integer orderPartyId;

     /**
      * 供应商id
      */
     private Integer supplierId;

     /**
      * 合同编号
      */
     private String contractNo;

     /**
      * 明细数
      */
     private Integer detailNum;

     /**
      * 所属组织
      */
     private Long orgId;

     /**
      * 开始时间
      */
     private Date startTime;

     /**
      * 结束时间
      */
     private Date endTime;

     /**
      * 备注
      */
     private String remark;

    /**
     * 商品
     */
    private AgreementGoodsSettVO[] goodsVOs;


 }
    
