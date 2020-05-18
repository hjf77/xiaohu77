package com.fhs.front.vo;

import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import com.fhs.front.dox.UcenterFrontUserBindDO;
import lombok.*;

/**
 * 前端用户绑定 openidvo
 * @author user
 * @since 2019-05-18 11:49:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TransTypes(types = {TransType.WORD_BOOK})
public class UcenterFrontUserBindVO extends UcenterFrontUserBindDO implements VO {

}
