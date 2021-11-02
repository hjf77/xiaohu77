package com.fhs.app.controller;

import com.alibaba.fastjson.annotation.JSONField;
import com.fhs.core.trans.vo.VO;
import com.fhs.core.jsonfilter.anno.AutoArray;
import com.fhs.core.jsonfilter.serializer.Str2ArrayValueSerializer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("testJson")
public class JSONTestContorller {

    @Data
    public static class Persion implements VO {
        @AutoArray
        @JSONField(serializeUsing = Str2ArrayValueSerializer.class)
        private String hobby;
        private String name;

        @Override
        public Map<String, String> getTransMap() {
            return null;
        }

        @Override
        public Object getPkey() {
            return null;
        }

        public Integer getIsDelete() {
            return null;
        }
    }

    @PostMapping("test")
    public Persion test(@RequestBody Persion persion){

        return  persion;
    }
}
