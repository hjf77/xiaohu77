package com.fhs.flow.framework.flowable.core.behavior.script.impl;

import com.fhs.basics.service.UcenterMsOrganizationService;
import com.fhs.basics.service.UcenterMsUserService;
import com.fhs.basics.vo.UcenterMsOrganizationVO;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.flow.framework.flowable.core.behavior.script.BpmTaskAssignScript;
import com.fhs.flow.service.task.BpmProcessInstanceService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Set;

import static java.util.Collections.emptySet;

/**
 * 分配给发起人的 Leader 审批的 Script 实现类
 * 目前 Leader 的定义是，
 *
 * @author 芋道源码
 */
public abstract class BpmTaskAssignLeaderAbstractScript implements BpmTaskAssignScript {

    @Resource
    private UcenterMsUserService adminUserApi;
    @Resource
    private UcenterMsOrganizationService deptApi;
    @Resource
    @Lazy // 解决循环依赖
    private BpmProcessInstanceService bpmProcessInstanceService;

    protected Set<Long> calculateTaskCandidateUsers(DelegateExecution execution, int level) {
        return emptySet();
    }

    private UcenterMsOrganizationVO getStartUserDept(Long startUserId) {
        UcenterMsUserVO startUser = adminUserApi.selectById(startUserId);
        if (startUser.getOrganizationId() == null) { // 找不到部门，所以无法使用该规则
            return null;
        }
        return deptApi.selectById(startUser.getOrganizationId());
    }

}
