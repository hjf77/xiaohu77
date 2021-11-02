package com.fhs.basics.vo;

import com.fhs.basics.po.ServiceDictGroupPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 字典分组vo
 *
 * @author user
 * @date 2020-05-18 15:28:36
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "字典分组参数")
public class ServiceDictGroupVO extends ServiceDictGroupPO implements VO {

}
