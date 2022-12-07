package com.fhs.flow.dal.mysql.oa;

import com.fhs.flow.comon.pojo.PageResult;
import com.fhs.flow.controller.admin.oa.vo.BpmOALeavePageReqVO;
import com.fhs.flow.core.mp.mapper.BaseMapperX;
import com.fhs.flow.core.mp.query.LambdaQueryWrapperX;
import com.fhs.flow.dal.dataobject.oa.BpmOALeavePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 请假申请 Mapper
 *
 * @author jason
 * @author 芋道源码
 */
@Mapper
public interface BpmOALeaveMapper extends BaseMapperX<BpmOALeavePO> {

    default PageResult<BpmOALeavePO> selectPage(Long userId, BpmOALeavePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BpmOALeavePO>()
                .eqIfPresent(BpmOALeavePO::getUserId, userId)
                .eqIfPresent(BpmOALeavePO::getResult, reqVO.getResult())
                .eqIfPresent(BpmOALeavePO::getType, reqVO.getType())
                .likeIfPresent(BpmOALeavePO::getReason, reqVO.getReason())
                .betweenIfPresent(BpmOALeavePO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BpmOALeavePO::getId));
    }

}
