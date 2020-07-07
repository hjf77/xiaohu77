package com.fhs.front.vo;

import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import com.fhs.front.dox.UcenterFrontUserBindDO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 前端用户绑定 openidvo
 * @author user
 * @since 2019-05-18 11:49:21
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TransTypes(types = {TransType.WORD_BOOK, TransType.AUTO_TRANS})
@ApiModel(value ="UcenterFrontUserBindVO",description ="UcenterFrontUserBind参数")
public class UcenterFrontUserBindVO extends UcenterFrontUserBindDO implements VO {

}
