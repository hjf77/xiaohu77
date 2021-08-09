package com.fhs.excel.service;

import com.fhs.excel.form.NamesForm;

import java.util.Map;
import java.util.Set;

/**
 * excel 反向翻译接口
 */
public interface TransRpcService {

    /**
     * 根据名称获取主键
     * @param names
     * @return
     */
    default Map<String,Object> unTrans(Set<String> names){
        return this.doUnTrans(new NamesForm(names));
    }

    Map<String,Object> doUnTrans(NamesForm namesForm);

    /**
     * 目标业务namespace
     * @return
     */
    String namespace();

    default boolean isFeign(){
        return true;
    }

}
