package com.fhs.basics.vo;

import com.fhs.basics.dox.SettMsMenuPermissionUrlRelaDO;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单和URL对应关系
 * @author user
 * @date 2020-05-18 15:33:38
 */
@Data
@NoArgsConstructor
@TransTypes(types = {TransType.WORD_BOOK})
public class SettMsMenuPermissionUrlRelaVO extends SettMsMenuPermissionUrlRelaDO implements VO {

}
