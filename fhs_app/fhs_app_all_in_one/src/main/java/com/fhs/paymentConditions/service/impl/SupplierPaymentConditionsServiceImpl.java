package com.fhs.paymentConditions.service.impl;


import com.fhs.paymentConditions.vo.SupplierPaymentConditionsVO;
import com.fhs.paymentConditions.po.SupplierPaymentConditionsPO;
import com.fhs.paymentConditions.service.SupplierPaymentConditionsService;
import org.springframework.stereotype.Service;
import com.fhs.core.cache.annotation.Namespace;
import com.fhs.core.base.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * 供应商付款条件(SupplierPaymentConditions)表服务实现类
 *
 * @author caiyu
 * @since 2022-06-02 15:49:06
 */
@Service
@Namespace("supplier_payment_conditions")
public class SupplierPaymentConditionsServiceImpl extends BaseServiceImpl<SupplierPaymentConditionsVO,SupplierPaymentConditionsPO> implements SupplierPaymentConditionsService {
    @Override
    public int insert(SupplierPaymentConditionsPO entity) {
        int insert = super.insert(entity);
        StringBuilder conCode = new StringBuilder();
        conCode.append("FC0");
        conCode.append(entity.getId());
        entity.setConditionsCode(conCode.toString());
        List<SupplierPaymentConditionsPO> list = new ArrayList<>();
        list.add(entity);
        this.batchUpdate(list);
        return insert;
    }
}
