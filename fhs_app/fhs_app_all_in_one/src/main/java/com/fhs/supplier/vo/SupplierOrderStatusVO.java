package com.fhs.supplier.vo;


import com.fhs.supplier.po.SupplierOrderStatusPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 订单方状态管理(SupplierOrderStatus)实体类
 *
 * @author liangxiaotao
 * @since 2022-06-01 16:50:55
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="订单方状态管理",description ="订单方状态管理")
public class SupplierOrderStatusVO extends SupplierOrderStatusPO implements VO {
    
 }
    
