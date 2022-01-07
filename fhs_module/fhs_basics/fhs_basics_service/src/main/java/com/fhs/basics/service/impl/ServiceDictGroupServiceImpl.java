package com.fhs.basics.service.impl;

import com.fhs.basics.po.ServiceDictGroupPO;
import com.fhs.basics.service.ServiceDictGroupService;
import com.fhs.basics.vo.ServiceDictGroupVO;
import com.fhs.common.utils.JsonUtil;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.cache.service.RedisCacheService;
import com.fhs.core.db.ds.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 字典分组
 *
 * @author nanshouxiao
 * @version [版本号, 2015/12/22 15:13:20]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service
@DataSource("basic")
public class ServiceDictGroupServiceImpl extends BaseServiceImpl<ServiceDictGroupVO, ServiceDictGroupPO> implements ServiceDictGroupService {


    @Autowired
    private RedisCacheService redisCacheService;

    @Override
    public boolean refreshRedisCache(String groupCode) {
        //刷新字典缓存
        Map<String,String> body = new HashMap<>();
        body.put("transType","dict");
        redisCacheService.convertAndSend("trans", JsonUtil.map2json(body));
        return true;
    }
}