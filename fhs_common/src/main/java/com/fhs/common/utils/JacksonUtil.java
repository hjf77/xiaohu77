package com.fhs.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtil {
    public static <T> T jacksonDeserialize(String jsonStr, Class<T> classType) throws Exception {
        return new ObjectMapper().readValue(jsonStr, classType);
    }
    /**
     * JSON序列化
     *
     * @param object 对象
     * @return JSON字符串
     */
    public static String jacksonSerialize(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

}
