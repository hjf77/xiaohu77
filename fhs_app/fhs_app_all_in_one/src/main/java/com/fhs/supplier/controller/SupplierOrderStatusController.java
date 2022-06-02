package com.fhs.supplier.controller;

import com.fhs.supplier.po.SupplierOrderStatusPO;
import com.fhs.supplier.vo.SupplierOrderStatusVO;
import org.springframework.web.bind.annotation.*;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;

/**
 * 订单方状态管理(SupplierOrderStatus)表控制层
 *
 * @author liangxiaotao
 * @since 2022-06-01 16:50:55
 */

@RestController
@Api(tags={"订单方状态管理"})
@RequestMapping("/ms/supplierOrderStatus")
public class SupplierOrderStatusController extends ModelSuperController<SupplierOrderStatusVO,SupplierOrderStatusPO> {
   

}
