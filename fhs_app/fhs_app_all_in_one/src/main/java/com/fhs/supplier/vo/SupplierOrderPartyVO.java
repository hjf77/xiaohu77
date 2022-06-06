package com.fhs.supplier.vo;


import com.fhs.supplier.po.SupplierOrderPartyPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 订单方资料(SupplierOrderParty)实体类
 *
 * @author liangxiaotao
 * @since 2022-06-01 16:50:55
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="订单方资料",description ="订单方资料")
public class SupplierOrderPartyVO extends SupplierOrderPartyPO implements VO {
    
 }
    
