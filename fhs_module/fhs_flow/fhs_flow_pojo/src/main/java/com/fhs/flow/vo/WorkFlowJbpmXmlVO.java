package com.fhs.flow.vo;

import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import com.fhs.flow.dox.WorkFlowJbpmXmlDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 流程xml管理 vo
 * @author user
 * @date 2020-05-18 13:42:22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TransTypes(types = {TransType.WORD_BOOK})
public class WorkFlowJbpmXmlVO extends WorkFlowJbpmXmlDO implements VO {

}
