package com.fhs.supplier.controller;

import com.fhs.supplier.po.SupplierReconciliationConditionsPO;
import com.fhs.supplier.vo.SupplierReconciliationConditionsVO;
import org.springframework.web.bind.annotation.*;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;

/**
 * 供应商对账条件配置(SupplierReconciliationConditions)表控制层
 *
 * @author wanglei
 * @since 2022-06-06 09:43:27
 */

@RestController
@Api(tags={"供应商对账条件配置"})
@RequestMapping("/ms/supplierReconciliationConditions")
public class SupplierReconciliationConditionsController extends ModelSuperController<SupplierReconciliationConditionsVO,SupplierReconciliationConditionsPO> {


}
