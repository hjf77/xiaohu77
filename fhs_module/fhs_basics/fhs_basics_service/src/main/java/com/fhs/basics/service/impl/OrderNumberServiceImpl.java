package com.fhs.basics.service.impl;

import com.fhs.common.constant.Constant;
import com.fhs.common.utils.DateUtils;
import com.fhs.common.utils.StringUtils;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.cache.service.RedisCacheService;
import com.fhs.core.db.ds.DataSource;
import com.fhs.core.result.HttpResult;
import com.fhs.basics.po.OrderNumberPO;
import com.fhs.basics.service.OrderNumberService;
import com.fhs.basics.vo.OrderNumberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * @author jianbo.qin
 * @version [版本号, 2018/05/10 15:09:42]
 * @Description:生成订单号
 * @versio 1.0
 * 陕西小伙伴网络科技有限公司
 * Copyright (c) 2017 All Rights Reserved.
 */
@Service("serviceOrderLogServiceImpl")
@DataSource("base_business")
public class OrderNumberServiceImpl extends BaseServiceImpl<OrderNumberVO, OrderNumberPO> implements OrderNumberService {

    /**
     * redis key
     */
    private static final String REDIS_KEY = "pubservice:order_number";

    @Autowired
    private  RedisCacheService<String> redisCacheService;

    /**
     * 生成订单号
     */
    protected void generation(String type, String date) {

        OrderNumberVO serviceOrderLog = this.getServiceOrderLog(type, date);
        if (serviceOrderLog == null) {
            serviceOrderLog = new OrderNumberVO();
            serviceOrderLog.setId(StringUtils.getUUID());
            serviceOrderLog.setType(type);
            serviceOrderLog.setTime(date);
            serviceOrderLog.setNumber(1);
            this.insertSelective(serviceOrderLog);
        }
        Integer orderIndex = serviceOrderLog.getNumber();
        int minOrderIndex = orderIndex;
        orderIndex += Constant.ONCE_ORDER_NUM_CREATE;
        serviceOrderLog.setNumber(orderIndex);
        this.updateSelectiveById(serviceOrderLog);
        List<String> orderList = orderNumList(minOrderIndex, date);
        redisCacheService.addSet(REDIS_KEY + ":" + type, orderList);
        // 设定redis失效时间
        redisCacheService.expire(REDIS_KEY + ":" + type, this.getExpireTime());
    }

    /**
     * 获取配置信息
     *
     * @param type
     * @return
     */
    private OrderNumberVO getServiceOrderLog(String type, String date) {
        OrderNumberVO serviceOrderLog = new OrderNumberVO();
        serviceOrderLog.setType(type);
        serviceOrderLog.setTime(date);
        serviceOrderLog = this.selectBean(serviceOrderLog);
        return serviceOrderLog;
    }



    /**
     * 组装orderNumList
     *
     * @param minOrderIndex
     * @param date
     * @return
     */
    private List<String> orderNumList(int minOrderIndex, String date) {
        List<String> dataList = new ArrayList<String>();
        for (int i = minOrderIndex; i < (Constant.ONCE_ORDER_NUM_CREATE + minOrderIndex); i++) {
            dataList.add(StringUtils.formatOrderNumber(date, i));
        }
        return dataList;
    }

    /**
     * redis晚上12点失效
     *
     * @return
     */
    private int getExpireTime() {
        long now = System.currentTimeMillis();
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        final long diff = cal.getTimeInMillis() - now;
        return Integer.valueOf(diff / 1000 + "");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public synchronized String getOrderNumber(String type) {
        try {
            long listSize = redisCacheService.getForListSize(REDIS_KEY + ":" + type);
            log.debug("队列中序列条数 ：" + listSize);
            // size为0 说明队列中已经没有序列、可以生成订单号了
            if (listSize == 0) {
                generation(type, DateUtils.getCurrentDateStr(DateUtils.DATETIME_PATTERN_DATE_NO_));// 生成订单号
            }
            //获取订单号
            String orderNo = redisCacheService.getBRPop(REDIS_KEY + ":" + type);
            return orderNo;
        } catch (Exception e1) {
            log.error("订单号默认值设定失败 ", e1);
            return null;
        }
    }
}
