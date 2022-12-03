package com.fhs.flow.service.definition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fhs.basics.po.UcenterMsRolePO;
import com.fhs.basics.po.UcenterMsUserPO;
import com.fhs.basics.service.ServiceDictItemService;
import com.fhs.basics.service.UcenterMsOrganizationService;
import com.fhs.basics.service.UcenterMsRoleService;
import com.fhs.basics.service.UcenterMsUserService;
import com.fhs.basics.vo.UcenterMsRoleVO;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.common.constant.Constant;
import com.fhs.flow.comon.constants.FlowableConstant;
import com.fhs.flow.controller.admin.definition.vo.rule.BpmTaskAssignRuleCreateReqVO;
import com.fhs.flow.controller.admin.definition.vo.rule.BpmTaskAssignRuleRespVO;
import com.fhs.flow.controller.admin.definition.vo.rule.BpmTaskAssignRuleUpdateReqVO;
import com.fhs.flow.convert.definition.BpmTaskAssignRuleConvert;
import com.fhs.flow.core.util.FlowableUtils;
import com.fhs.flow.dal.dataobject.definition.BpmTaskAssignRulePO;
import com.fhs.flow.dal.mysql.definition.BpmTaskAssignRuleMapper;
import com.fhs.flow.enums.definition.BpmTaskAssignRuleTypeEnum;
import com.fhs.flow.framework.flowable.core.behavior.script.BpmTaskAssignScript;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static cn.hutool.core.text.CharSequenceUtil.format;
import static com.fhs.flow.comon.exception.util.ServiceExceptionUtil.exception;
import static com.fhs.flow.comon.utils.CollectionUtils.convertMap;
import static com.fhs.flow.comon.utils.JsonUtils.toJsonString;
import static com.fhs.flow.enums.ErrorCodeConstants.*;

/**
 * BPM 任务分配规则 Service 实现类
 */
@Service
@Validated
@Slf4j
public class BpmTaskAssignRuleServiceImpl implements BpmTaskAssignRuleService {

    @Resource
    private BpmTaskAssignRuleMapper taskRuleMapper;
    @Resource
    @Lazy // 解决循环依赖
    private BpmModelService modelService;
    @Resource
    @Lazy // 解决循环依赖
    private BpmProcessDefinitionService processDefinitionService;
    @Resource
    private BpmUserGroupService userGroupService;
    @Resource
    private UcenterMsRoleService roleApi;
    @Resource
    private UcenterMsOrganizationService deptApi;

    @Resource
    private UcenterMsUserService adminUserApi;
    @Resource
    private ServiceDictItemService dictDataApi;
    /**
     * 任务分配脚本
     */
    private Map<Long, BpmTaskAssignScript> scriptMap = Collections.emptyMap();

    @Resource
    public void setScripts(List<BpmTaskAssignScript> scripts) {
        this.scriptMap = convertMap(scripts, script -> script.getEnum().getId());
    }

    @Override
    public List<BpmTaskAssignRulePO> getTaskAssignRuleListByProcessDefinitionId(String processDefinitionId,
                                                                                String taskDefinitionKey) {
        return taskRuleMapper.selectListByProcessDefinitionId(processDefinitionId, taskDefinitionKey);
    }

    @Override
    public List<BpmTaskAssignRulePO> getTaskAssignRuleListByModelId(String modelId) {
        return taskRuleMapper.selectListByModelId(modelId);
    }

    @Override
    public List<BpmTaskAssignRuleRespVO> getTaskAssignRuleList(String modelId, String processDefinitionId) {
        // 获得规则
        List<BpmTaskAssignRulePO> rules = Collections.emptyList();
        BpmnModel model = null;
        if (StrUtil.isNotEmpty(modelId)) {
            rules = getTaskAssignRuleListByModelId(modelId);
            model = modelService.getBpmnModel(modelId);
        } else if (StrUtil.isNotEmpty(processDefinitionId)) {
            rules = getTaskAssignRuleListByProcessDefinitionId(processDefinitionId, null);
            model = processDefinitionService.getBpmnModel(processDefinitionId);
        }
        if (model == null) {
            return Collections.emptyList();
        }
        // 获得用户任务，只有用户任务才可以设置分配规则
        List<UserTask> userTasks = FlowableUtils.getBpmnModelElements(model, UserTask.class);
        if (CollUtil.isEmpty(userTasks)) {
            return Collections.emptyList();
        }
        // 转换数据
        List<BpmTaskAssignRuleRespVO> result = BpmTaskAssignRuleConvert.INSTANCE.convertList(userTasks, rules);
        this.transOptions(result);
        return result;
    }

