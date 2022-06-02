package com.fhs.supplier.service.impl;


import com.fhs.supplier.vo.SupplierOrderPartyVO;
import com.fhs.supplier.po.SupplierOrderPartyPO;
import com.fhs.supplier.service.SupplierOrderPartyService;
import org.springframework.stereotype.Service;
import com.fhs.core.db.ds.DataSource;
import com.fhs.core.cache.annotation.Namespace;
import com.fhs.core.base.service.impl.BaseServiceImpl;

/**
 * 订单方资料(SupplierOrderParty)表服务实现类
 *
 * @author liangxiaotao
 * @since 2022-06-01 16:50:55
 */
@Service
@Namespace("supplier_order_party")
public class SupplierOrderPartyServiceImpl extends BaseServiceImpl<SupplierOrderPartyVO,SupplierOrderPartyPO> implements SupplierOrderPartyService {
    
}
