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
import com.fhs.trans.service.impl.DictionaryTransService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
public class ServiceDictGroupServiceImpl extends BaseServiceImpl<ServiceDictGroupVO, ServiceDictGroupPO> implements ServiceDictGroupService, InitializingBean {


    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private ServiceDictItemService dictItemService;

    @Autowired
    private DictionaryTransService dictionaryTransService;

    @Override
    public boolean refreshRedisCache(String groupCode) {
        LambdaQueryWrapper<ServiceDictItemPO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(groupCode!=null){
            lambdaQueryWrapper.eq(ServiceDictItemPO::getDictGroupCode,groupCode);
        }
        List<ServiceDictItemVO> dictItemVOList = dictItemService.selectListMP(lambdaQueryWrapper);
        Map<String,List<ServiceDictItemVO>> dictGroupMap = dictItemVOList.stream().collect(Collectors.groupingBy(ServiceDictItemVO::getDictGroupCode));
        for (String dictGroupCode : dictGroupMap.keySet()) {
            dictionaryTransService.refreshCacheAndNoticeOtherService(dictGroupCode,dictGroupMap.get(dictGroupCode).stream().collect(Collectors
                    .toMap(ServiceDictItemVO::getDictCode, ServiceDictItemVO::getDictDesc)));
        }
        return true;
    }

    @Override
    public int updateById(ServiceDictGroupPO entity) {
        ServiceDictGroupPO oldData = super.selectById(entity.getGroupId());
        List<ServiceDictItemPO> items =
                new ServiceDictItemPO().dictGroupCode().eq(oldData.getGroupCode()).list();
        for (ServiceDictItemPO item : items) {
            item.setDictGroupCode(entity.getGroupCode());
        }
        dictItemService.batchUpdate(items);
        int result = super.updateById(entity);
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        //初始化所有数据
        List<ServiceDictItemVO> dictItemVOList = dictItemService.selectListMP(new LambdaQueryWrapper());
        Map<String,List<ServiceDictItemVO>> dictGroupMap = dictItemVOList.stream().collect(Collectors.groupingBy(ServiceDictItemVO::getDictGroupCode));
        for (String dictGroupCode : dictGroupMap.keySet()) {
            dictionaryTransService.refreshCache(dictGroupCode,dictGroupMap.get(dictGroupCode).stream().collect(Collectors
                    .toMap(ServiceDictItemVO::getDictCode, ServiceDictItemVO::getDictDesc)));
        }
    }
}
