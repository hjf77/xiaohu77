package com.fhs.flow.convert.definition;

import com.fhs.flow.comon.pojo.PageResult;
import com.fhs.flow.controller.admin.definition.vo.form.BpmFormCreateReqVO;
import com.fhs.flow.controller.admin.definition.vo.form.BpmFormRespVO;
import com.fhs.flow.controller.admin.definition.vo.form.BpmFormSimpleRespVO;
import com.fhs.flow.controller.admin.definition.vo.form.BpmFormUpdateReqVO;
import com.fhs.flow.dal.dataobject.definition.BpmFormPO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 动态表单 Convert
 *
 * @author 芋艿
 */
@Mapper
public interface BpmFormConvert {

    BpmFormConvert INSTANCE = Mappers.getMapper(BpmFormConvert.class);

    BpmFormPO convert(BpmFormCreateReqVO bean);

    BpmFormPO convert(BpmFormUpdateReqVO bean);

    BpmFormRespVO convert(BpmFormPO bean);

    List<BpmFormSimpleRespVO> convertList2(List<BpmFormPO> list);

    PageResult<BpmFormRespVO> convertPage(PageResult<BpmFormPO> page);

    BpmFormSimpleRespVO map(BpmFormPO value);

}
