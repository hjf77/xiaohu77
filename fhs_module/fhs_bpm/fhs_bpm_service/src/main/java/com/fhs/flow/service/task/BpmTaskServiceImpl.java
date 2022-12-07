package com.fhs.flow.service.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fhs.basics.context.UserContext;
import com.fhs.basics.service.UcenterMsOrganizationService;
import com.fhs.basics.service.UcenterMsUserService;
import com.fhs.basics.vo.UcenterMsOrganizationVO;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.flow.comon.constants.FlowableConstant;
import com.fhs.flow.comon.pojo.PageResult;
import com.fhs.flow.comon.utils.DateUtils;
import com.fhs.flow.comon.utils.PageUtils;
import com.fhs.flow.controller.admin.task.cmd.AddHisCommentCmd;
import com.fhs.flow.controller.admin.task.vo.task.*;
import com.fhs.flow.convert.task.BpmTaskConvert;
import com.fhs.flow.dal.dataobject.task.BpmTaskExtPO;
import com.fhs.flow.dal.mysql.task.BpmTaskExtMapper;
import com.fhs.flow.dal.mysql.task.HisFlowableActinstMapper;
import com.fhs.flow.dal.mysql.task.RunFlowableActinstMapper;
import com.fhs.flow.enums.task.BpmProcessInstanceDeleteReasonEnum;
import com.fhs.flow.enums.task.BpmProcessInstanceResultEnum;
import com.fhs.flow.service.message.BpmMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.persistence.entity.ActivityInstanceEntity;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.idm.api.User;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.fhs.flow.comon.exception.util.ServiceExceptionUtil.exception;
import static com.fhs.flow.comon.utils.CollectionUtils.convertMap;
import static com.fhs.flow.comon.utils.CollectionUtils.convertSet;
import static com.fhs.flow.enums.ErrorCodeConstants.*;

/**
 * 流程任务实例 Service 实现类
 *
 * @author 芋道源码
 * @author jason
 */
@Slf4j
@Service
public class BpmTaskServiceImpl implements BpmTaskService {

    @Resource
    private TaskService taskService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private IdentityService identityService;
    @Resource
    private ManagementService managementService;
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RunFlowableActinstMapper flowableActinstMapper;
    @Resource
    private HisFlowableActinstMapper hisFlowableActinstMapper;
    @Resource
    private HistoryService historyService;
    @Resource
    private BpmProcessInstanceService processInstanceService;
    @Resource
    private UcenterMsUserService adminUserApi;
    @Resource
    private UcenterMsOrganizationService deptApi;
    @Resource
    private BpmTaskExtMapper taskExtMapper;
    @Resource
    private BpmMessageService messageService;


    @Override
    public PageResult<BpmTaskTodoPageItemRespVO> getTodoTaskPage(Long userId, BpmTaskTodoPageReqVO pageVO) {
        // 查询待办任务
        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(String.valueOf(userId)) // 分配给自己
                .orderByTaskCreateTime().desc(); // 创建时间倒序
        if (StrUtil.isNotBlank(pageVO.getName())) {
            taskQuery.taskNameLike("%" + pageVO.getName() + "%");
        }
        if (pageVO.getCreateTime() != null) {
            taskQuery.taskCreatedAfter(DateUtils.of(pageVO.getCreateTime()[0]));
            taskQuery.taskCreatedBefore(DateUtils.of(pageVO.getCreateTime()[1]));
        }
        // 执行查询
        List<Task> tasks = taskQuery.listPage(PageUtils.getStart(pageVO), pageVO.getPageSize());
        if (CollUtil.isEmpty(tasks)) {
            return PageResult.empty(taskQuery.count());
        }

        // 获得 ProcessInstance Map
        Map<String, ProcessInstance> processInstanceMap =
                processInstanceService.getProcessInstanceMap(convertSet(tasks, Task::getProcessInstanceId));
        // 获得 User Map
        Map<Long, UcenterMsUserVO> userMap = adminUserApi.selectBatchIdsMP(
                        convertSet(processInstanceMap.values(), instance -> Long.valueOf(instance.getStartUserId()))).stream()
                .collect(Collectors.toMap(UcenterMsUserVO::getUserId, v -> v));
        // 拼接结果
        return new PageResult<>(BpmTaskConvert.INSTANCE.convertList1(tasks, processInstanceMap, userMap),
                taskQuery.count());
    }

