package com.fhs.basics.vo;

import com.fhs.basics.dox.SettMsMenuDO;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 系统菜单vo
 *
 * @author user
 * @date 2020-05-18 15:36:38
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TransTypes(types = {TransType.WORD_BOOK, TransType.AUTO_TRANS})
@ApiModel(value = "SettMsMenuVO", description = "SettMsMenu参数")
public class SettMsMenuVO extends SettMsMenuDO implements VO {

}
