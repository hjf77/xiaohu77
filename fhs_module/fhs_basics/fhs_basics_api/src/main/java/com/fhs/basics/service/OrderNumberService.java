package com.fhs.basics.service;

import com.fhs.core.base.service.BaseService;
import com.fhs.basics.po.OrderNumberPO;
import com.fhs.easycloud.anno.CloudApi;
import com.fhs.easycloud.anno.CloudMethod;
import com.fhs.basics.vo.OrderNumberVO;


/**
 * @author jianbo.qin
 * @version [版本号, 2018/05/10 15:09:42]
 * @Description:生成订单号
 * @versio 1.0
 * 陕西小伙伴网络科技有限公司
 * Copyright (c) 2017 All Rights Reserved.
 */
@CloudApi(serviceName = "basic")
public interface OrderNumberService extends BaseService<OrderNumberVO, OrderNumberPO> {
    /**
     * 根据类型获取订单号
     * @param type 类型
     * @return
     */
    @CloudMethod
    String getOrderNumber(String type);
}
