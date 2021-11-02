package com.fhs.basics.vo;

import com.fhs.basics.po.SettAlipaySettPO;
import com.fhs.core.trans.vo.VO;
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
@ApiModel(value = "SettAlipaySettVO", description = "SettAlipaySett参数")
public class SettAlipaySettVO extends SettAlipaySettPO implements VO {

}
