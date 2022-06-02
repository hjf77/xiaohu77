package com.fhs.fee.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fhs.common.utils.IdHelper;
import com.fhs.core.result.HttpResult;
import com.fhs.core.valid.checker.ParamChecker;
import com.fhs.fee.po.FeePlanPO;
import com.fhs.fee.po.FeeProjectPO;
import com.fhs.fee.vo.FeePlanVO;
import com.fhs.fee.vo.FeeProjectVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;

import java.util.List;

/**
 * 费用方案(FeePlan)表控制层
 *
 * @author wanglei
 * @since 2022-06-01 15:42:18
 */

@RestController
@Api(tags={"费用方案"})
@RequestMapping("/ms/feePlan")
public class FeePlanController extends ModelSuperController<FeePlanVO,FeePlanPO> {

    @Autowired
    private IdHelper idHelper;

    @GetMapping("findNo")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "获取费用方案单号", notes = "数据来源", httpMethod = "GET")
    public HttpResult<String> exportTemplate() {
        long code = idHelper.nextId();
        return HttpResult.success(String.valueOf(code));
    }

}
