//package com.fhs.flow.listener;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.util.RandomUtil;
//import cn.hutool.extra.spring.SpringUtil;
//import com.fhs.basics.service.UcenterMsUserService;
//import com.fhs.common.utils.StringUtils;
//import org.flowable.engine.HistoryService;
//import org.flowable.engine.TaskService;
//import org.flowable.engine.history.HistoricProcessInstance;
//import org.flowable.task.api.Task;
//import org.flowable.task.api.TaskQuery;
//import org.flowable.task.service.delegate.DelegateTask;
//import org.flowable.task.service.delegate.TaskListener;
//
//import java.util.List;
//
///**
// * @author tanyukun
// */
//public class RuleTaskListener implements TaskListener {
//
//    @Override
//    public void notify(DelegateTask delegateTask) {
//        // 获取流程发起人
//        HistoricProcessInstance hi = SpringUtil.getBean(HistoryService.class).createHistoricProcessInstanceQuery()
//                .processInstanceId(delegateTask.getProcessInstanceId())
//                .singleResult();
//        if (StringUtils.isEmpty(hi.getStartUserId())) {
//            return;
//        }
//        System.out.println("hi.getStartUserId() = " + hi.getStartUserId());
//        String taskId = delegateTask.getId();
//        // 获取处理人，拿到的是角色id
//        TaskQuery taskQuery = SpringUtil.getBean(TaskService.class).createTaskQuery().taskId(taskId);
//        Task task = taskQuery.singleResult();
//        System.out.println("task.getAssignee() = " + task.getAssignee());
//        // 获取用户所在机构下指定角色的用户
//        List<Long> userIds = SpringUtil.getBean(UcenterMsUserService.class).getUserByRoleOrg(hi.getStartUserId(),task.getAssignee());
//        if (CollUtil.isEmpty(userIds)) {
//            return;
//        }
//        int index = RandomUtil.randomInt(userIds.size());
//        delegateTask.setAssignee(CollUtil.get(userIds, index).toString());
//    }
//
//}
