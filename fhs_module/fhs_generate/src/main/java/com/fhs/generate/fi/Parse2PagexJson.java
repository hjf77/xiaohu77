package com.fhs.generate.fi;

import com.alibaba.fastjson.JSONObject;

@FunctionalInterface
public interface Parse2PagexJson<R,T> {

    /**
     * 把页面配置的JSON格式化为pagex的json
     * @param source 页面配置的json
     * @return page json
     */
    R parse(T source);
}
