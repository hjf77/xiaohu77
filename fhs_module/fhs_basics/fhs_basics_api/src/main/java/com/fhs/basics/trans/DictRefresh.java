package com.fhs.basics.trans;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fhs.basics.service.CommonLanguageService;
import com.fhs.basics.service.ServiceDictItemService;
import com.fhs.basics.vo.CommonLanguageVO;
import com.fhs.basics.vo.ServiceDictItemVO;
import com.fhs.core.trans.constant.TransType;
import com.fhs.trans.fi.LocaleGetter;
import com.fhs.trans.listener.TransMessageListener;
import com.fhs.trans.service.impl.DictionaryTransService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 字典翻译服务
 *
 * @author wanglei
 * @date 2020-05-18 14:41:20
 */
@Slf4j
@Component
public class DictRefresh implements ApplicationRunner {

    private static final Set<String> support_Language = new HashSet<>(Arrays.asList("zh-CN", "en", "ar"));


    @Autowired
    private ServiceDictItemService serviceDictItemService;

    @Autowired
    private DictionaryTransService dictionaryTransService;

    @Autowired
    private CommonLanguageService commonLanguageService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        TransMessageListener.regTransRefresher(TransType.DICTIONARY, this::refreshCache);
        refreshCache(null);
    }

    /**
     * 刷新字典缓存
     *
     * @param message
     */
    public void refreshCache(Map<String, Object> message) {
        Map<String, String> transMap = new HashMap<>();
        List<CommonLanguageVO> commonLanguageVOS = commonLanguageService.select();
        commonLanguageVOS.forEach(c -> {
            transMap.put(c.getName() + "_zh-CN", c.getValuesZh());
            transMap.put(c.getName() + "_en", c.getValuesEn());
            transMap.put(c.getName() + "_ar", c.getValuesAr());
        });
        dictionaryTransService.refreshCache(TransType.SIMPLE, transMap);
        Map<String, CommonLanguageVO> languageVOMap = commonLanguageVOS.stream().collect(Collectors.toMap(CommonLanguageVO::getName, Function.identity()));
        List<ServiceDictItemVO> dictItemVOList = serviceDictItemService.selectListMP(new LambdaQueryWrapper<>());
        List<ServiceDictItemVO> dictItemVOListTemp = new ArrayList<>();
        for (ServiceDictItemVO dictItem : dictItemVOList) {
            String code = dictItem.getDictGroupCode() + "_" + dictItem.getDictCode();
            if (languageVOMap.containsKey(code)) {
                CommonLanguageVO languageVO = languageVOMap.get(code);
                dictItemVOListTemp.add(serviceDictItemService.p2v(ServiceDictItemVO.builder().dictGroupCode(dictItem.getDictGroupCode())
                        .dictCode(dictItem.getDictCode() + "_zh-CN").dictDesc(languageVO.getValuesZh()).build()));
                dictItemVOListTemp.add(serviceDictItemService.p2v(ServiceDictItemVO.builder().dictGroupCode(dictItem.getDictGroupCode())
                        .dictCode(dictItem.getDictCode() + "_en").dictDesc(languageVO.getValuesEn()).build()));
                dictItemVOListTemp.add(serviceDictItemService.p2v(ServiceDictItemVO.builder().dictGroupCode(dictItem.getDictGroupCode())
                        .dictCode(dictItem.getDictCode() + "_ar").dictDesc(languageVO.getValuesAr()).build()));
            } else {
                dictItemVOListTemp.add(serviceDictItemService.p2v(ServiceDictItemVO.builder().dictGroupCode(dictItem.getDictGroupCode())
                        .dictCode(dictItem.getDictCode() + "_zh-CN").dictDesc("国际化未配置").build()));
                dictItemVOListTemp.add(serviceDictItemService.p2v(ServiceDictItemVO.builder().dictGroupCode(dictItem.getDictGroupCode())
                        .dictCode(dictItem.getDictCode() + "_en").dictDesc("国际化未配置").build()));
                dictItemVOListTemp.add(serviceDictItemService.p2v(ServiceDictItemVO.builder().dictGroupCode(dictItem.getDictGroupCode())
                        .dictCode(dictItem.getDictCode() + "_ar").dictDesc("国际化未配置").build()));
            }
        }
        dictItemVOList.addAll(dictItemVOListTemp);
        Map<String, List<ServiceDictItemVO>> dictGroupMap = dictItemVOList.stream().collect(Collectors.groupingBy(ServiceDictItemVO::getDictGroupCode));
        for (String dictGroupCode : dictGroupMap.keySet()) {
            dictionaryTransService.refreshCache(dictGroupCode, dictGroupMap.get(dictGroupCode).stream().collect(Collectors
                    .toMap(ServiceDictItemVO::getDictCode, ServiceDictItemVO::getDictDesc)));
        }
        dictionaryTransService.openI18n(new LocaleGetter() {
            @Override
            public String getLanguageTag() {
                Locale locale = LocaleContextHolder.getLocale();
                String language = locale.toLanguageTag();//springboot可以国际化可以通过这段代码告诉框架当前语言环境
                if (support_Language.contains(language)) {
                    return language;
                }
                return "en";
            }
        });
    }

}
