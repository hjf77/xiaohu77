package com.fhs.basics.vo;

import com.fhs.basics.dox.UcenterAlipaySettDO;
import com.fhs.basics.dox.UcenterFrontUserBindDO;
import com.fhs.common.constant.Constant;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 前端用户绑定 openidvo
 */
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TransTypes(types = {Constant.WORD_BOOK, Constant.USER_INFO})
public class UcenterFrontUserBindVO extends UcenterFrontUserBindDO implements VO {

}