    /**
     * 根据分配规则的类型翻译角色、用户名
     *
     * @param result
     */
    private void transOptions(List<BpmTaskAssignRuleRespVO> result) {
        List<Long> optionsAll = new ArrayList<>();
        Map<Integer, List<BpmTaskAssignRuleRespVO>> ruleTypeMap = result.stream().filter(x->null != x.getType()).collect(Collectors.groupingBy(BpmTaskAssignRuleRespVO::getType));
        if (CollUtil.isEmpty(ruleTypeMap)) {
            return;
        }
        List<BpmTaskAssignRuleRespVO> typeList = ruleTypeMap.get(FlowableConstant.TaskAssignRuleType.ROLE);
        Map<Long, String> roleNameMap = null;
        if (CollUtil.isNotEmpty(typeList)) {
            // 角色翻译
            List<Long> finalOptionsAll = optionsAll;
            typeList.forEach(x -> finalOptionsAll.addAll(x.getOptions()));
            LambdaQueryWrapper<UcenterMsRolePO> roleWrapper = new LambdaQueryWrapper<>();
            roleWrapper.in(UcenterMsRolePO::getRoleId, optionsAll).eq(UcenterMsRolePO::getIsDelete, Constant.INT_FALSE);
            List<UcenterMsRoleVO> roleVos = roleApi.selectListMP(roleWrapper);
            roleNameMap = roleVos.stream().collect(Collectors.toMap(UcenterMsRoleVO::getRoleId, UcenterMsRoleVO::getRoleName));
        }
        typeList = ruleTypeMap.get(FlowableConstant.TaskAssignRuleType.USER);
        Map<Long, String> userNameMap = null;
        if (CollUtil.isNotEmpty(typeList)) {
            optionsAll = new ArrayList<>();
            // 用户名翻译
            List<Long> finalOptionsAll = optionsAll;
            typeList.forEach(x -> finalOptionsAll.addAll(x.getOptions()));
            LambdaQueryWrapper<UcenterMsUserPO> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.in(UcenterMsUserPO::getUserId, optionsAll).eq(UcenterMsUserPO::getIsDelete, Constant.INT_FALSE);
            List<UcenterMsUserVO> userVos = adminUserApi.selectListMP(userWrapper);
            userNameMap = userVos.stream().collect(Collectors.toMap(UcenterMsUserVO::getUserId, UcenterMsUserVO::getUserName));
        }
        List<String> optionNames;
        for (BpmTaskAssignRuleRespVO vo : result) {
            if (CollUtil.isEmpty(vo.getOptions())) {
                continue;
            }
            optionNames = new ArrayList<>(vo.getOptions().size());
            if (FlowableConstant.TaskAssignRuleType.ROLE.equals(vo.getType()) && null != roleNameMap) {
                for (Long option : vo.getOptions()) {
                    optionNames.add(roleNameMap.get(option));
                }
            }
            if (FlowableConstant.TaskAssignRuleType.USER.equals(vo.getType()) && null != userNameMap) {
                for (Long option : vo.getOptions()) {
                    optionNames.add(userNameMap.get(option));
                }
            }
            if (CollUtil.isNotEmpty(optionNames)) {
                vo.setOptionNames(String.join(",", optionNames));
            }
        }
    }

    @Override
    public Long createTaskAssignRule(@Valid BpmTaskAssignRuleCreateReqVO reqVO) {
        // 校验参数
        validTaskAssignRuleOptions(reqVO.getType(), reqVO.getOptions());
        // 校验是否已经配置
        BpmTaskAssignRulePO existRule =
                taskRuleMapper.selectListByModelIdAndTaskDefinitionKey(reqVO.getModelId(), reqVO.getTaskDefinitionKey());
        if (existRule != null) {
            throw exception(TASK_ASSIGN_RULE_EXISTS, reqVO.getModelId(), reqVO.getTaskDefinitionKey());
        }

        // 存储
        BpmTaskAssignRulePO rule = BpmTaskAssignRuleConvert.INSTANCE.convert(reqVO);
        rule.setProcessDefinitionId(""); // 只有流程模型，才允许新建
        taskRuleMapper.insert(rule);
        return rule.getId();
    }

    @Override
    public void updateTaskAssignRule(@Valid BpmTaskAssignRuleUpdateReqVO reqVO) {
        // 校验参数
        validTaskAssignRuleOptions(reqVO.getType(), reqVO.getOptions());
        // 校验是否存在
        BpmTaskAssignRulePO existRule = taskRuleMapper.selectById(reqVO.getId());
        if (existRule == null) {
            throw exception(TASK_ASSIGN_RULE_NOT_EXISTS);
        }
        // 只允许修改流程模型的规则
        if (!Objects.equals("", existRule.getProcessDefinitionId())) {
            throw exception(TASK_UPDATE_FAIL_NOT_MODEL);
        }

        // 执行更新
        taskRuleMapper.updateById(BpmTaskAssignRuleConvert.INSTANCE.convert(reqVO));
    }

