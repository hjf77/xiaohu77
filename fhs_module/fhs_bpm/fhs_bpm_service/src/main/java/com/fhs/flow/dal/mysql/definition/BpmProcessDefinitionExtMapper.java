package com.fhs.flow.dal.mysql.definition;

import com.fhs.flow.core.mp.mapper.BaseMapperX;
import com.fhs.flow.dal.dataobject.definition.BpmProcessDefinitionExtPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface BpmProcessDefinitionExtMapper extends BaseMapperX<BpmProcessDefinitionExtPO> {

    default List<BpmProcessDefinitionExtPO> selectListByProcessDefinitionIds(Collection<String> processDefinitionIds) {
        return selectList("process_definition_id", processDefinitionIds);
    }

    default BpmProcessDefinitionExtPO selectByProcessDefinitionId(String processDefinitionId) {
        return selectOne("process_definition_id", processDefinitionId);
    }

}
