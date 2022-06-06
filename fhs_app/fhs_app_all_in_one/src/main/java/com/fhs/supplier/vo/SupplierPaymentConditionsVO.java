package com.fhs.supplier.vo;


import com.fhs.supplier.po.SupplierPaymentConditionsPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 供应商付款条件(SupplierPaymentConditions)实体类
 *
 * @author liangxiaotao
 * @since 2022-06-06 11:09:11
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="供应商付款条件",description ="供应商付款条件")
public class SupplierPaymentConditionsVO extends SupplierPaymentConditionsPO implements VO {
    
 }
    
