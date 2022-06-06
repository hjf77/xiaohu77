package com.fhs.supplier.vo;


import com.fhs.supplier.po.SupplierReconciliationConditionsPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 供应商对账条件配置(SupplierReconciliationConditions)实体类
 *
 * @author wanglei
 * @since 2022-06-06 09:43:27
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="供应商对账条件配置",description ="供应商对账条件配置")
public class SupplierReconciliationConditionsVO extends SupplierReconciliationConditionsPO implements VO {

 }

