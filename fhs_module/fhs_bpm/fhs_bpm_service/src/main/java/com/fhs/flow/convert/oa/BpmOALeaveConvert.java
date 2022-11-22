package com.fhs.flow.convert.oa;

import com.fhs.flow.comon.pojo.PageResult;
import com.fhs.flow.controller.admin.oa.vo.BpmOALeaveCreateReqVO;
import com.fhs.flow.controller.admin.oa.vo.BpmOALeaveRespVO;
import com.fhs.flow.dal.dataobject.oa.BpmOALeavePO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 请假申请 Convert
 *
 * @author 芋艿
 */
@Mapper
public interface BpmOALeaveConvert {

    BpmOALeaveConvert INSTANCE = Mappers.getMapper(BpmOALeaveConvert.class);

    BpmOALeavePO convert(BpmOALeaveCreateReqVO bean);

    BpmOALeaveRespVO convert(BpmOALeavePO bean);

    List<BpmOALeaveRespVO> convertList(List<BpmOALeavePO> list);

    PageResult<BpmOALeaveRespVO> convertPage(PageResult<BpmOALeavePO> page);

}
