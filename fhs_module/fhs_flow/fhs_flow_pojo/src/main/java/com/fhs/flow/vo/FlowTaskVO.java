package com.fhs.flow.vo;

import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import com.fhs.flow.dox.FlowTaskDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 任务-主要用于待办和待签收vo
 *
 * @author Jackwong
 * @date 2019 -11-11 16:37:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TransTypes(types = {TransType.WORD_BOOK, TransType.AUTO_TRANS})
public class FlowTaskVO extends FlowTaskDO implements VO {

}
