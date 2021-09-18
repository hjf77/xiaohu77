package com.fhs.basics.vo;

import com.fhs.basics.dox.SettMpSettDO;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 公众号设置vo
 *
 * @author user
 * @date 2020-05-18 15:32:38
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TransTypes(types = {TransType.WORD_BOOK, TransType.AUTO_TRANS})
@ApiModel(value = "SettMpSettVO", description = "SettMpSett参数")
public class SettMpSettVO extends SettMpSettDO implements VO {

}
