package com.fhs.supplier.service.impl;


import com.fhs.supplier.vo.SupplierOrderStatusVO;
import com.fhs.supplier.po.SupplierOrderStatusPO;
import com.fhs.supplier.service.SupplierOrderStatusService;
import org.springframework.stereotype.Service;
import com.fhs.core.db.ds.DataSource;
import com.fhs.core.cache.annotation.Namespace;
import com.fhs.core.base.service.impl.BaseServiceImpl;

/**
 * 订单方状态管理(SupplierOrderStatus)表服务实现类
 *
 * @author liangxiaotao
 * @since 2022-06-01 16:50:55
 */
@Service
@Namespace("supplier_order_status")
public class SupplierOrderStatusServiceImpl extends BaseServiceImpl<SupplierOrderStatusVO,SupplierOrderStatusPO> implements SupplierOrderStatusService {
    
}
