package com.fhs.flow.dal.mysql.definition;

import com.fhs.flow.comon.pojo.PageResult;
import com.fhs.flow.controller.admin.definition.vo.group.BpmUserGroupPageReqVO;
import com.fhs.flow.core.mp.mapper.BaseMapperX;
import com.fhs.flow.core.mp.query.LambdaQueryWrapperX;
import com.fhs.flow.dal.dataobject.definition.BpmUserGroupPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户组 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface BpmUserGroupMapper extends BaseMapperX<BpmUserGroupPO> {

    default PageResult<BpmUserGroupPO> selectPage(BpmUserGroupPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BpmUserGroupPO>()
                .likeIfPresent(BpmUserGroupPO::getName, reqVO.getName())
                .eqIfPresent(BpmUserGroupPO::getStatus, reqVO.getStatus())
                .betweenIfPresent(BpmUserGroupPO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BpmUserGroupPO::getId));
    }

    default List<BpmUserGroupPO> selectListByStatus(Integer status) {
        return selectList(BpmUserGroupPO::getStatus, status);
    }

}
