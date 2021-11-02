package com.fhs.basics.vo;

import com.fhs.basics.po.SettMpSettPO;
import com.fhs.core.trans.vo.VO;
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
@ApiModel(value = "SettMpSettVO", description = "SettMpSett参数")
public class SettMpSettVO extends SettMpSettPO implements VO {

}
