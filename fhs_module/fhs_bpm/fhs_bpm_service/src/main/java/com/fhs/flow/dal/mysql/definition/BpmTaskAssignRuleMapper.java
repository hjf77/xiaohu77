package com.fhs.flow.dal.mysql.definition;

import com.fhs.flow.core.mp.mapper.BaseMapperX;
import com.fhs.flow.core.mp.query.QueryWrapperX;
import com.fhs.flow.dal.dataobject.definition.BpmTaskAssignRulePO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.lang.Nullable;

import java.util.List;

@Mapper
public interface BpmTaskAssignRuleMapper extends BaseMapperX<BpmTaskAssignRulePO> {

    default List<BpmTaskAssignRulePO> selectListByProcessDefinitionId(String processDefinitionId,
                                                                      @Nullable String taskDefinitionKey) {
        return selectList(new QueryWrapperX<BpmTaskAssignRulePO>()
                .eq("process_definition_id", processDefinitionId)
                .eqIfPresent("task_definition_key", taskDefinitionKey));
    }

    default List<BpmTaskAssignRulePO> selectListByModelId(String modelId) {
        return selectList(new QueryWrapperX<BpmTaskAssignRulePO>()
                .eq("model_id", modelId)
                .eq("process_definition_id", ""));
    }

    default BpmTaskAssignRulePO selectListByModelIdAndTaskDefinitionKey(String modelId,
                                                                        String taskDefinitionKey) {
        return selectOne(new QueryWrapperX<BpmTaskAssignRulePO>()
                .eq("model_id", modelId)
                .eq("process_definition_id", "")
                .eq("task_definition_key", taskDefinitionKey));
    }


}
