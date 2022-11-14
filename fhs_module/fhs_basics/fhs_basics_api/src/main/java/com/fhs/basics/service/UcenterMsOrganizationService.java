package com.fhs.basics.service;

import com.fhs.basics.po.UcenterMsOrganizationPO;
import com.fhs.basics.vo.UcenterMsOrganizationVO;
import com.fhs.core.base.service.BaseService;
import com.fhs.easycloud.anno.CloudApi;
import com.fhs.easycloud.anno.CloudMethod;

import java.io.Serializable;
import java.util.List;

/**
 * @author qixiaobo
 * @version [版本号, 2018-09-04]
 * @Description:后台组织机构表
 * @versio 1.0 陕西小伙伴网络科技有限公司  Copyright (c) 2018 All Rights Reserved.
 */
@CloudApi(serviceName = "basic",primary = false)
public interface UcenterMsOrganizationService extends BaseService<UcenterMsOrganizationVO, UcenterMsOrganizationPO> {

    @CloudMethod
    UcenterMsOrganizationVO selectById(Serializable primaryValue);

    int countPipelineNum(String orgId);

    /**
     *按id模糊查询
     */
    @CloudMethod
    List<UcenterMsOrganizationVO> vagueById(String orgId);
}
