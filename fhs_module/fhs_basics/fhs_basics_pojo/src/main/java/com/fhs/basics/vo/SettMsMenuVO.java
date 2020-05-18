package com.fhs.basics.vo;

import com.fhs.basics.dox.SettMsMenuDO;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import lombok.*;

/**
 * 系统菜单vo
 * @author user
 * @date 2020-05-18 15:36:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TransTypes(types = {TransType.WORD_BOOK})
public class SettMsMenuVO extends SettMsMenuDO implements VO {

}
