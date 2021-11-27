package com.fhs.front.service;

import com.fhs.core.base.service.BaseService;
import com.fhs.front.po.UcenterFrontUserBindPO;
import com.fhs.front.vo.UcenterFrontUserBindVO;

/**
 * 前端用户openid绑定
 *
 * @author jackwong
 * @since 2019-03-11 14:37:18
 */
public interface UcenterFrontUserBindService extends BaseService<UcenterFrontUserBindVO, UcenterFrontUserBindPO> {

    /**
     * 公众号
     */
    int OPENID_TYPE_WXMP = 0;

    /**
     * 支付宝
     */
    int OPENID_TYPE_ALIPAY = 1;
    /**
     * 微信小程序
     */
    int OPENID_TYPE_WX_MINI_PARAM = 2;


}
