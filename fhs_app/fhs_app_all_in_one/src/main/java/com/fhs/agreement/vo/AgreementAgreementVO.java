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

    /**
     * 商品
     */
    private AgreementGoodsSettVO[] goodsVOs;


 }
    
