package com.fhs.flow.convert.definition;

import com.fhs.flow.comon.utils.CollectionUtils;
import com.fhs.flow.controller.admin.definition.vo.rule.BpmTaskAssignRuleCreateReqVO;
import com.fhs.flow.controller.admin.definition.vo.rule.BpmTaskAssignRuleRespVO;
import com.fhs.flow.controller.admin.definition.vo.rule.BpmTaskAssignRuleUpdateReqVO;
import com.fhs.flow.dal.dataobject.definition.BpmTaskAssignRulePO;
import org.flowable.bpmn.model.UserTask;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

@Mapper
public interface BpmTaskAssignRuleConvert {
    BpmTaskAssignRuleConvert INSTANCE = Mappers.getMapper(BpmTaskAssignRuleConvert.class);

    default List<BpmTaskAssignRuleRespVO> convertList(List<UserTask> tasks, List<BpmTaskAssignRulePO> rules) {
        Map<String, BpmTaskAssignRulePO> ruleMap = CollectionUtils.convertMap(rules, BpmTaskAssignRulePO::getTaskDefinitionKey);
        // 以 UserTask 为主维度，原因是：流程图编辑后，一些规则实际就没用了。
        return CollectionUtils.convertList(tasks, task -> {
            BpmTaskAssignRuleRespVO respVO = convert(ruleMap.get(task.getId()));
            if (respVO == null) {
                respVO = new BpmTaskAssignRuleRespVO();
                respVO.setTaskDefinitionKey(task.getId());
            }
            respVO.setTaskDefinitionName(task.getName());
            return respVO;
        });
    }

    BpmTaskAssignRuleRespVO convert(BpmTaskAssignRulePO bean);

    BpmTaskAssignRulePO convert(BpmTaskAssignRuleCreateReqVO bean);

    BpmTaskAssignRulePO convert(BpmTaskAssignRuleUpdateReqVO bean);

    List<BpmTaskAssignRulePO> convertList2(List<BpmTaskAssignRuleRespVO> list);

    BpmTaskAssignRulePO map(BpmTaskAssignRuleRespVO value);
}
