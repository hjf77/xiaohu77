package com.fhs.core.base.pojo;

import com.fhs.common.utils.JsonUtil;
import com.fhs.common.utils.MapUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class BaseObject<T extends BaseObject> implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 重载方法
     *
     * @return json格式
     */
    public String toString() {
        return JsonUtil.bean2json(this);
    }



    /**
     * 将对象转换为map
     *
     * @return map
     */
    public Map<String, Object> asMap() {
        return MapUtils.bean2Map(this);
    }

    /**
     * 将对象转换为json
     *
     * @return
     */
    public String asJson() {
        return JsonUtil.bean2json(this);
    }


    @Override
    public int hashCode() {
        return this.asJson().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.asJson().equals(JsonUtil.bean2json(obj));
    }
}

