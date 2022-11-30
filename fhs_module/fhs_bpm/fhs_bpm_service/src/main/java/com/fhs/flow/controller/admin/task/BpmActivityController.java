package com.fhs.flow.controller.admin.task;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.fhs.flow.controller.admin.task.vo.activity.BpmActivityRespVO;
import com.fhs.flow.service.task.BpmActivityService;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "管理后台 - 流程活动实例")
@RestController
@RequestMapping("/ms/activity")
@ApiGroup(group ="bpm")
@Validated
public class BpmActivityController {

    @Resource
    private BpmActivityService activityService;

    @GetMapping("/list")
    @ApiOperation(value = "生成指定流程实例的高亮流程图",
            notes = "只高亮进行中的任务。不过要注意，该接口暂时没用，通过前端的 ProcessViewer.vue 界面的 highlightDiagram 方法生成")
    @ApiImplicitParam(name = "processInstanceId", value = "流程实例的编号", required = true)
    @SaCheckRole("task:query")
    public List<BpmActivityRespVO> getActivityList(
            @RequestParam("processInstanceId") String processInstanceId) {
        return activityService.getActivityListByProcessInstanceId(processInstanceId);
    }
}
