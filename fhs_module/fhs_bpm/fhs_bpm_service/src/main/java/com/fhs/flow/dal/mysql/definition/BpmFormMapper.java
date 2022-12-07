package com.fhs.flow.dal.mysql.definition;


import com.fhs.flow.comon.pojo.PageResult;
import com.fhs.flow.controller.admin.definition.vo.form.BpmFormPageReqVO;
import com.fhs.flow.core.mp.mapper.BaseMapperX;
import com.fhs.flow.core.mp.query.QueryWrapperX;
import com.fhs.flow.dal.dataobject.definition.BpmFormPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 动态表单 Mapper
 *
 * @author 风里雾里
 */
@Mapper
public interface BpmFormMapper extends BaseMapperX<BpmFormPO> {

    default PageResult<BpmFormPO> selectPage(BpmFormPageReqVO reqVO) {
        return selectPage(reqVO, new QueryWrapperX<BpmFormPO>()
                .likeIfPresent("name", reqVO.getName())
                .orderByDesc("id"));
    }

}
