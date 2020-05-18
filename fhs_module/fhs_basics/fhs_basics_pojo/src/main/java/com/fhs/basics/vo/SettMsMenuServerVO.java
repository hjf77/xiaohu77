package com.fhs.basics.vo;

import com.fhs.basics.dox.SettMsMenuServerDO;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务器vo
 * @author user
 * @date 2020-05-18 15:34:21
 */
@Data
@NoArgsConstructor
@TransTypes(types = {TransType.WORD_BOOK})
public class SettMsMenuServerVO extends SettMsMenuServerDO implements VO {

}
