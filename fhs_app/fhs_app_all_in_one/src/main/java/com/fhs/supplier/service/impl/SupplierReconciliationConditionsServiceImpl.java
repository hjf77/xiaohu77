package com.fhs.supplier.service.impl;


import com.fhs.supplier.vo.SupplierReconciliationConditionsVO;
import com.fhs.supplier.po.SupplierReconciliationConditionsPO;
import com.fhs.supplier.service.SupplierReconciliationConditionsService;
import org.springframework.stereotype.Service;
import com.fhs.core.db.ds.DataSource;
import com.fhs.core.cache.annotation.Namespace;
import com.fhs.core.base.service.impl.BaseServiceImpl;

/**
 * 供应商对账条件配置(SupplierReconciliationConditions)表服务实现类
 *
 * @author wanglei
 * @since 2022-06-06 09:43:27
 */
@Service
@Namespace("supplier_reconciliation_conditions")
public class SupplierReconciliationConditionsServiceImpl extends BaseServiceImpl<SupplierReconciliationConditionsVO,SupplierReconciliationConditionsPO> implements SupplierReconciliationConditionsService {

}
