package com.fhs.flow.vo;

import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import com.fhs.flow.dox.FlowTaskHistoryDO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 流程任务日志vo
 *
 * @author user
 * @since 2019-05-18 13:34:05
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TransTypes(types = {TransType.WORD_BOOK, TransType.AUTO_TRANS})
@ApiModel(value = "FlowTaskHistoryVO", description = "FlowTaskHistory参数")
public class FlowTaskHistoryVO extends FlowTaskHistoryDO implements VO {

}
