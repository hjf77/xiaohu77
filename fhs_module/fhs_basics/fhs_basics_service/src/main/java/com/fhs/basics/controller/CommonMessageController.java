package com.fhs.basics.controller;

import com.fhs.basics.api.anno.LogMethod;
import com.fhs.basics.po.CommonMessagePO;
import com.fhs.basics.service.CommonMessageService;
import com.fhs.basics.vo.CommonMessageVO;
import com.fhs.core.exception.ParamException;
import com.fhs.core.result.HttpResult;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息推送表(CommonMessage)表控制层
 *
 * @author miyaxin
 * @since 2022-08-18 16:21:42
 */

@RestController
@Api(tags = {"消息推送表"})
@RequestMapping("/ms/commonMessage")
public class CommonMessageController extends ModelSuperController<CommonMessageVO, CommonMessagePO, Long> {

    @Autowired
    private CommonMessageService commonMessageService;

    @ResponseBody
    @PostMapping({"delMessages"})
    @ApiOperation("删除消息")
    @LogMethod(type = 2, pkeyParamIndex = 0)
    public HttpResult<Boolean> delMessages(@RequestParam("ids") List<Long> ids) {
        if (!ids.isEmpty()) {
            int count = commonMessageService.deleteBatchIds(ids);
            if (count > 0) {
                return HttpResult.success(true);
            }
        }
        throw new ParamException("请选择需要删除的消息!");
    }
}
