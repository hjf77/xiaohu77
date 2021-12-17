package com.fhs.front.service;

import com.fhs.core.base.service.BaseService;
import com.fhs.easycloud.anno.CloudApi;
import com.fhs.easycloud.anno.CloudMethod;
import com.fhs.front.form.GetSingleFrontUserForm;
import com.fhs.front.po.UcenterFrontUserPO;
import com.fhs.front.vo.UcenterFrontUserVO;

import java.util.List;

/**
 * 前端用户表(UcenterFrontUser)表服务接口
 *
 * @author wanglei
 * @since 2019-03-11 14:14:58
 */
@CloudApi(serviceName = "basic",primary = false)
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

    /**
     * 根据条件获取单个用户
     * @param getSingleFrontUserForm
     * @return
     */
    @CloudMethod
    UcenterFrontUserVO getSingleFrontUser(GetSingleFrontUserForm getSingleFrontUserForm);
}
