package com.fhs.flow.dal.mysql.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fhs.flow.core.mp.mapper.BaseMapperX;
import com.fhs.flow.dal.dataobject.task.BpmTaskExtPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface BpmTaskExtMapper extends BaseMapperX<BpmTaskExtPO> {

    default void updateByTaskId(BpmTaskExtPO entity) {
        update(entity, new LambdaQueryWrapper<BpmTaskExtPO>().eq(BpmTaskExtPO::getTaskId, entity.getTaskId()));
    }

    default List<BpmTaskExtPO> selectListByTaskIds(Collection<String> taskIds) {
        return selectList(BpmTaskExtPO::getTaskId, taskIds);
    }

    default BpmTaskExtPO selectByTaskId(String taskId) {
        return selectOne(BpmTaskExtPO::getTaskId, taskId);
    }

}
