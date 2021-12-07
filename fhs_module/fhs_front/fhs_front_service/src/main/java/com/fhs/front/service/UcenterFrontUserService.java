package com.fhs.front.service;

import com.fhs.core.base.service.BaseService;
import com.fhs.front.po.UcenterFrontUserPO;
import com.fhs.front.vo.UcenterFrontUserVO;

import java.util.List;

/**
 * 前端用户表(UcenterFrontUser)表服务接口
 *
 * @author wanglei
 * @since 2019-03-11 14:14:58
 */
public interface UcenterFrontUserService extends BaseService<UcenterFrontUserVO, UcenterFrontUserPO> {

    /**
     * 未知
     */
    String SEX_UNKNOWN = "0";

    /**
     * 男
     */
    String SEX_BOY = "1";

    /**
     * 女
     */
    String SEX_GIRL = "2";

    List<UcenterFrontUserVO> findListFilterMobile();
}
