package com.fhs.basics.vo;

import com.fhs.basics.dox.UcenterMsRoleDO;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
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
@TransTypes(types = {TransType.WORD_BOOK, TransType.AUTO_TRANS})
@ApiModel(value = "UcenterMsRoleVO", description = "UcenterMsRole参数")
public class UcenterMsRoleVO extends UcenterMsRoleDO implements VO {

}
