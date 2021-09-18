package com.fhs.basics.vo;

import com.fhs.basics.dox.SettMsMenuServerDO;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 服务器vo
 *
 * @author user
 * @date 2020-05-18 15:34:21
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TransTypes(types = {TransType.WORD_BOOK, TransType.AUTO_TRANS})
@ApiModel(value = "SettMsMenuServerVO", description = "SettMsMenuServer参数")
public class SettMsMenuServerVO extends SettMsMenuServerDO implements VO {

}
