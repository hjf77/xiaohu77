package com.fhs.flow.dal.mysql.task;

import com.fhs.core.base.pojo.pager.Pager;
import com.fhs.flow.comon.pojo.PageResult;
import com.fhs.flow.controller.admin.task.vo.instance.BpmProcessInstanceMyPageReqVO;
import com.fhs.flow.core.mp.mapper.BaseMapperX;
import com.fhs.flow.core.mp.query.LambdaQueryWrapperX;
import com.fhs.flow.dal.dataobject.task.BpmProcessInstanceExtPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BpmProcessInstanceExtMapper extends BaseMapperX<BpmProcessInstanceExtPO> {

    default PageResult<BpmProcessInstanceExtPO> selectPage(Long userId, BpmProcessInstanceMyPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BpmProcessInstanceExtPO>()
                .eqIfPresent(BpmProcessInstanceExtPO::getStartUserId, userId)
                .likeIfPresent(BpmProcessInstanceExtPO::getName, reqVO.getName())
                .eqIfPresent(BpmProcessInstanceExtPO::getProcessDefinitionId, reqVO.getProcessDefinitionId())
                .eqIfPresent(BpmProcessInstanceExtPO::getCategory, reqVO.getCategory())
                .eqIfPresent(BpmProcessInstanceExtPO::getStatus, reqVO.getStatus())
                .eqIfPresent(BpmProcessInstanceExtPO::getResult, reqVO.getResult())
                .betweenIfPresent(BpmProcessInstanceExtPO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BpmProcessInstanceExtPO::getId));
    }

    default BpmProcessInstanceExtPO selectByProcessInstanceId(String processInstanceId) {
        return selectOne(BpmProcessInstanceExtPO::getProcessInstanceId, processInstanceId);
    }

    default void updateByProcessInstanceId(BpmProcessInstanceExtPO updateObj) {
        update(updateObj, new LambdaQueryWrapperX<BpmProcessInstanceExtPO>()
                .eq(BpmProcessInstanceExtPO::getProcessInstanceId, updateObj.getProcessInstanceId()));
    }

}
