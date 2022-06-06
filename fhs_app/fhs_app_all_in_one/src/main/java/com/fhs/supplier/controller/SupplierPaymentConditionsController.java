package com.fhs.supplier.controller;

import com.fhs.supplier.po.SupplierPaymentConditionsPO;
import com.fhs.supplier.vo.SupplierPaymentConditionsVO;
import org.springframework.web.bind.annotation.*;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;

/**
 * 供应商付款条件(SupplierPaymentConditions)表控制层
 *
 * @author liangxiaotao
 * @since 2022-06-06 11:09:09
 */

@RestController
@Api(tags={"供应商付款条件"})
@RequestMapping("/ms/supplierPaymentConditions")
public class SupplierPaymentConditionsController extends ModelSuperController<SupplierPaymentConditionsVO,SupplierPaymentConditionsPO> {
   

}
