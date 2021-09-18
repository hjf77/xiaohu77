package com.fhs.flow.vo;

import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import com.fhs.flow.dox.FlowListenerDO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 监听器vo
 *
 * @author user
 * @since 2019-05-18 13:33:05
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TransTypes(types = {TransType.WORD_BOOK, TransType.AUTO_TRANS})
@ApiModel(value = "FlowListenerVO", description = "FlowListener参数")
public class FlowListenerVO extends FlowListenerDO implements VO {

}
