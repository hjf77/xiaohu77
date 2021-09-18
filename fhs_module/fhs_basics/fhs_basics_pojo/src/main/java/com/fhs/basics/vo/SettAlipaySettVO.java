package com.fhs.basics.vo;

import com.fhs.basics.dox.SettAlipaySettDO;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 支付宝设置vo
 *
 * @author user
 * @date 2020-05-18 15:32:23
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TransTypes(types = {TransType.WORD_BOOK, TransType.AUTO_TRANS})
@ApiModel(value = "SettAlipaySettVO", description = "SettAlipaySett参数")
public class SettAlipaySettVO extends SettAlipaySettDO implements VO {

}
