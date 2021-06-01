package com.fhs.flow.vo;


import com.fhs.flow.dox.FlowJbmpAuthFormDO;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 自定义审批表单(FlowJbmpAuthForm)实体类
 *
 * @author wanglei
 * @since 2021-06-01 15:03:55
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FlowJbmpAuthFormVO", description = "FlowJbmpAuthForm参数")
@TransTypes(types = {TransType.WORD_BOOK, TransType.AUTO_TRANS})
public class FlowJbmpAuthFormVO extends FlowJbmpAuthFormDO implements VO {

}
    
