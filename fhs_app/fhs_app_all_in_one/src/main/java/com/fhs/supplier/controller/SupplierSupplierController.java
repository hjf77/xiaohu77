package com.fhs.supplier.controller;

import com.fhs.supplier.po.SupplierSupplierPO;
import com.fhs.supplier.vo.SupplierSupplierVO;
import org.springframework.web.bind.annotation.*;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;

/**
 * 供应商(SupplierSupplier)表控制层
 *
 * @author liangxiaotao
 * @since 2022-05-31 14:14:53
 */

@RestController
@Api(tags={"供应商"})
@RequestMapping("/ms/supplierSupplier")
public class SupplierSupplierController extends ModelSuperController<SupplierSupplierVO,SupplierSupplierPO> {
   

}
