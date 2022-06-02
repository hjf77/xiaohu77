package com.fhs.paymentConditions.controller;

import com.fhs.paymentConditions.po.SupplierPaymentConditionsPO;
import com.fhs.paymentConditions.vo.SupplierPaymentConditionsVO;
import org.springframework.web.bind.annotation.*;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;

/**
 * 供应商付款条件(SupplierPaymentConditions)表控制层
 *
 * @author caiyu
 * @since 2022-06-02 15:49:06
 */

@RestController
@Api(tags={"供应商付款条件"})
@RequestMapping("/ms/supierplPaymentConditions")
public class SupplierPaymentConditionsController extends ModelSuperController<SupplierPaymentConditionsVO,SupplierPaymentConditionsPO> {
   

}
