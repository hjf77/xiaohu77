package com.fhs.basics.vo;

import com.fhs.basics.dox.SettMsMenuPermissionDO;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 菜单权限vo
 *
 * @author user
 * @date 2020-05-18 15:33:48
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TransTypes(types = {TransType.WORD_BOOK, TransType.AUTO_TRANS})
@ApiModel(value = "SettMsMenuPermissionVO", description = "SettMsMenuPermission参数")
public class SettMsMenuPermissionVO extends SettMsMenuPermissionDO implements VO {

}
