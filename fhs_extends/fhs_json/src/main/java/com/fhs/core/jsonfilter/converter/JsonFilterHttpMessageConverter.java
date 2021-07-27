package com.fhs.core.jsonfilter.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fhs.core.jsonfilter.bean.JsonFilterObject;
import com.fhs.core.jsonfilter.bean.VoConverterObject;
import com.fhs.core.jsonfilter.filter.AllPropertyNotSeralizerFilter;
import com.fhs.core.jsonfilter.filter.JsonFormatterFilter;
import com.fhs.core.jsonfilter.filter.SimpleSerializerFilter;
import com.fhs.core.jsonfilter.filter.TransFormatterFilter;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;

/**
 * update from https://github.com/Liuyis/jsonfilter
 * by jackwong
 * json转换器
 * @author jackwong
 * @date 2020-05-19 14:48:18
 */
public class JsonFilterHttpMessageConverter extends FastJsonHttpMessageConverter{

    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        if(obj instanceof JsonFilterObject){
            JsonFilterObject jsonFilterObject = (JsonFilterObject) obj;
            OutputStream out = outputMessage.getBody();
            SimpleSerializerFilter simpleSerializerFilter = new SimpleSerializerFilter(jsonFilterObject.getIncludes(), jsonFilterObject.getExcludes());
            String text = JSON.toJSONString(jsonFilterObject.getJsonObject(),simpleSerializerFilter,super.getFastJsonConfig().getSerializerFeatures());
            byte[] bytes = text.getBytes(super.getFastJsonConfig().getCharset());
            out.write(bytes);
        }else if(obj instanceof VoConverterObject){
            VoConverterObject voConverterObject = (VoConverterObject) obj;
            OutputStream out = outputMessage.getBody();
            String text = JSON.toJSONString(voConverterObject.getJsonObject(),new SerializeFilter[]{
                new AllPropertyNotSeralizerFilter(voConverterObject),new JsonFormatterFilter(voConverterObject)
            },super.getFastJsonConfig().getSerializerFeatures());
            byte[] bytes = text.getBytes(super.getFastJsonConfig().getCharset());
            out.write(bytes);
        }else {
            OutputStream out = outputMessage.getBody();
            String text = JSON.toJSONString(obj,new SerializeFilter[]{
                    new TransFormatterFilter()
            }, super.getFastJsonConfig().getSerializerFeatures());
            byte[] bytes = text.getBytes(super.getFastJsonConfig().getCharset());
            out.write(bytes);
        }
    }


}
