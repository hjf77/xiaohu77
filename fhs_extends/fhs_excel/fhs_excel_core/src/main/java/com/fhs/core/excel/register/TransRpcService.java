package com.fhs.core.excel.register;

import java.util.Map;
import java.util.Set;

public interface TransRpcService {

    public Map<String,Object> unTrans(Set<Object> nameSet);

    /**
     * 目标业务namespace
     * @return
     */
    String namespace();

    default boolean isFeign(){
        return true;
    }

}
