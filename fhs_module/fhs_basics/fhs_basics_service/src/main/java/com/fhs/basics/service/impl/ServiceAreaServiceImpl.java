package com.fhs.basics.service.impl;

import com.fhs.basics.constant.BaseTransConstant;
import com.fhs.basics.mapper.ServiceAreaMapper;
import com.fhs.basics.po.ServiceAreaPO;
import com.fhs.basics.service.ServiceAreaService;
import com.fhs.basics.vo.ServiceAreaVO;
import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.StringUtils;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.cache.service.RedisCacheService;
import com.fhs.core.db.ds.DataSource;
import com.fhs.core.trans.anno.AutoTrans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 省市区字典
 *
 * @Filename: AreaServiceImpl.java
 * @Description:
 * @Version: 1.0
 * @Author: wanglei
 * @Email: qxb@sxpartner.com
 * @History:<br> 陕西小伙伴网络科技有限公司
 * Copyright (c) 2017 All Rights Reserved.
 */
@Service
@DataSource("basic")
@AutoTrans(namespace = BaseTransConstant.AREA, useRedis = true, fields = "areaName")
public class ServiceAreaServiceImpl extends BaseServiceImpl<ServiceAreaVO, ServiceAreaPO> implements ServiceAreaService {
    /**
     * redis缓存服务
     */
    @Autowired
    private RedisCacheService<String> redisCacheService;

    @Autowired
    private ServiceAreaMapper serviceAreaManager;

    @Override
    public String findAddressById(Map<String, Object> map) {
        String address = null;
        String provinceId = (String) map.get("provinceId");
        if (!CheckUtils.isNullOrEmpty(provinceId)) {
            address = super.selectById(provinceId).getAreaName();
        }
        String cityId = (String) map.get("cityId");
        if (!CheckUtils.isNullOrEmpty(cityId)) {
            address = address + super.selectById(cityId).getAreaName();
        }
        String areaId = (String) map.get("areaId");
        if (!CheckUtils.isNullOrEmpty(areaId)) {
            address = address + super.selectById(areaId).getAreaName();
        }
        return address;
    }

    @Override
    public void refreshRedisCache() {
        List<ServiceAreaVO> areaList = this.select();
        for (ServiceAreaVO area : areaList) {
            if (!StringUtils.isEmpty(area.getAreaName())) {
                try {
                    redisCacheService.remove(BaseTransConstant.AREA_NAME + area.getId());
                    redisCacheService.addStr(BaseTransConstant.AREA_NAME + area.getId(), area.getAreaName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<String> selectAllParentId(String id) {
        ServiceAreaPO serviceAreaPO = serviceAreaManager.selectById(id);
        List<String> code = new ArrayList<>();
        while (!serviceAreaPO.getAreaParentId().equals(0)){
            code.add(serviceAreaPO.getId());
            serviceAreaPO = serviceAreaManager.selectById(serviceAreaPO.getAreaParentId());
        }
        if(serviceAreaPO.getAreaParentId().equals(0)){
            code.add(serviceAreaPO.getId());
        }
        Collections.reverse(code);
        return code;
    }

}
