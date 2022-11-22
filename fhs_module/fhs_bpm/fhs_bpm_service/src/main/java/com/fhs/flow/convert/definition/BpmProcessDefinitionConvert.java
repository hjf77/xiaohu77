package com.fhs.flow.convert.definition;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.fhs.flow.comon.utils.CollectionUtils;
import com.fhs.flow.controller.admin.definition.vo.process.BpmProcessDefinitionPageItemRespVO;
import com.fhs.flow.controller.admin.definition.vo.process.BpmProcessDefinitionRespVO;
import com.fhs.flow.dal.dataobject.definition.BpmFormPO;
import com.fhs.flow.dal.dataobject.definition.BpmProcessDefinitionExtPO;
import com.fhs.flow.service.definition.dto.BpmProcessDefinitionCreateReqDTO;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

/**
 * Bpm 流程定义的 Convert
 *
 * @author yunlong.li
 */
@Mapper
public interface BpmProcessDefinitionConvert {

    BpmProcessDefinitionConvert INSTANCE = Mappers.getMapper(BpmProcessDefinitionConvert.class);

    BpmProcessDefinitionPageItemRespVO convert(ProcessDefinition bean);

    BpmProcessDefinitionExtPO convert2(BpmProcessDefinitionCreateReqDTO bean);

    default List<BpmProcessDefinitionPageItemRespVO> convertList(List<ProcessDefinition> list, Map<String, Deployment> deploymentMap,
                                                                 Map<String, BpmProcessDefinitionExtPO> processDefinitionDOMap, Map<Long, BpmFormPO> formMap) {
        return CollectionUtils.convertList(list, definition -> {
            Deployment deployment = definition.getDeploymentId() != null ? deploymentMap.get(definition.getDeploymentId()) : null;
            BpmProcessDefinitionExtPO definitionDO = processDefinitionDOMap.get(definition.getId());
            BpmFormPO form = definitionDO != null ? formMap.get(definitionDO.getFormId()) : null;
            return convert(definition, deployment, definitionDO, form);
        });
    }

    default List<BpmProcessDefinitionRespVO> convertList3(List<ProcessDefinition> list,
                                                          Map<String, BpmProcessDefinitionExtPO> processDefinitionDOMap) {
        return CollectionUtils.convertList(list, processDefinition -> {
            BpmProcessDefinitionRespVO respVO = convert3(processDefinition);
            BpmProcessDefinitionExtPO processDefinitionExtDO = processDefinitionDOMap.get(processDefinition.getId());
            // 复制通用属性
            copyTo(processDefinitionExtDO, respVO);
            return respVO;
        });
    }


    BpmProcessDefinitionRespVO convert3(ProcessDefinition bean);

    @Named("convertSuspendedToSuspensionState")
    default Integer convertSuspendedToSuspensionState(boolean suspended) {
        return suspended ? SuspensionState.SUSPENDED.getStateCode() :
                SuspensionState.ACTIVE.getStateCode();
    }

    default BpmProcessDefinitionPageItemRespVO convert(ProcessDefinition bean, Deployment deployment,
                                                       BpmProcessDefinitionExtPO processDefinitionExtDO, BpmFormPO form) {
        BpmProcessDefinitionPageItemRespVO respVO = convert(bean);
        respVO.setSuspensionState(bean.isSuspended() ? SuspensionState.SUSPENDED.getStateCode() : SuspensionState.ACTIVE.getStateCode());
        if (deployment != null) {
            respVO.setDeploymentTime(LocalDateTimeUtil.of(deployment.getDeploymentTime()));
        }
        if (form != null) {
            respVO.setFormName(form.getName());
        }
        // 复制通用属性
        copyTo(processDefinitionExtDO, respVO);
        return respVO;
    }

    @Mapping(source = "from.id", target = "to.id", ignore = true)
    void copyTo(BpmProcessDefinitionExtPO from, @MappingTarget BpmProcessDefinitionRespVO to);
}
