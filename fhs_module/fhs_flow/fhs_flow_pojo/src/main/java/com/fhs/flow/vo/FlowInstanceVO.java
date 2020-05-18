package com.fhs.flow.vo;

import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import com.fhs.flow.dox.FlowInstanceDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * fhs的流程实例，为activiti的实例扩展表 vo
 * @author user
 * @since 2019-05-18 11:59:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TransTypes(types = {TransType.WORD_BOOK,TransType.AUTO_TRANS})
public class FlowInstanceVO extends FlowInstanceDO implements VO {

}
