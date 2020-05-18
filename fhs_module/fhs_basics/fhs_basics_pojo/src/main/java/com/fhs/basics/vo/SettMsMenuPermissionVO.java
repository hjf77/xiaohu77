package com.fhs.basics.vo;

import com.fhs.basics.dox.SettMsMenuPermissionDO;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单权限vo
 * @author user
 * @date 2020-05-18 15:33:48
 */
@Data
@NoArgsConstructor
@TransTypes(types = {TransType.WORD_BOOK})
public class SettMsMenuPermissionVO extends SettMsMenuPermissionDO implements VO {

}