    @Override
    public boolean isTaskAssignRulesEquals(String modelId, String processDefinitionId) {
        // 调用 VO 接口的原因是，过滤掉流程模型不需要的规则，保持和 copyTaskAssignRules 方法的一致性
        List<BpmTaskAssignRuleRespVO> modelRules = getTaskAssignRuleList(modelId, null);
        List<BpmTaskAssignRuleRespVO> processInstanceRules = getTaskAssignRuleList(null, processDefinitionId);
        if (modelRules.size() != processInstanceRules.size()) {
            return false;
        }

        // 遍历，匹配对应的规则
        Map<String, BpmTaskAssignRuleRespVO> processInstanceRuleMap =
                convertMap(processInstanceRules, BpmTaskAssignRuleRespVO::getTaskDefinitionKey);
        for (BpmTaskAssignRuleRespVO modelRule : modelRules) {
            BpmTaskAssignRuleRespVO processInstanceRule = processInstanceRuleMap.get(modelRule.getTaskDefinitionKey());
            if (processInstanceRule == null) {
                return false;
            }
            if (!ObjectUtil.equals(modelRule.getType(), processInstanceRule.getType()) || !ObjectUtil.equal(
                    modelRule.getOptions(), processInstanceRule.getOptions())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void copyTaskAssignRules(String fromModelId, String toProcessDefinitionId) {
        List<BpmTaskAssignRuleRespVO> rules = getTaskAssignRuleList(fromModelId, null);
        if (CollUtil.isEmpty(rules)) {
            return;
        }
        // 开始复制
        List<BpmTaskAssignRulePO> newRules = BpmTaskAssignRuleConvert.INSTANCE.convertList2(rules);
        newRules.forEach(rule -> {
            rule.setProcessDefinitionId(toProcessDefinitionId);
            rule.setId(null);
            rule.setCreateTime(null);
            rule.setUpdateTime(null);
        });
        taskRuleMapper.insertBatchX(newRules);
    }

    @Override
    public void checkTaskAssignRuleAllConfig(String id) {
        // 一个用户任务都没配置，所以无需配置规则
        List<BpmTaskAssignRuleRespVO> taskAssignRules = getTaskAssignRuleList(id, null);
        if (CollUtil.isEmpty(taskAssignRules)) {
            return;
        }
        // 校验未配置规则的任务
        taskAssignRules.forEach(rule -> {
            if (CollUtil.isEmpty(rule.getOptions())) {
                throw exception(MODEL_DEPLOY_FAIL_TASK_ASSIGN_RULE_NOT_CONFIG, rule.getTaskDefinitionName());
            }
        });
    }

    private void validTaskAssignRuleOptions(Integer type, Set<Long> options) {
       /* if (Objects.equals(type, BpmTaskAssignRuleTypeEnum.ROLE.getType())) {
            roleApi.validRoles(options);
        } else if (ObjectUtils.equalsAny(type, BpmTaskAssignRuleTypeEnum.DEPT_MEMBER.getType(),
                BpmTaskAssignRuleTypeEnum.DEPT_LEADER.getType())) {
            deptApi.validDepts(options);
        } else if (Objects.equals(type, BpmTaskAssignRuleTypeEnum.POST.getType())) {
            postApi.validPosts(options);
        } else if (Objects.equals(type, BpmTaskAssignRuleTypeEnum.USER.getType())) {
            adminUserApi.validUsers(options);
        } else if (Objects.equals(type, BpmTaskAssignRuleTypeEnum.USER_GROUP.getType())) {
            userGroupService.validUserGroups(options);
        } else if (Objects.equals(type, BpmTaskAssignRuleTypeEnum.SCRIPT.getType())) {
            dictDataApi.validDictDatas(DictTypeConstants.TASK_ASSIGN_SCRIPT,
                    CollectionUtils.convertSet(options, String::valueOf));
        } else {
            throw new IllegalArgumentException(format("未知的规则类型({})", type));
        }*/
    }

    @Override
    public Set<Long> calculateTaskCandidateUsers(DelegateExecution execution) {
        BpmTaskAssignRulePO rule = getTaskRule(execution);
        return calculateTaskCandidateUsers(execution, rule);
    }

    @VisibleForTesting
    BpmTaskAssignRulePO getTaskRule(DelegateExecution execution) {
        List<BpmTaskAssignRulePO> taskRules = getTaskAssignRuleListByProcessDefinitionId(
                execution.getProcessDefinitionId(), execution.getCurrentActivityId());
        if (CollUtil.isEmpty(taskRules)) {
            throw new FlowableException(format("流程任务({}/{}/{}) 找不到符合的任务规则",
                    execution.getId(), execution.getProcessDefinitionId(), execution.getCurrentActivityId()));
        }
        if (taskRules.size() > 1) {
            throw new FlowableException(format("流程任务({}/{}/{}) 找到过多任务规则({})",
                    execution.getId(), execution.getProcessDefinitionId(), execution.getCurrentActivityId()));
        }
        return taskRules.get(0);
    }

    @VisibleForTesting
    Set<Long> calculateTaskCandidateUsers(DelegateExecution execution, BpmTaskAssignRulePO rule) {
        Set<Long> assigneeUserIds = null;
        if (Objects.equals(BpmTaskAssignRuleTypeEnum.ROLE.getType(), rule.getType())) {
            assigneeUserIds = calculateTaskCandidateUsersByRole(rule);
        } else if (Objects.equals(BpmTaskAssignRuleTypeEnum.DEPT_MEMBER.getType(), rule.getType())) {
            assigneeUserIds = calculateTaskCandidateUsersByDeptMember(rule);
        } else if (Objects.equals(BpmTaskAssignRuleTypeEnum.DEPT_LEADER.getType(), rule.getType())) {
            assigneeUserIds = calculateTaskCandidateUsersByDeptLeader(rule);
        } else if (Objects.equals(BpmTaskAssignRuleTypeEnum.POST.getType(), rule.getType())) {
            assigneeUserIds = calculateTaskCandidateUsersByPost(rule);
        } else if (Objects.equals(BpmTaskAssignRuleTypeEnum.USER.getType(), rule.getType())) {
            assigneeUserIds = calculateTaskCandidateUsersByUser(rule);
        } else if (Objects.equals(BpmTaskAssignRuleTypeEnum.USER_GROUP.getType(), rule.getType())) {
            assigneeUserIds = calculateTaskCandidateUsersByUserGroup(rule);
        } else if (Objects.equals(BpmTaskAssignRuleTypeEnum.SCRIPT.getType(), rule.getType())) {
            assigneeUserIds = calculateTaskCandidateUsersByScript(execution, rule);
        }

        // 移除被禁用的用户
        removeDisableUsers(assigneeUserIds);
        // 如果候选人为空，抛出异常
        if (CollUtil.isEmpty(assigneeUserIds)) {
            log.error("[calculateTaskCandidateUsers][流程任务({}/{}/{}) 任务规则({}) 找不到候选人]", execution.getId(),
                    execution.getProcessDefinitionId(), execution.getCurrentActivityId(), toJsonString(rule));
            throw exception(TASK_CREATE_FAIL_NO_CANDIDATE_USER);
        }
        return assigneeUserIds;
    }

    private Set<Long> calculateTaskCandidateUsersByRole(BpmTaskAssignRulePO rule) {
        //todo 根据角色查询用户id
        return rule.getOptions();
    }

    private Set<Long> calculateTaskCandidateUsersByDeptMember(BpmTaskAssignRulePO rule) {
        //List<UcenterMsUserVO> users = adminUserApi.getUsersByDeptIds(rule.getOptions());
        //根据部门查询用户id
        return new HashSet<>();
    }

    private Set<Long> calculateTaskCandidateUsersByDeptLeader(BpmTaskAssignRulePO rule) {

        return new HashSet<>();
    }

    private Set<Long> calculateTaskCandidateUsersByPost(BpmTaskAssignRulePO rule) {
        return new HashSet<>();
    }

    private Set<Long> calculateTaskCandidateUsersByUser(BpmTaskAssignRulePO rule) {
        return rule.getOptions();
    }

    private Set<Long> calculateTaskCandidateUsersByUserGroup(BpmTaskAssignRulePO rule) {
        return new HashSet<>();
    }

    private Set<Long> calculateTaskCandidateUsersByScript(DelegateExecution execution, BpmTaskAssignRulePO rule) {
        // 获得对应的脚本
        List<BpmTaskAssignScript> scripts = new ArrayList<>(rule.getOptions().size());
        rule.getOptions().forEach(id -> {
            BpmTaskAssignScript script = scriptMap.get(id);
            if (script == null) {
                throw exception(TASK_ASSIGN_SCRIPT_NOT_EXISTS, id);
            }
            scripts.add(script);
        });
        // 逐个计算任务
        Set<Long> userIds = new HashSet<>();
        scripts.forEach(script -> CollUtil.addAll(userIds, script.calculateTaskCandidateUsers(execution)));
        return userIds;
    }

    @VisibleForTesting
    void removeDisableUsers(Set<Long> assigneeUserIds) {
        /*if (CollUtil.isEmpty(assigneeUserIds)) {
            return;
        }
        Map<Long, UcenterMsUserVO> userMap = adminUserApi.getUserMap(assigneeUserIds);
        assigneeUserIds.removeIf(id -> {
            UcenterMsUserVO user = userMap.get(id);
            return user == null || !CommonStatusEnum.ENABLE.getStatus().equals(user.getStatus());
        });*/
    }

}
