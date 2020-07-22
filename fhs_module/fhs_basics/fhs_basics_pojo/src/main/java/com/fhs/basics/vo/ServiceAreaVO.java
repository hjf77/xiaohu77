package com.fhs.basics.vo;

import com.fhs.basics.dox.ServiceAreaDO;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 区域
 * @author user
 * @date 2020-05-18 15:27:36
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="ServiceAreaVO",description ="ServiceArea参数")
@TransTypes(types = {TransType.WORD_BOOK, TransType.AUTO_TRANS})
public class ServiceAreaVO extends ServiceAreaDO implements VO {

}
