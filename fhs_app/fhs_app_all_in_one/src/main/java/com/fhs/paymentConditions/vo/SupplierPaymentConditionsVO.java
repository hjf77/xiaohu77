package com.fhs.paymentConditions.vo;


import com.fhs.paymentConditions.po.SupplierPaymentConditionsPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 供应商付款条件(SupplierPaymentConditions)实体类
 *
 * @author caiyu
 * @since 2022-06-02 15:49:07
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="供应商付款条件",description ="供应商付款条件")
public class SupplierPaymentConditionsVO extends SupplierPaymentConditionsPO implements VO {
    
 }
    
