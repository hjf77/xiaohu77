package com.fhs.basics.trans;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fhs.basics.service.ServiceDictItemService;
import com.fhs.basics.vo.ServiceDictItemVO;
import com.fhs.core.trans.constant.TransType;
import com.fhs.trans.listener.TransMessageListener;
import com.fhs.trans.service.impl.DictionaryTransService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典翻译服务
 *
 * @author wanglei
 * @date 2020-05-18 14:41:20
 */
@Slf4j
@Component
public class DictRefresh implements  ApplicationRunner {


    @Autowired
    private ServiceDictItemService serviceDictItemService;

    @Autowired
    private DictionaryTransService dictionaryTransService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        TransMessageListener.regTransRefresher(TransType.DICTIONARY, this::refreshCache);
        refreshCache(null);
    }

    /**
     * 刷新字典缓存
     * @param message
     */
    public void refreshCache(Map<String, Object> message){
        List<ServiceDictItemVO> dictItemVOList = serviceDictItemService.selectListMP(new LambdaQueryWrapper<>());
        Map<String,List<ServiceDictItemVO>> dictGroupMap = dictItemVOList.stream().collect(Collectors.groupingBy(ServiceDictItemVO::getDictGroupCode));
        for (String dictGroupCode : dictGroupMap.keySet()) {
            dictionaryTransService.refreshCache(dictGroupCode,dictGroupMap.get(dictGroupCode).stream().collect(Collectors
                    .toMap(ServiceDictItemVO::getDictCode, ServiceDictItemVO::getDictDesc)));
        }
    }
}
