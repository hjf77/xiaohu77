package com.fhs.basics.vo;

import com.fhs.basics.po.SettMsModelPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 服务器vo
 *
 * @author user
 * @date 2020-05-18 15:37:20
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SettMsModelVO", description = "SettMsModel参数")
public class SettMsModelVO extends SettMsModelPO implements VO {

}
