package com.fhs.flow.controller.admin.task;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.fhs.basics.context.UserContext;
import com.fhs.core.base.vo.FhsPager;
import com.fhs.core.result.HttpResult;
import com.fhs.flow.controller.admin.task.vo.instance.*;
import com.fhs.flow.service.task.BpmProcessInstanceService;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


@Api(tags = "管理后台 - 流程实例") // 流程实例，通过流程定义创建的一次“申请”
@RestController
@ApiGroup(group ="bpm")
@RequestMapping("/ms/process-instance")
@Validated
public class BpmProcessInstanceController {

    @Resource
    private BpmProcessInstanceService processInstanceService;

    @GetMapping("/my-page")
    @ApiOperation(value = "获得我的实例分页列表", notes = "在【我的流程】菜单中，进行调用")
    @SaCheckRole("process-instance:see")
    public FhsPager<BpmProcessInstancePageItemRespVO> getMyProcessInstancePage(
            @Valid BpmProcessInstanceMyPageReqVO pageReqVO) {
        return processInstanceService.getMyProcessInstancePage(UserContext.getSessionuser().getUserId(), pageReqVO).toFhsPager();
    }

    @PostMapping("/create")
    @ApiOperation("新建流程实例")
    @SaCheckRole("process-instance:add")
    public HttpResult<String> createProcessInstance(@Valid @RequestBody BpmProcessInstanceCreateReqVO createReqVO) {
        return HttpResult.success(processInstanceService.createProcessInstance(UserContext.getSessionuser().getUserId(), createReqVO));
    }

    @GetMapping("/get")
    @ApiOperation(value = "获得指定流程实例", notes = "在【流程详细】界面中，进行调用")
    @SaCheckRole("process-instance:see")
    public HttpResult<BpmProcessInstanceRespVO> getProcessInstance(@RequestParam("id") String id) {
        return HttpResult.success(processInstanceService.getProcessInstanceVO(id));
    }

    @DeleteMapping("/cancel")
    @ApiOperation(value = "取消流程实例", notes = "撤回发起的流程")
    @SaCheckRole("process-instance:cancel")
    public HttpResult<Boolean> cancelProcessInstance(@Valid @RequestBody BpmProcessInstanceCancelReqVO cancelReqVO) {
        processInstanceService.cancelProcessInstance(UserContext.getSessionuser().getUserId(), cancelReqVO);
        return HttpResult.success(true);
    }
}
