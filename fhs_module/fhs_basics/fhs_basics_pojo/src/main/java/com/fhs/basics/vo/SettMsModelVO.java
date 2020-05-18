package com.fhs.basics.vo;

import com.fhs.basics.dox.SettMsModelDO;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import lombok.*;

/**
 * 服务器vo
 * @author user
 * @date 2020-05-18 15:37:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TransTypes(types = {TransType.WORD_BOOK})
public class SettMsModelVO extends SettMsModelDO implements VO {

}
