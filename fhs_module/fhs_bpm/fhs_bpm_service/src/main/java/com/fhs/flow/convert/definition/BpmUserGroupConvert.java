package com.fhs.flow.convert.definition;

import com.fhs.flow.comon.pojo.PageResult;
import com.fhs.flow.controller.admin.definition.vo.group.BpmUserGroupCreateReqVO;
import com.fhs.flow.controller.admin.definition.vo.group.BpmUserGroupRespVO;
import com.fhs.flow.controller.admin.definition.vo.group.BpmUserGroupUpdateReqVO;
import com.fhs.flow.dal.dataobject.definition.BpmUserGroupPO;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 用户组 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface BpmUserGroupConvert {

    BpmUserGroupConvert INSTANCE = Mappers.getMapper(BpmUserGroupConvert.class);

    BpmUserGroupPO convert(BpmUserGroupCreateReqVO bean);

    BpmUserGroupPO convert(BpmUserGroupUpdateReqVO bean);

    BpmUserGroupRespVO convert(BpmUserGroupPO bean);

    List<BpmUserGroupRespVO> convertList(List<BpmUserGroupPO> list);

    PageResult<BpmUserGroupRespVO> convertPage(PageResult<BpmUserGroupPO> page);

    @Named("convertList2")
    List<BpmUserGroupRespVO> convertList2(List<BpmUserGroupPO> list);

}