    @Override
    public PageResult<BpmTaskDonePageItemRespVO> getDoneTaskPage(Long userId, BpmTaskDonePageReqVO pageVO) {
        // 查询已办任务
        HistoricTaskInstanceQuery taskQuery = historyService.createHistoricTaskInstanceQuery().finished() // 已完成
                .taskAssignee(String.valueOf(userId)) // 分配给自己
                .orderByHistoricTaskInstanceEndTime().desc(); // 审批时间倒序
        if (StrUtil.isNotBlank(pageVO.getName())) {
            taskQuery.taskNameLike("%" + pageVO.getName() + "%");
        }
        if (pageVO.getCreateTime() != null) {
            taskQuery.taskCreatedAfter(DateUtils.of(pageVO.getCreateTime()[0]));
            taskQuery.taskCreatedBefore(DateUtils.of(pageVO.getCreateTime()[1]));
        }
        // 执行查询
        List<HistoricTaskInstance> tasks = taskQuery.listPage(PageUtils.getStart(pageVO), pageVO.getPageSize());
        if (CollUtil.isEmpty(tasks)) {
            return PageResult.empty(taskQuery.count());
        }

        // 获得 TaskExtDO Map
        List<BpmTaskExtPO> BpmTaskExtPOs =
                taskExtMapper.selectListByTaskIds(convertSet(tasks, HistoricTaskInstance::getId));
        Map<String, BpmTaskExtPO> BpmTaskExtPOMap = convertMap(BpmTaskExtPOs, BpmTaskExtPO::getTaskId);
        // 获得 ProcessInstance Map
        Map<String, HistoricProcessInstance> historicProcessInstanceMap =
                processInstanceService.getHistoricProcessInstanceMap(
                        convertSet(tasks, HistoricTaskInstance::getProcessInstanceId));
        // 获得 User Map
        Map<Long, UcenterMsUserVO> userMap = adminUserApi.selectBatchIdsMP(
                        convertSet(historicProcessInstanceMap.values(), instance -> Long.valueOf(instance.getStartUserId()))).stream()
                .collect(Collectors.toMap(UcenterMsUserVO::getUserId, v -> v));
        // 拼接结果
        return new PageResult<>(
                BpmTaskConvert.INSTANCE.convertList2(tasks, BpmTaskExtPOMap, historicProcessInstanceMap, userMap),
                taskQuery.count());
    }

    @Override
    public List<Task> getTasksByProcessInstanceIds(List<String> processInstanceIds) {
        if (CollUtil.isEmpty(processInstanceIds)) {
            return Collections.emptyList();
        }
        return taskService.createTaskQuery().processInstanceIdIn(processInstanceIds).list();
    }

