package com.fhs.flow.vo;

import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import com.fhs.flow.dox.FlowJbpmXmlDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 流程列表-xml vo
 * @author user
 * @since 2019-05-18 12:00:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TransTypes(types = {TransType.WORD_BOOK})
public class FlowJbpmXmlVO extends FlowJbpmXmlDO implements VO {

}
