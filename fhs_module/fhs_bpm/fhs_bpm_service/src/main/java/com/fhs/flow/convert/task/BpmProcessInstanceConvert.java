package com.fhs.flow.convert.task;

import com.fhs.basics.vo.UcenterMsOrganizationVO;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.core.base.vo.FhsPager;
import com.fhs.flow.comon.pojo.PageResult;
import com.fhs.flow.controller.admin.task.vo.instance.BpmProcessInstancePageItemRespVO;
import com.fhs.flow.controller.admin.task.vo.instance.BpmProcessInstanceRespVO;
import com.fhs.flow.dal.dataobject.definition.BpmProcessDefinitionExtPO;
import com.fhs.flow.dal.dataobject.task.BpmProcessInstanceExtPO;
import com.fhs.flow.framework.bpm.core.event.BpmProcessInstanceResultEvent;
import com.fhs.flow.service.message.dto.BpmMessageSendWhenProcessInstanceApproveReqDTO;
import com.fhs.flow.service.message.dto.BpmMessageSendWhenProcessInstanceRejectReqDTO;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

/**
 * 流程实例 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface BpmProcessInstanceConvert {

    BpmProcessInstanceConvert INSTANCE = Mappers.getMapper(BpmProcessInstanceConvert.class);

    default PageResult<BpmProcessInstancePageItemRespVO> convertPage(PageResult<BpmProcessInstanceExtPO> page,
                                                                     Map<String, List<Task>> taskMap) {
        List<BpmProcessInstancePageItemRespVO> list = convertList(page.getList());
        list.forEach(respVO -> respVO.setTasks(convertList2(taskMap.get(respVO.getId()))));
        return new PageResult(list,page.getTotal());
    }

    List<BpmProcessInstancePageItemRespVO> convertList(List<BpmProcessInstanceExtPO> list);

    @Mapping(source = "processInstanceId", target = "id")
    BpmProcessInstancePageItemRespVO convert(BpmProcessInstanceExtPO bean);

    List<BpmProcessInstancePageItemRespVO.Task> convertList2(List<Task> tasks);

    default BpmProcessInstanceRespVO convert2(HistoricProcessInstance processInstance, BpmProcessInstanceExtPO processInstanceExt,
                                              ProcessDefinition processDefinition, BpmProcessDefinitionExtPO processDefinitionExt,
                                              String bpmnXml, UcenterMsUserVO startUser, UcenterMsOrganizationVO dept) {
        BpmProcessInstanceRespVO respVO = convert2(processInstance);
        copyTo(processInstanceExt, respVO);
        // definition
        respVO.setProcessDefinition(convert2(processDefinition));
        copyTo(processDefinitionExt, respVO.getProcessDefinition());
        respVO.getProcessDefinition().setBpmnXml(bpmnXml);
        // user
        if (startUser != null) {
            respVO.setStartUser(convert2(startUser));
            if (dept != null) {
                respVO.getStartUser().setDeptName(dept.getName());
            }
        }
        return respVO;
    }

    BpmProcessInstanceRespVO convert2(HistoricProcessInstance bean);

    @Mapping(source = "from.id", target = "to.id", ignore = true)
    void copyTo(BpmProcessInstanceExtPO from, @MappingTarget BpmProcessInstanceRespVO to);

    BpmProcessInstanceRespVO.ProcessDefinition convert2(ProcessDefinition bean);

    @Mapping(source = "from.id", target = "to.id", ignore = true)
    void copyTo(BpmProcessDefinitionExtPO from, @MappingTarget BpmProcessInstanceRespVO.ProcessDefinition to);

    BpmProcessInstanceRespVO.User convert2(UcenterMsUserVO bean);

    default BpmProcessInstanceResultEvent convert(Object source, HistoricProcessInstance instance, Integer result) {
        BpmProcessInstanceResultEvent event = new BpmProcessInstanceResultEvent(source);
        event.setId(instance.getId());
        event.setProcessDefinitionKey(instance.getProcessDefinitionKey());
        event.setBusinessKey(instance.getBusinessKey());
        event.setResult(result);
        return event;
    }

    default BpmProcessInstanceResultEvent convert(Object source, ProcessInstance instance, Integer result) {
        BpmProcessInstanceResultEvent event = new BpmProcessInstanceResultEvent(source);
        event.setId(instance.getId());
        event.setProcessDefinitionKey(instance.getProcessDefinitionKey());
        event.setBusinessKey(instance.getBusinessKey());
        event.setResult(result);
        return event;
    }

    default BpmMessageSendWhenProcessInstanceApproveReqDTO convert2ApprovedReq(ProcessInstance instance) {
        BpmMessageSendWhenProcessInstanceApproveReqDTO result = new BpmMessageSendWhenProcessInstanceApproveReqDTO();
        result.setStartUserId(ConverterUtils.toLong(instance.getStartUserId()));
        result.setProcessInstanceId(instance.getId());
        result.setProcessInstanceName(instance.getName());
        return result;
    }

    default BpmMessageSendWhenProcessInstanceRejectReqDTO convert2RejectReq(ProcessInstance instance, String reason) {
        BpmMessageSendWhenProcessInstanceRejectReqDTO reulst = new BpmMessageSendWhenProcessInstanceRejectReqDTO();
        reulst.setProcessInstanceName(instance.getName());
        reulst.setProcessInstanceId(instance.getId());
        reulst.setReason(reason);
        reulst.setStartUserId(ConverterUtils.toLong(instance.getStartUserId()));
        return reulst;
    }

}
