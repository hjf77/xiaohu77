package com.fhs.basics.service;

import com.fhs.basics.po.ServiceAreaPO;
import com.fhs.basics.vo.ServiceAreaVO;
import com.fhs.core.base.service.BaseService;
import com.fhs.easycloud.anno.CloudApi;
import com.fhs.easycloud.anno.CloudMethod;

import java.util.Map;


/**
 * 省市区字典
 *
 * @Filename: ServiceAreaService.java
 * @Description:
 * @Version: 1.0
 * @Author: wanglei
 * @Email: qxb@sxpartner.com
 * @History:<br> 陕西小伙伴网络科技有限公司
 * Copyright (c) 2017 All Rights Reserved.
 */

@CloudApi(serviceName = "basic")
public interface ServiceAreaService extends BaseService<ServiceAreaVO, ServiceAreaPO> {


    /**
     * 刷新区域缓存
     */
    void refreshRedisCache();

    /**
     * 根据id获取区域编码
     */
    @CloudMethod
    Integer getById(Integer id);
}
