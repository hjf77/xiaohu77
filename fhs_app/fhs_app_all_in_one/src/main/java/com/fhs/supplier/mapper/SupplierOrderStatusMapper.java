package com.fhs.supplier.mapper;

import com.fhs.supplier.po.SupplierOrderStatusPO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 订单方状态管理(SupplierOrderStatus)表数据库访问层
 *
 * @author liangxiaotao
 * @since 2022-06-01 16:50:55
 */
@Repository
public interface SupplierOrderStatusMapper extends FhsBaseMapper<SupplierOrderStatusPO> {

}
