package com.fhs.core.jsonfilter.serializer;


import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.ConverterUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 字符串序列化为数组
 *
 * @author wanglei
 */
public class Str2ArrayValueSerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                      int features) throws IOException {
        String str = ConverterUtils.toString(object);
        if (CheckUtils.isNullOrEmpty(str)) {
            serializer.write(new String[]{});
            return;
        }
        String[] split = str.split(",");
        serializer.write(split);
    }
}
