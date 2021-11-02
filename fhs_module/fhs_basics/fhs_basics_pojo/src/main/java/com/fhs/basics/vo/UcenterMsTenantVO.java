package com.fhs.basics.vo;

import com.fhs.basics.po.UcenterMsTenantPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 租户vo
 *
 * @author user
 * @date 2020-05-18 15:48:56
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UcenterMsTenantVO", description = "UcenterMsTenant参数")
public class UcenterMsTenantVO extends UcenterMsTenantPO implements VO {

}
