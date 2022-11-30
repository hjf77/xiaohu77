package com.fhs.flow.convert.task;

import com.fhs.basics.vo.UcenterMsOrganizationVO;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.flow.comon.utils.CollectionUtils;
import com.fhs.flow.controller.admin.task.vo.task.BpmTaskDonePageItemRespVO;
import com.fhs.flow.controller.admin.task.vo.task.BpmTaskRespVO;
import com.fhs.flow.controller.admin.task.vo.task.BpmTaskTodoPageItemRespVO;
import com.fhs.flow.dal.dataobject.task.BpmTaskExtPO;
import com.fhs.flow.service.message.dto.BpmMessageSendWhenTaskCreatedReqDTO;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Bpm 任务 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface BpmTaskConvert {

    BpmTaskConvert INSTANCE = Mappers.getMapper(BpmTaskConvert.class);

    /**
     * 复制对象
     *
     * @param source 源 要复制的对象
     * @param target 目标 复制到此对象
     * @param <T>
     * @return
     */
    public static <T> T copy(Object source, Class<T> target) {
        if (source == null || target == null) {
            return null;
        }
        try {
            T newInstance = target.newInstance();
            BeanUtils.copyProperties(source, newInstance);
            return newInstance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default <T, K> List<K> copyList(List<T> source, Class<K> target) {
        if (null == source || source.isEmpty()) {
            return Collections.emptyList();
        }
        return source.stream().map(e -> copy(e, target)).collect(Collectors.toList());
    }

    default List<BpmTaskTodoPageItemRespVO> convertList1(List<Task> tasks,
                                                         Map<String, ProcessInstance> processInstanceMap, Map<Long, UcenterMsUserVO> userMap) {
        return CollectionUtils.convertList(tasks, task -> {
            BpmTaskTodoPageItemRespVO respVO = convert1(task);
            ProcessInstance processInstance = processInstanceMap.get(task.getProcessInstanceId());
            if (processInstance != null) {
                UcenterMsUserVO startUser = userMap.get(ConverterUtils.toLong(processInstance.getStartUserId()));
                respVO.setProcessInstance(convert(processInstance, startUser));
            }
            return respVO;
        });
    }


    BpmTaskTodoPageItemRespVO convert1(Task bean);

    @Named("convertSuspendedToSuspensionState")
    default Integer convertSuspendedToSuspensionState(boolean suspended) {
        return suspended ? SuspensionState.SUSPENDED.getStateCode() : SuspensionState.ACTIVE.getStateCode();
    }

    default List<BpmTaskDonePageItemRespVO> convertList2(List<HistoricTaskInstance> tasks,
                                                         Map<String, BpmTaskExtPO> BpmTaskExtPOMap, Map<String, HistoricProcessInstance> historicProcessInstanceMap,
                                                         Map<Long, UcenterMsUserVO> userMap) {
        return CollectionUtils.convertList(tasks, task -> {
            BpmTaskDonePageItemRespVO respVO = convert2(task);
            BpmTaskExtPO taskExtDO = BpmTaskExtPOMap.get(task.getId());
            copyTo(taskExtDO, respVO);
            HistoricProcessInstance processInstance = historicProcessInstanceMap.get(task.getProcessInstanceId());
            if (processInstance != null) {
                UcenterMsUserVO startUser = userMap.get(ConverterUtils.toLong(processInstance.getStartUserId()));
                respVO.setProcessInstance(convert(processInstance, startUser));
            }
            return respVO;
        });
    }

    BpmTaskDonePageItemRespVO convert2(HistoricTaskInstance bean);

    @Mappings({@Mapping(source = "processInstance.id", target = "id"),
            @Mapping(source = "processInstance.name", target = "name"),
            @Mapping(source = "processInstance.startUserId", target = "startUserId"),
            @Mapping(source = "processInstance.processDefinitionId", target = "processDefinitionId"),
            @Mapping(source = "startUser.userName", target = "startUserNickname")})
    BpmTaskTodoPageItemRespVO.ProcessInstance convert(ProcessInstance processInstance, UcenterMsUserVO startUser);

    default List<BpmTaskRespVO> convertList3(List<HistoricTaskInstance> tasks,
                                             Map<String, BpmTaskExtPO> BpmTaskExtPOMap, HistoricProcessInstance processInstance,
                                             Map<Long, UcenterMsUserVO> userMap, Map<String, UcenterMsOrganizationVO> deptMap) {
        return CollectionUtils.convertList(tasks, task -> {
            BpmTaskRespVO respVO = convert3(task);
            BpmTaskExtPO taskExtDO = BpmTaskExtPOMap.get(task.getId());
            copyTo(taskExtDO, respVO);
            if (processInstance != null) {
                UcenterMsUserVO startUser = userMap.get(ConverterUtils.toLong(processInstance.getStartUserId()));
                respVO.setProcessInstance(convert(processInstance, startUser));
            }
            UcenterMsUserVO assignUser = userMap.get(ConverterUtils.toLong(task.getAssignee()));
            if (assignUser != null) {
                respVO.setAssigneeUser(convert3(assignUser));
                UcenterMsOrganizationVO dept = deptMap.get(assignUser.getOrganizationId());
                if (dept != null) {
                    respVO.getAssigneeUser().setDeptName(dept.getName());
                }
            }
            return respVO;
        });
    }

    @Mapping(source = "taskDefinitionKey", target = "definitionKey")
    BpmTaskRespVO convert3(HistoricTaskInstance bean);

    BpmTaskRespVO.User convert3(UcenterMsUserVO bean);

    @Mapping(target = "id", ignore = true)
    void copyTo(BpmTaskExtPO from, @MappingTarget BpmTaskDonePageItemRespVO to);

    @Mappings({@Mapping(source = "processInstance.id", target = "id"),
            @Mapping(source = "processInstance.name", target = "name"),
            @Mapping(source = "processInstance.startUserId", target = "startUserId"),
            @Mapping(source = "processInstance.processDefinitionId", target = "processDefinitionId"),
            @Mapping(source = "startUser.userName", target = "startUserNickname")})
    BpmTaskTodoPageItemRespVO.ProcessInstance convert(HistoricProcessInstance processInstance,
                                                      UcenterMsUserVO startUser);

    default BpmTaskExtPO convert2TaskExt(Task task) {
        BpmTaskExtPO taskExtDO = new BpmTaskExtPO();
        taskExtDO.setTaskId(task.getId());
        taskExtDO.setAssigneeUserId(ConverterUtils.toLong(task.getAssignee()));
        taskExtDO.setName(task.getName());
        taskExtDO.setProcessDefinitionId(task.getProcessDefinitionId());
        taskExtDO.setProcessInstanceId(task.getProcessInstanceId());
        taskExtDO.setCreateTime(task.getCreateTime());
        return taskExtDO;
    }

    default BpmMessageSendWhenTaskCreatedReqDTO convert(ProcessInstance processInstance, UcenterMsUserVO startUser,
                                                        Task task) {
        BpmMessageSendWhenTaskCreatedReqDTO reqDTO = new BpmMessageSendWhenTaskCreatedReqDTO();
        reqDTO.setProcessInstanceId(processInstance.getProcessInstanceId());
        reqDTO .setProcessInstanceName(processInstance.getName());reqDTO.setStartUserId(startUser.getUserId());
        reqDTO.setStartUserNickname(startUser.getUserName());reqDTO.setTaskId(task.getId());reqDTO.setTaskName(task.getName());
        reqDTO.setAssigneeUserId(ConverterUtils.toLong(task.getAssignee()));
        return reqDTO;
    }

}
