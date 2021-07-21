package com.fhs.basics.vo;

import com.fhs.basics.dox.SettMsSystemDO;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 子系统vo
 * @author user
 * @date 2020-05-18 15:37:57
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TransTypes(types = {TransType.WORD_BOOK, TransType.AUTO_TRANS})
@ApiModel(value ="SettMsSystemVO",description ="SettMsSystem参数")
public class SettMsSystemVO extends SettMsSystemDO implements VO {

}