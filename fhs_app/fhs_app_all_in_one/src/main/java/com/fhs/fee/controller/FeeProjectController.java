package com.fhs.fee.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fhs.basics.po.ServiceDictItemPO;
import com.fhs.core.exception.ParamException;
import com.fhs.core.result.HttpResult;
import com.fhs.core.valid.checker.ParamChecker;
import com.fhs.demo.pojo.ExcelField;
import com.fhs.demo.utils.ExcelBaseUtils;
import com.fhs.fee.po.FeeProjectPO;
import com.fhs.fee.service.FeeProjectService;
import com.fhs.fee.vo.FeeProjectVO;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 费用项目(FeeProject)表控制层
 *
 * @author yutao
 * @since 2022-05-31 14:50:22
 */

@RestController
@Api(tags={"费用项目"})
@RequestMapping("/ms/feeProject")
public class FeeProjectController extends ModelSuperController<FeeProjectVO,FeeProjectPO> {

    @Autowired
    private FeeProjectService feeProjectService;

    @GetMapping("findCode")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "获取费用项目代码", notes = "数据来源", httpMethod = "GET")
    public HttpResult<String> exportTemplate(@RequestParam("id") String id) {
        FeeProjectVO feeProjectVO = feeProjectService.selectById(id);
        ParamChecker.isNotNullOrEmpty(feeProjectVO, "无效的费用分类");
        LambdaQueryWrapper<FeeProjectPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FeeProjectPO::getParentId, id);
        List<FeeProjectVO> feeProjectVOS = feeProjectService.selectListMP(wrapper);
        //项目费用代码 = 爸爸code + 自己排第几 = 0101
        String code = feeProjectVO.getCode() + String.format("%02d", feeProjectVOS.size() + 1);
        return HttpResult.success(code);
    }


}
