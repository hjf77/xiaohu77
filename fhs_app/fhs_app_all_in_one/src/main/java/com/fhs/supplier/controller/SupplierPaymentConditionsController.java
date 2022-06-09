package com.fhs.supplier.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.fhs.basics.api.anno.LogMethod;
import com.fhs.basics.vo.ServiceDictItemVO;
import com.fhs.supplier.po.SupplierPaymentConditionsPO;
import com.fhs.supplier.vo.SupplierPaymentConditionsVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    /**
     * 当月天数
     * @return
     */
    @LogMethod
    @ApiOperation("当月天数")
    @SaCheckRole("supplierPaymentConditions:see")
    @GetMapping("dateBaseDayList")
    public List<ServiceDictItemVO> dateBaseDayList(HttpServletRequest request) {
        List<ServiceDictItemVO> list = new ArrayList<>();
        for (int i = 1; i <= getCurrentMonthDay(); i++) {
            ServiceDictItemVO dict = new ServiceDictItemVO();
            dict.setDictCode(i + "");
            dict.setDictDesc(i + "");
            list.add(dict);
        }
        return list;
    }

    /**
     * 获取当月天数
     * @return
     */
    public static int getCurrentMonthDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }


}
