package com.fhs.basics.vo;

import com.fhs.basics.dox.SettMsModelDO;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 服务器vo
 * @author user
 * @date 2020-05-18 15:37:20
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TransTypes(types = {TransType.WORD_BOOK, TransType.AUTO_TRANS})
@ApiModel(value ="SettMsModelVO",description ="SettMsModel参数")
public class SettMsModelVO extends SettMsModelDO implements VO {

}
