package com.fhs.flow.controller.admin.task;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.fhs.basics.context.UserContext;
import com.fhs.core.base.vo.FhsPager;
import com.fhs.core.result.HttpResult;
import com.fhs.flow.comon.pojo.PageResult;
import com.fhs.flow.controller.admin.task.vo.task.*;
import com.fhs.flow.service.task.BpmTaskService;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.fhs.flow.comon.pojo.CommonResult.success;

@Api(tags = "管理后台 - 流程任务实例")
@RestController
@RequestMapping("/ms/task")
@ApiGroup(group ="bpm")
@Validated
public class BpmTaskController {

    @Resource
    private BpmTaskService taskService;

    @GetMapping("todo-page")
    @ApiOperation("获取 Todo 待办任务分页")
    @SaCheckRole("task:see")
    public HttpResult<PageResult<BpmTaskTodoPageItemRespVO>> getTodoTaskPage(@Valid BpmTaskTodoPageReqVO pageVO) {
        return success(taskService.getTodoTaskPage(UserContext.getSessionuser().getUserId(), pageVO)).toHttpResult();
    }

    @GetMapping("done-page")
    @ApiOperation("获取 Done 已办任务分页")
    @SaCheckRole("task:see")
    public FhsPager<BpmTaskDonePageItemRespVO> getDoneTaskPage(@Valid BpmTaskDonePageReqVO pageVO) {
        return taskService.getDoneTaskPage(UserContext.getSessionuser().getUserId(), pageVO).toFhsPager();
    }

    @GetMapping("/list-by-process-instance-id")
    @ApiOperation(value = "获得指定流程实例的任务列表", notes = "包括完成的、未完成的")
    @ApiImplicitParam(name = "processInstanceId", value = "流程实例的编号", required = true)
    @SaCheckRole("task:see")
    public List<BpmTaskRespVO> getTaskListByProcessInstanceId(
        @RequestParam("processInstanceId") String processInstanceId) {
        return taskService.getTaskListByProcessInstanceId(processInstanceId);
    }

    @PutMapping("/approve")
    @ApiOperation("通过任务")
    @SaCheckRole("task:update")
    public HttpResult<Boolean> approveTask(@Valid @RequestBody BpmTaskApproveReqVO reqVO) {
        taskService.approveTask(UserContext.getSessionuser().getUserId(), reqVO);
        return success(true).toHttpResult();
    }

    @PutMapping("/reject")
    @ApiOperation("不通过任务")
    @SaCheckRole("task:update")
    public HttpResult<Boolean> rejectTask(@Valid @RequestBody BpmTaskRejectReqVO reqVO) {
        taskService.rejectTask(UserContext.getSessionuser().getUserId(), reqVO);
        return success(true).toHttpResult();
    }

    @PutMapping("/update-assignee")
    @ApiOperation(value = "更新任务的负责人", notes = "用于【流程详情】的【转派】按钮")
    @SaCheckRole("task:update")
    public HttpResult<Boolean> updateTaskAssignee(@Valid @RequestBody BpmTaskUpdateAssigneeReqVO reqVO) {
        taskService.updateTaskAssignee(UserContext.getSessionuser().getUserId(), reqVO);
        return success(true).toHttpResult();
    }

}
