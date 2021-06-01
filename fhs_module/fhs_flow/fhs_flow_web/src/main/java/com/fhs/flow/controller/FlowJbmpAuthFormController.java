package com.fhs.flow.controller;

import com.fhs.core.result.HttpResult;
import com.fhs.flow.dox.FlowJbmpAuthFormDO;
import com.fhs.flow.service.FlowCoreService;
import com.fhs.flow.vo.FlowJbmpAuthFormVO;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;

/**
 * 自定义审批表单(FlowJbmpAuthForm)表控制层
 *
 * @author wanglei
 * @since 2021-06-01 15:03:49
 */

@RestController
@Api(tags = {"自定义审批表单"})
@RequestMapping("/ms/flowJbmpAuthForm")
public class FlowJbmpAuthFormController extends ModelSuperController<FlowJbmpAuthFormVO, FlowJbmpAuthFormDO> {


    @Autowired
    private FlowCoreService flowCoreService;

    @GetMapping("findByTaskId")
    @ApiOperation("根据taskid获取自定义表单配置")
    public HttpResult<FlowJbmpAuthFormVO> findByTaskId(String taskId) {
        TaskEntity taskEntity = flowCoreService.findTaskById(taskId);
        if (taskEntity != null) {
            return HttpResult.success(super.getBaseService().findBean(FlowJbmpAuthFormDO.builder()
                    .taskKey(taskEntity.getTaskDefinitionKey()).build()));

        }
        return HttpResult.error(null);
    }
}
