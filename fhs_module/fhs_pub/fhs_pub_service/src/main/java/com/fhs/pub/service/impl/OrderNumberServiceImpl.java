package com.fhs.pub.service.impl;

import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.db.ds.DataSource;
import com.fhs.pub.dox.OrderNumberDO;
import com.fhs.pub.service.OrderNumberService;
import com.fhs.pub.vo.OrderNumberVO;
import org.springframework.stereotype.Service;


/**
 * @Description:生成订单号
 * @author  jianbo.qin
 * @version  [版本号, 2018/05/10 15:09:42]
 * @versio  1.0
 * 陕西小伙伴网络科技有限公司
 * Copyright (c) 2017 All Rights Reserved.
 * */
@Service("serviceOrderLogServiceImpl")
@DataSource("base_business")
public class OrderNumberServiceImpl extends BaseServiceImpl<OrderNumberVO, OrderNumberDO> implements OrderNumberService
{

}