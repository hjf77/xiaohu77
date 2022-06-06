package com.fhs.supplier.controller;

import com.fhs.supplier.po.SupplierOrderPartyPO;
import com.fhs.supplier.vo.SupplierOrderPartyVO;
import org.springframework.web.bind.annotation.*;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;

/**
 * 订单方资料(SupplierOrderParty)表控制层
 *
 * @author liangxiaotao
 * @since 2022-06-01 16:50:55
 */

@RestController
@Api(tags={"订单方资料"})
@RequestMapping("/ms/supplierOrderParty")
public class SupplierOrderPartyController extends ModelSuperController<SupplierOrderPartyVO,SupplierOrderPartyPO> {
   

}
