package com.fhs.flow.controller;

import com.fhs.bislogger.api.anno.LogMethod;
import com.fhs.bislogger.api.anno.LogNamespace;
import com.fhs.bislogger.constant.LoggerConstant;
import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.StringUtil;
import com.fhs.core.base.pojo.pager.Pager;
import com.fhs.core.result.HttpResult;
import com.fhs.core.valid.checker.ParamChecker;
import com.fhs.flow.dox.FlowInstanceDO;
import com.fhs.flow.service.FlowInstanceService;
import com.fhs.flow.vo.FlowInstanceVO;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * fhs的流程实例，为activiti的实例扩展表(FlowInstance)表控制层
 *
 * @author jackwong
 * @since 2019-11-11 19:40:44
 */
@RestController
@Api(tags = {"流程实例"})
@RequestMapping("/ms/flow_instance")
@LogNamespace(namespace = "flow_instance",module = "流程实例管理")
public class FlowInstanceController extends ModelSuperController<FlowInstanceVO, FlowInstanceDO> {

    @Autowired
    private FlowInstanceService flowInstanceService;
    @Autowired
    private TaskService taskService;

    /**
     * 是否执行完毕
     */
    @RequestMapping("isRevokeApply")
    @LogMethod
    public HttpResult<Boolean> isRevokeApply(String instanceId, HttpServletRequest request,String taskId) throws Exception {
        ParamChecker.isNotNullOrEmpty(instanceId, "流程实例id不能为空");
        FlowInstanceVO flowInstance = new FlowInstanceVO();
        flowInstance.setActivitiProcessInstanceId(instanceId);
        FlowInstanceVO instance = flowInstanceService.selectBean(flowInstance);
        String userId = super.getSessionuser().getUserId();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String taskDefinitionKey = null;
        if (task!=null){
            taskDefinitionKey = task.getTaskDefinitionKey();
        }
        if (instance.getCreateUser().equals(userId) && instance.getFirstDefinitionKey().equals(taskDefinitionKey)) {
            return HttpResult.success(true);
        }
        return HttpResult.success(false);
    }

    @Override
    public Pager<FlowInstanceVO> findPage(FlowInstanceVO e, HttpServletRequest request, HttpServletResponse response) throws Exception {
        e.setCcTo(super.getSessionuser().getUserId());
        return super.findPage(e, request, response);
    }

    /**
     *  更新抄送人
     * @param ccTo 抄送人
     * @return
     */
    @RequestMapping("updateCCTo")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE,desc = "更新抄送人")
    public HttpResult updateCCTo(String ccTo, String instanceId) {
        ParamChecker.isNotNull(ccTo, "抄送人不能为空");
        ParamChecker.isNotNull(instanceId, "instanceId不能为空");
        FlowInstanceVO instanceVO = this.flowInstanceService.selectBean(FlowInstanceDO.builder().activitiProcessInstanceId(instanceId).build());
        ParamChecker.isNotNull(instanceVO, "instanceId无效");
        if(CheckUtils.isNullOrEmpty(instanceVO.getCcTo())){
            instanceVO.setCcTo(ccTo);
        }else{
            Set<String> ccToSet = new HashSet<>(Arrays.asList(instanceVO.getCcTo().split(",")));
            ccToSet.addAll(Arrays.asList(ccTo.split(",")));
            instanceVO.setCcTo(StringUtil.getStrForIn(ccToSet,false));
        }
        flowInstanceService.updateJpa(instanceVO);
        return HttpResult.success();
    }


}