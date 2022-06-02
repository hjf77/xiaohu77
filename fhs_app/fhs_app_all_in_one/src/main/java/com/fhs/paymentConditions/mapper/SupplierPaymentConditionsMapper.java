package com.fhs.paymentConditions.mapper;

import com.fhs.paymentConditions.po.SupplierPaymentConditionsPO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 供应商付款条件(SupplierPaymentConditions)表数据库访问层
 *
 * @author caiyu
 * @since 2022-06-02 15:49:08
 */
@Repository
public interface SupplierPaymentConditionsMapper extends FhsBaseMapper<SupplierPaymentConditionsPO> {

}
