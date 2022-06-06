package com.fhs.supplier.service.impl;


import com.fhs.common.utils.StringUtils;
import com.fhs.supplier.constant.SupplierConstant;
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
public class SupplierReconciliationConditionsServiceImpl extends BaseServiceImpl<SupplierReconciliationConditionsVO, SupplierReconciliationConditionsPO> implements SupplierReconciliationConditionsService {
    @Override
    public int insert(SupplierReconciliationConditionsPO entity) {
        int result = super.insert(entity);
        entity.setConditionsCode(SupplierConstant.RECONCILIATIONC_PRE +  StringUtils.formatCountWith0("",
                "%06d",entity.getId()));
        return result;
    }
}
