package com.fhs.basics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fhs.basics.po.ServiceDictGroupPO;
import com.fhs.basics.po.ServiceDictItemPO;
import com.fhs.basics.service.ServiceDictGroupService;
import com.fhs.basics.service.ServiceDictItemService;
import com.fhs.basics.vo.ServiceDictGroupVO;
import com.fhs.basics.vo.ServiceDictItemVO;
import com.fhs.common.utils.JsonUtil;
import com.fhs.common.utils.ListUtils;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.cache.service.RedisCacheService;
import com.fhs.core.db.ds.DataSource;
import com.fhs.core.trans.constant.TransType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private ServiceDictItemService dictItemService;

    @Override
    public boolean refreshRedisCache() {
        //刷新字典缓存
        Map<String,String> body = new HashMap<>();
        body.put("transType", TransType.DICTIONARY);
        redisCacheService.convertAndSend("trans", JsonUtil.map2json(body));
        return true;
    }

    @Override
    public int updateById(ServiceDictGroupPO entity) {
        ServiceDictGroupPO oldData = super.selectById(entity.getGroupId());
        List<ServiceDictItemVO> items = dictItemService.selectListMP(new LambdaQueryWrapper<ServiceDictItemPO>().eq(ServiceDictItemPO::getDictGroupCode,oldData.getGroupCode()));
        for (ServiceDictItemPO item : items) {
            item.setDictGroupCode(entity.getGroupCode());
        }
        dictItemService.batchUpdate(ListUtils.copyListToPararentList(items,ServiceDictItemPO.class));
        int result = super.updateById(entity);
        return result;
    }
}
