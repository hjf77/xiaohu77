package com.fhs.basics.vo;

import com.fhs.basics.dox.SettMsSystemDO;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import lombok.*;

/**
 * 子系统vo
 * @author user
 * @date 2020-05-18 15:37:57
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TransTypes(types = {TransType.WORD_BOOK})
public class SettMsSystemVO extends SettMsSystemDO implements VO {

}
