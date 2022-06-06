package com.fhs.supplier.service.impl;


import com.fhs.supplier.vo.SupplierPaymentConditionsVO;
import com.fhs.supplier.po.SupplierPaymentConditionsPO;
import com.fhs.supplier.service.SupplierPaymentConditionsService;
import org.springframework.stereotype.Service;
import com.fhs.core.db.ds.DataSource;
import com.fhs.core.cache.annotation.Namespace;
import com.fhs.core.base.service.impl.BaseServiceImpl;

/**
 * 供应商付款条件(SupplierPaymentConditions)表服务实现类
 *
 * @author liangxiaotao
 * @since 2022-06-06 11:09:09
 */
@Service
@Namespace("supplier_payment_conditions")
public class SupplierPaymentConditionsServiceImpl extends BaseServiceImpl<SupplierPaymentConditionsVO,SupplierPaymentConditionsPO> implements SupplierPaymentConditionsService {
    
}
