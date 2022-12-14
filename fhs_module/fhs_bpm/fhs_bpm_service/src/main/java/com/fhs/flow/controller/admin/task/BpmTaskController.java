package com.fhs.flow.controller.admin.task;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.fhs.basics.context.UserContext;
import com.fhs.common.utils.ClassUtils;
import com.fhs.core.base.vo.FhsPager;
import com.fhs.core.base.vo.QueryFilter;
import com.fhs.core.result.HttpResult;
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
@ApiGroup(group = "bpm")
@Validated
public class BpmTaskController {

    @Resource
    private BpmTaskService taskService;

    @PostMapping("todo-page")
    @ApiOperation("获取 Todo 待办任务分页")
    @SaCheckRole("task:see")
    public FhsPager<BpmTaskTodoPageItemRespVO> getTodoTaskPage(@RequestBody QueryFilter<?> queryFilter) {
        return taskService.getTodoTaskPage(UserContext.getSessionuser().getUserId(), ClassUtils.convert2Clz(queryFilter.queryFieldsMap(), BpmTaskTodoPageReqVO.class)).toFhsPager();
    }

    @GetMapping("todo-page-count")
    @ApiOperation("获取 Todo 待办任务数量")
    @SaCheckRole("task:see")
    public HttpResult<Long> getTodoTaskCount() {
        return HttpResult.success(taskService.getTodoTaskCount());
    }

    @PostMapping("done-page")
    @ApiOperation("获取 Done 已办任务分页")
    @SaCheckRole("task:see")
    public FhsPager<BpmTaskDonePageItemRespVO> getDoneTaskPage(@RequestBody QueryFilter<?> queryFilter) {
        return taskService.getDoneTaskPage(UserContext.getSessionuser().getUserId(), ClassUtils.convert2Clz(queryFilter.queryFieldsMap(), BpmTaskDonePageReqVO.class)).toFhsPager();
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

    /**
     * 获取可驳回节点列表
     *
     * @param processInstanceId 流程实例id
     * @return
     */
    @GetMapping(value = "/getBackNodesByProcessInstanceId")
    @ApiOperation(value = "获取可驳回节点列表", notes = "获取可驳回节点列表")
    public List<FlowNodeVo> getBackNodesByProcessInstanceId(
            @RequestParam("processInstanceId") String processInstanceId
            , @RequestParam("taskId") String taskId) {
        return taskService.getBackNodesByProcessInstanceId(taskId, processInstanceId);
    }


    /**
     * 驳回节点
     * @param params 参数
     * @return
     */
    @PostMapping(value = "/doBackStep")
    @ApiOperation(value = "驳回节点", notes = "驳回节点")
    public HttpResult<Boolean> doBackStep(@Valid @RequestBody BackTaskVo params) {
        params.setUserId(UserContext.getSessionuser().getUserId());
        taskService.backToStepTask(params);
        return success(true).toHttpResult();
    }
}