    @Override
    public List<BpmTaskRespVO> getTaskListByProcessInstanceId(String processInstanceId) {
        // 获得任务列表
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceStartTime().desc() // 创建时间倒序
                .list();
        if (CollUtil.isEmpty(tasks)) {
            return Collections.emptyList();
        }

        // 获得 TaskExtDO Map
        List<BpmTaskExtPO> BpmTaskExtPOs = taskExtMapper.selectListByTaskIds(convertSet(tasks, HistoricTaskInstance::getId));
        Map<String, BpmTaskExtPO> BpmTaskExtPOMap = convertMap(BpmTaskExtPOs, BpmTaskExtPO::getTaskId);
        // 获得 ProcessInstance Map
        HistoricProcessInstance processInstance = processInstanceService.getHistoricProcessInstance(processInstanceId);
        // 获得 User Map
        Set<Long> userIds = convertSet(tasks, task -> ConverterUtils.toLong(task.getAssignee()));
        userIds.add(ConverterUtils.toLong(processInstance.getStartUserId()));
        Map<Long, UcenterMsUserVO> userMap = adminUserApi.selectBatchIdsMP(userIds).stream()
                .collect(Collectors.toMap(UcenterMsUserVO::getUserId, v -> v));
        List<UcenterMsOrganizationVO> organizationVOS = deptApi.selectBatchIdsMP(userMap.values().stream().map(UcenterMsUserVO::getOrganizationId).collect(Collectors.toSet()));
        // 获得 Dept Map
        Map<String, UcenterMsOrganizationVO> deptMap = organizationVOS.stream().collect(Collectors.toMap(UcenterMsOrganizationVO::getId, v -> v));

        // 拼接数据
        return BpmTaskConvert.INSTANCE.convertList3(tasks, BpmTaskExtPOMap, processInstance, userMap, deptMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveTask(Long userId, @Valid BpmTaskApproveReqVO reqVO) {
        // 校验任务存在
        Task task = checkTask(userId, reqVO.getId());
        // 校验流程实例存在
        ProcessInstance instance = processInstanceService.getProcessInstance(task.getProcessInstanceId());
        if (instance == null) {
            throw exception(PROCESS_INSTANCE_NOT_EXISTS);
        }

        // 完成任务，审批通过
        taskService.complete(task.getId(), instance.getProcessVariables());
        BpmTaskExtPO toUpdate = new BpmTaskExtPO();
        toUpdate.setTaskId(task.getId());
        toUpdate.setResult(BpmProcessInstanceResultEnum.APPROVE.getResult());
        toUpdate.setReason(reqVO.getReason());
        // 更新任务拓展表为通过
        taskExtMapper.updateByTaskId(toUpdate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectTask(Long userId, @Valid BpmTaskRejectReqVO reqVO) {
        Task task = checkTask(userId, reqVO.getId());
        // 校验流程实例存在
        ProcessInstance instance = processInstanceService.getProcessInstance(task.getProcessInstanceId());
        if (instance == null) {
            throw exception(PROCESS_INSTANCE_NOT_EXISTS);
        }

        // 更新流程实例为不通过
        processInstanceService.updateProcessInstanceExtReject(instance.getProcessInstanceId(), reqVO.getReason());
        BpmTaskExtPO toUpdate = new BpmTaskExtPO();
        toUpdate.setTaskId(task.getId());
        toUpdate.setResult(BpmProcessInstanceResultEnum.REJECT.getResult());
        toUpdate.setEndTime(LocalDateTime.now());
        toUpdate.setReason(reqVO.getReason());
        // 更新任务拓展表为不通过
        taskExtMapper.updateByTaskId(
                new BpmTaskExtPO().setTaskId(task.getId()).setResult(BpmProcessInstanceResultEnum.REJECT.getResult())
                        .setEndTime(LocalDateTime.now()).setReason(reqVO.getReason()));
    }

    @Override
    public void updateTaskAssignee(Long userId, BpmTaskUpdateAssigneeReqVO reqVO) {
        // 校验任务存在
        Task task = checkTask(userId, reqVO.getId());
        // 更新负责人
        updateTaskAssignee(task.getId(), reqVO.getAssigneeUserId());
    }

    @Override
    public void updateTaskAssignee(String id, Long userId) {
        taskService.setAssignee(id, String.valueOf(userId));
    }

    /**
     * 校验任务是否存在， 并且是否是分配给自己的任务
     *
     * @param userId 用户 id
     * @param taskId task id
     */
    private Task checkTask(Long userId, String taskId) {
        Task task = getTask(taskId);
        if (task == null) {
            throw exception(TASK_COMPLETE_FAIL_NOT_EXISTS);
        }
        if (!Objects.equals(userId, ConverterUtils.toLong(task.getAssignee()))) {
            throw exception(TASK_COMPLETE_FAIL_ASSIGN_NOT_SELF);
        }
        return task;
    }

    @Override
    public void createTaskExt(Task task) {
        BpmTaskExtPO taskExtDO =
                BpmTaskConvert.INSTANCE.convert2TaskExt(task).setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        taskExtMapper.insert(taskExtDO);
        if (FlowableConstant.FLOW_SUBMITTER.equals(task.getName())) {

            // 如果是提交步骤就自动通过
            taskExtDO.setResult(BpmProcessInstanceResultEnum.APPROVE.getResult());
            taskExtDO.setReason("提交");
            // 通过任务
            HistoricProcessInstance hi = SpringUtil.getBean(HistoryService.class).createHistoricProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult();
            if (StringUtils.isEmpty(hi.getStartUserId())) {
                return;
            }
            // 设置提交人
            taskExtDO.setAssigneeUserId(Long.valueOf(hi.getStartUserId()));
            BpmTaskApproveReqVO reqVo = new BpmTaskApproveReqVO();
            reqVo.setId(task.getId());
            taskService.setAssignee(taskExtDO.getTaskId(),taskExtDO.getAssigneeUserId().toString());
            this.approveTask(taskExtDO.getAssigneeUserId(), reqVo);
        }
    }

    @Override
    public void updateTaskExtComplete(Task task) {
        BpmTaskExtPO taskExtDO = BpmTaskConvert.INSTANCE.convert2TaskExt(task)
                .setResult(BpmProcessInstanceResultEnum.APPROVE.getResult()) // 不设置也问题不大，因为 Complete 一般是审核通过，已经设置
                .setEndTime(LocalDateTime.now());
        taskExtMapper.updateByTaskId(taskExtDO);
    }

    @Override
    public void updateTaskExtCancel(String taskId) {
        // 需要在事务提交后，才进行查询。不然查询不到历史的原因
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
                // 可能只是活动，不是任务，所以查询不到
                HistoricTaskInstance task = getHistoricTask(taskId);
                if (task == null) {
                    return;
                }

                // 如果任务拓展表已经是完成的状态，则跳过
                BpmTaskExtPO taskExt = taskExtMapper.selectByTaskId(taskId);
                if (taskExt == null) {
                    log.error("[updateTaskExtCancel][taskId({}) 查找不到对应的记录，可能存在问题]", taskId);
                    return;
                }
                // 如果已经是最终的结果，则跳过
                if (BpmProcessInstanceResultEnum.isEndResult(taskExt.getResult())) {
                    log.error("[updateTaskExtCancel][taskId({}) 处于结果({})，无需进行更新]", taskId, taskExt.getResult());
                    return;
                }

                // 更新任务
                taskExtMapper.updateById(new BpmTaskExtPO().setId(taskExt.getId()).setResult(BpmProcessInstanceResultEnum.CANCEL.getResult())
                        .setEndTime(LocalDateTime.now()).setReason(BpmProcessInstanceDeleteReasonEnum.translateReason(task.getDeleteReason())));
            }

        });
    }

    @Override
    public void updateTaskExtAssign(Task task) {
        BpmTaskExtPO taskExtDO =
                new BpmTaskExtPO().setAssigneeUserId(ConverterUtils.toLong(task.getAssignee())).setTaskId(task.getId());
        taskExtMapper.updateByTaskId(taskExtDO);
        // 发送通知。在事务提交时，批量执行操作，所以直接查询会无法查询到 ProcessInstance，所以这里是通过监听事务的提交来实现。
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                ProcessInstance processInstance =
                        processInstanceService.getProcessInstance(task.getProcessInstanceId());
                UcenterMsUserVO startUser = adminUserApi.selectById(Long.valueOf(processInstance.getStartUserId()));
                messageService.sendMessageWhenTaskAssigned(
                        BpmTaskConvert.INSTANCE.convert(processInstance, startUser, task));
            }
        });
    }

    private Task getTask(String id) {
        return taskService.createTaskQuery().taskId(id).singleResult();
    }

    private HistoricTaskInstance getHistoricTask(String id) {
        return historyService.createHistoricTaskInstanceQuery().taskId(id).singleResult();
    }


    @Override
    public List<FlowNodeVo> getBackNodesByProcessInstanceId(String taskId, String processInstanceId) {
        List<FlowNodeVo> backNods = new ArrayList<>();
        TaskEntity taskEntity = (TaskEntity) checkTask(UserContext.getSessionuser().getUserId(), taskId);
        String currActId = taskEntity.getTaskDefinitionKey();
        //获取运行节点表中usertask
        String sql = "select t.* from act_ru_actinst t where t.ACT_TYPE_ = 'userTask' " +
                " and t.PROC_INST_ID_=#{processInstanceId} and t.END_TIME_ is not null ";
        List<ActivityInstance> activityInstances = runtimeService.createNativeActivityInstanceQuery().sql(sql)
                .parameter("processInstanceId", processInstanceId)
                .list();
        //获取运行节点表的parallelGateway节点并出重
        sql = "SELECT t.ID_, t.REV_,t.PROC_DEF_ID_,t.PROC_INST_ID_,t.EXECUTION_ID_,t.ACT_ID_, t.TASK_ID_, t.CALL_PROC_INST_ID_, t.ACT_NAME_, t.ACT_TYPE_, " +
                " t.ASSIGNEE_, t.START_TIME_, max(t.END_TIME_) as END_TIME_, t.DURATION_, t.DELETE_REASON_, t.TENANT_ID_" +
                " FROM  act_ru_actinst t WHERE t.ACT_TYPE_ = 'parallelGateway' AND t.PROC_INST_ID_ = #{processInstanceId} and t.END_TIME_ is not null" +
                " and t.ACT_ID_ <> #{actId} GROUP BY t.act_id_";
        List<ActivityInstance> parallelGatewaies = runtimeService.createNativeActivityInstanceQuery().sql(sql)
                .parameter("processInstanceId", processInstanceId)
                .parameter("actId", currActId)
                .list();
        //排序
        if (CollectionUtils.isNotEmpty(parallelGatewaies)) {
            activityInstances.addAll(parallelGatewaies);
            activityInstances.sort(Comparator.comparing(ActivityInstance::getEndTime));
        }
        //分组节点
        int count = 0;
        Map<ActivityInstance, List<ActivityInstance>> parallelGatewayUserTasks = new HashMap<>();
        List<ActivityInstance> userTasks = new ArrayList<>();
        ActivityInstance currActivityInstance = null;
        for (ActivityInstance activityInstance : activityInstances) {
            if (BpmnXMLConstants.ELEMENT_GATEWAY_PARALLEL.equals(activityInstance.getActivityType())) {
                count++;
                if (count % 2 != 0) {
                    List<ActivityInstance> datas = new ArrayList<>();
                    currActivityInstance = activityInstance;
                    parallelGatewayUserTasks.put(currActivityInstance, datas);
                }
            }
            if (BpmnXMLConstants.ELEMENT_TASK_USER.equals(activityInstance.getActivityType())) {
                if (count % 2 == 0) {
                    userTasks.add(activityInstance);
                } else {
                    if (parallelGatewayUserTasks.containsKey(currActivityInstance)) {
                        parallelGatewayUserTasks.get(currActivityInstance).add(activityInstance);
                    }
                }
            }
        }
        //组装人员名称
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).finished().list();
        Map<String, List<HistoricTaskInstance>> taskInstanceMap = new HashMap<>();
        List<String> userCodes = new ArrayList<>();
        historicTaskInstances.forEach(historicTaskInstance -> {
            userCodes.add(historicTaskInstance.getAssignee());
            String taskDefinitionKey = historicTaskInstance.getTaskDefinitionKey();
            if (taskInstanceMap.containsKey(historicTaskInstance.getTaskDefinitionKey())) {
                taskInstanceMap.get(taskDefinitionKey).add(historicTaskInstance);
            } else {
                List<HistoricTaskInstance> tasks = new ArrayList<>();
                tasks.add(historicTaskInstance);
                taskInstanceMap.put(taskDefinitionKey, tasks);
            }
        });
        //组装usertask的数据
        List<UcenterMsUserVO> userList = adminUserApi.selectBatchIdsMP(userCodes);
        Map<String, String> activityIdUserNames = this.getApplyers(processInstanceId, userList, taskInstanceMap);
        if (CollectionUtils.isNotEmpty(userTasks)) {
            userTasks.forEach(activityInstance -> {
                FlowNodeVo node = new FlowNodeVo();
                node.setDefinitionKey(activityInstance.getActivityId());
                node.setDefinitionName(activityInstance.getActivityName());
                node.setEndTime(activityInstance.getEndTime());
                node.setUserName(activityIdUserNames.get(activityInstance.getActivityId()));
                backNods.add(node);
            });
        }
        //组装会签节点数据
        if (MapUtils.isNotEmpty(taskInstanceMap)) {
            parallelGatewayUserTasks.forEach((activity, activities) -> {
                FlowNodeVo node = new FlowNodeVo();
                node.setDefinitionKey(activity.getActivityId());
                node.setEndTime(activity.getEndTime());
                StringBuffer nodeNames = new StringBuffer("会签:");
                StringBuffer userNames = new StringBuffer("审批人员:");
                if (CollectionUtils.isNotEmpty(activities)) {
                    activities.forEach(activityInstance -> {
                        nodeNames.append(activityInstance.getActivityName()).append(",");
                        userNames.append(activityIdUserNames.get(activityInstance.getActivityId())).append(",");
                    });
                    node.setDefinitionName(nodeNames.toString());
                    node.setUserName(userNames.toString());
                    backNods.add(node);
                }
            });
        }
        //去重合并
        List<FlowNodeVo> datas = backNods.stream().collect(
                Collectors.collectingAndThen(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(nodeVo -> nodeVo.getDefinitionKey()))), ArrayList::new));

        //排序
        datas.sort(Comparator.comparing(FlowNodeVo::getEndTime).reversed());
        return datas;
    }


    private Map<String, String> getApplyers(String processInstanceId, List<UcenterMsUserVO> userList, Map<String, List<HistoricTaskInstance>> taskInstanceMap) {
        Map<Long, UcenterMsUserVO> userMap = userList.stream().collect(Collectors.toMap(UcenterMsUserVO::getUserId, user -> user));
        Map<String, String> applyMap = new HashMap<>();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        taskInstanceMap.forEach((activityId, taskInstances) -> {
            StringBuffer applyers = new StringBuffer();
            StringBuffer finalApplyers = applyers;
            taskInstances.forEach(taskInstance -> {
                if (!taskInstance.getName().equals(FlowableConstant.FLOW_SUBMITTER)) {
                    UcenterMsUserVO user = userMap.get(Long.valueOf(taskInstance.getAssignee()));
                    if (user != null) {
                        if (StringUtils.indexOf(finalApplyers.toString(), user.getUserName()) == -1) {
                            finalApplyers.append(user.getUserName()).append(",");
                        }
                    }
                } else {
                    String startUserId = processInstance.getStartUserId();
                    User user = identityService.createUserQuery().userId(startUserId).singleResult();
                    if (user != null) {
                        finalApplyers.append(user.getDisplayName()).append(",");
                    }
                }
            });
            if (applyers.length() > 0) {
                applyers = applyers.deleteCharAt(applyers.length() - 1);
            }
            applyMap.put(activityId, applyers.toString());
        });
        return applyMap;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void backToStepTask(BackTaskVo backTaskVo) {
        TaskEntity taskEntity = (TaskEntity) checkTask(backTaskVo.getUserId(), backTaskVo.getTaskId());
        ProcessInstance instance = processInstanceService.getProcessInstance(taskEntity.getProcessInstanceId());
        if (instance == null) {
            throw exception(PROCESS_INSTANCE_NOT_EXISTS);
        }
        //1.把当前的节点设置为空
        //2.设置审批人
        taskEntity.setAssignee(backTaskVo.getUserId().toString());
        taskService.saveTask(taskEntity);
        //3.添加驳回意见
        managementService.executeCommand(new AddHisCommentCmd(backTaskVo.getTaskId(), backTaskVo.getUserId().toString(), backTaskVo.getProcessInstanceId(),
                BpmProcessInstanceResultEnum.BACK.getResult().toString(), backTaskVo.getReason()));
        //4.处理提交人节点
        FlowNode distActivity = this.findFlowNodeByActivityId(taskEntity.getProcessDefinitionId(), backTaskVo.getDefinitionKey());
        if (distActivity != null) {
            if (FlowableConstant.FLOW_SUBMITTER.equals(distActivity.getName())) {
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(taskEntity.getProcessInstanceId()).singleResult();
                runtimeService.setVariable(backTaskVo.getProcessInstanceId(), FlowableConstant.FLOW_SUBMITTER_VAR, processInstance.getStartUserId());
            }
        }
        //5.删除节点
        this.deleteActivity(backTaskVo.getDefinitionKey(), taskEntity.getProcessInstanceId());
        List<String> executionIds = new ArrayList<>();
        //6.判断节点是不是子流程内部的节点
        if (this.checkActivitySubprocessByActivityId(taskEntity.getProcessDefinitionId(),
                backTaskVo.getDefinitionKey())
                && this.checkActivitySubprocessByActivityId(taskEntity.getProcessDefinitionId(),
                taskEntity.getTaskDefinitionKey())) {
            //6.1 子流程内部驳回
            Execution executionTask = runtimeService.createExecutionQuery().executionId(taskEntity.getExecutionId()).singleResult();
            String parentId = executionTask.getParentId();
            List<Execution> executions = runtimeService.createExecutionQuery().parentId(parentId).list();
            executions.forEach(execution -> executionIds.add(execution.getId()));
            this.moveExecutionsToSingleActivityId(executionIds, backTaskVo.getDefinitionKey());
        } else {
            //6.2 普通驳回
            List<Execution> executions = runtimeService.createExecutionQuery().parentId(taskEntity.getProcessInstanceId()).list();
            executions.forEach(execution -> executionIds.add(execution.getId()));
            this.moveExecutionsToSingleActivityId(executionIds, backTaskVo.getDefinitionKey());
        }
        // 扩展表字段修改
        processInstanceService.updateProcessInstanceExtBACK(instance);
        BpmTaskExtPO toUpdate = new BpmTaskExtPO();
        toUpdate.setTaskId(taskEntity.getId());
        toUpdate.setResult(BpmProcessInstanceResultEnum.REJECT.getResult());
        toUpdate.setEndTime(LocalDateTime.now());
        toUpdate.setReason(backTaskVo.getReason());
        // 更新任务拓展表为驳回
        taskExtMapper.updateByTaskId(
                new BpmTaskExtPO().setTaskId(taskEntity.getId()).setResult(BpmProcessInstanceResultEnum.BACK.getResult())
                        .setEndTime(LocalDateTime.now()).setReason(backTaskVo.getReason()));
    }

    @Override
    public FlowNode findFlowNodeByActivityId(String processDefId, String activityId) {
        FlowNode activity = null;
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefId);
        List<Process> processes = bpmnModel.getProcesses();
        for (Process process : processes) {
            FlowElement flowElement = process.getFlowElementMap().get(activityId);
            if (flowElement != null) {
                activity = (FlowNode) flowElement;
                break;
            }
        }
        return activity;
    }

    /**
     * 删除跳转的历史节点信息
     *
     * @param disActivityId     跳转的节点id
     * @param processInstanceId 流程实例id
     */
    protected void deleteActivity(String disActivityId, String processInstanceId) {
        String tableName = managementService.getTableName(ActivityInstanceEntity.class);
        String sql = "select t.* from " + tableName + " t where t.PROC_INST_ID_=#{processInstanceId} and t.ACT_ID_ = #{disActivityId} " +
                " order by t.END_TIME_ ASC";
        List<ActivityInstance> disActivities = runtimeService.createNativeActivityInstanceQuery().sql(sql)
                .parameter("processInstanceId", processInstanceId)
                .parameter("disActivityId", disActivityId).list();
        //删除运行时和历史节点信息
        if (CollectionUtils.isNotEmpty(disActivities)) {
            ActivityInstance activityInstance = disActivities.get(0);
            sql = "select t.* from " + tableName + " t where t.PROC_INST_ID_=#{processInstanceId} and (t.END_TIME_ >= #{endTime} or t.END_TIME_ is null)";
            List<ActivityInstance> datas = runtimeService.createNativeActivityInstanceQuery().sql(sql).parameter("processInstanceId", processInstanceId)
                    .parameter("endTime", activityInstance.getEndTime()).list();
            List<String> runActivityIds = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(datas)) {
                datas.forEach(ai -> runActivityIds.add(ai.getId()));
                flowableActinstMapper.deleteRunActinstsByIds(runActivityIds);
                hisFlowableActinstMapper.deleteHisActinstsByIds(runActivityIds);
            }
        }
    }

    @Override
    public boolean checkActivitySubprocessByActivityId(String processDefId, String activityId) {
        boolean flag = true;
        List<FlowNode> activities = this.findFlowNodesByActivityId(processDefId, activityId);
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(activities)) {
            flag = false;
        }
        return flag;
    }

    public List<FlowNode> findFlowNodesByActivityId(String processDefId, String activityId) {
        List<FlowNode> activities = new ArrayList<>();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefId);
        List<Process> processes = bpmnModel.getProcesses();
        for (Process process : processes) {
            FlowElement flowElement = process.getFlowElement(activityId);
            if (flowElement != null) {
                FlowNode flowNode = (FlowNode) flowElement;
                activities.add(flowNode);
            }
        }
        return activities;
    }

    /**
     * 执行跳转
     */
    protected void moveExecutionsToSingleActivityId(List<String> executionIds, String activityId) {
        runtimeService.createChangeActivityStateBuilder()
                .moveExecutionsToSingleActivityId(executionIds, activityId)
                .changeState();
    }

}
