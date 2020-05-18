package com.fhs.basics.vo;

import com.fhs.basics.dox.UcenterMsTenantDO;
import com.fhs.common.constant.Constant;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 租户vo
 * @author user
 * @date 2020-05-18 15:48:56
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TransTypes(types = {TransType.WORD_BOOK})
public class UcenterMsTenantVO extends UcenterMsTenantDO implements VO {

}
