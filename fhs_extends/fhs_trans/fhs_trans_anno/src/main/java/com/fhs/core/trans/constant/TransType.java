package com.fhs.core.trans.constant;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * 翻译类型
 *
 * @author user
 * @date 2020-05-19 10:15:15
 */
public interface TransType {

    /**
     * 自动翻译
     */
    String AUTO_TRANS = "auto";

    /**
     * 字典
     */
    String WORD_BOOK = "wordbook";


}
