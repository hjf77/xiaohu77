package com.fhs.basics.vo;

import com.fhs.basics.po.ServiceAreaPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 区域
 *
 * @author user
 * @date 2020-05-18 15:27:36
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ServiceAreaVO", description = "ServiceArea参数")
public class ServiceAreaVO extends ServiceAreaPO implements VO {

}
