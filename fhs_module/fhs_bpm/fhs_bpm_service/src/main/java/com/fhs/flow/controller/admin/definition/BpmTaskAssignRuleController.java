package com.fhs.flow.controller.admin.definition;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.fhs.core.base.vo.FhsPager;
import com.fhs.core.result.HttpResult;
import com.fhs.flow.controller.admin.definition.vo.rule.BpmTaskAssignRuleCreateReqVO;
import com.fhs.flow.controller.admin.definition.vo.rule.BpmTaskAssignRuleRespVO;
import com.fhs.flow.controller.admin.definition.vo.rule.BpmTaskAssignRuleUpdateReqVO;
import com.fhs.flow.service.definition.BpmTaskAssignRuleService;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.fhs.flow.comon.pojo.CommonResult.success;


@Api(tags = "管理后台 - 任务分配规则")
@ApiGroup(group ="bpm")
@RestController
@RequestMapping("/ms/task-assign-rule")
@Validated
public class BpmTaskAssignRuleController {

    @Resource
    private BpmTaskAssignRuleService taskAssignRuleService;

    @PostMapping("/list")
    @ApiOperation(value = "获得任务分配规则列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelId", value = "模型编号", example = "1024"),
            @ApiImplicitParam(name = "processDefinitionId", value = "流程定义的编号", example = "2048")
    })
    @SaCheckRole("task-assign-rule:see")
    public FhsPager<BpmTaskAssignRuleRespVO> getTaskAssignRuleList(
            @RequestParam(value = "modelId", required = false) String modelId,
            @RequestParam(value = "processDefinitionId", required = false) String processDefinitionId) {
        List<BpmTaskAssignRuleRespVO> taskAssignRuleList = taskAssignRuleService.getTaskAssignRuleList(modelId, processDefinitionId);
        FhsPager<BpmTaskAssignRuleRespVO> result = new FhsPager<>();
        result.setTotal(taskAssignRuleList.size());
        result.setRecords(taskAssignRuleList);
        return result;
    }

    @PostMapping("/create")
    @ApiOperation(value = "创建任务分配规则")
    @SaCheckRole("task-assign-rule:add")
    public HttpResult<Long> createTaskAssignRule(@Valid @RequestBody BpmTaskAssignRuleCreateReqVO reqVO) {
        return success(taskAssignRuleService.createTaskAssignRule(reqVO)).toHttpResult();
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新任务分配规则")
    @SaCheckRole("task-assign-rule:update")
    public HttpResult<Boolean> updateTaskAssignRule(@Valid @RequestBody BpmTaskAssignRuleUpdateReqVO reqVO) {
        taskAssignRuleService.updateTaskAssignRule(reqVO);
        return success(true).toHttpResult();
    }
}
