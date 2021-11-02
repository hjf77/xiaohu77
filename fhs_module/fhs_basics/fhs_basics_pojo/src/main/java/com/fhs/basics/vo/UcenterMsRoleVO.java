package com.fhs.basics.vo;

import com.fhs.basics.po.UcenterMsRolePO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 角色vo
 *
 * @author user
 * @date 2020-05-18 15:48:33
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UcenterMsRoleVO", description = "UcenterMsRole参数")
public class UcenterMsRoleVO extends UcenterMsRolePO implements VO {

}
