package com.fhs.supplier.vo;


import com.fhs.supplier.po.SupplierSupplierPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 供应商(SupplierSupplier)实体类
 *
 * @author liangxiaotao
 * @since 2022-05-31 14:14:57
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="供应商",description ="供应商")
public class SupplierSupplierVO extends SupplierSupplierPO implements VO {
    
 }
    
