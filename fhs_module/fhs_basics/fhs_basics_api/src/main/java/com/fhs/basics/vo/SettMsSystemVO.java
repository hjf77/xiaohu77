package com.fhs.basics.vo;

import com.fhs.basics.po.SettMsSystemPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 子系统vo
 *
 * @author user
 * @date 2020-05-18 15:37:57
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SettMsSystemVO", description = "SettMsSystem参数")
public class SettMsSystemVO extends SettMsSystemPO implements VO {

}
