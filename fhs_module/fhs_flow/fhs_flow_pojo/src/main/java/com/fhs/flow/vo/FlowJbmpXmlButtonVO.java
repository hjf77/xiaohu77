package com.fhs.flow.vo;


import com.fhs.flow.dox.FlowJbmpXmlButtonDO;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 流程自定义按钮(FlowJbmpXmlButton)实体类
 *
 * @author wanglei
 * @since 2020-06-16 14:54:52
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FlowJbmpXmlButtonVO", description = "FlowJbmpXmlButton参数")
@TransTypes(types = {TransType.WORD_BOOK, TransType.AUTO_TRANS})
public class FlowJbmpXmlButtonVO extends FlowJbmpXmlButtonDO implements VO {

}
