package com.fhs.generate.vo;

import com.google.gson.JsonObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
@ApiModel("表单配置")
public class FormSettVO {

    public List<Control> controls;





    public static class Control extends HashMap<String,Object>{
        public void setType(String type){
            put("type",type);
        }

        public void setName(String name){
            put("name",name);
        }

        public void setLabel(String label){
            put("label",label);
        }

        public void setMock(String mock){
            put("mock",mock);
        }

        public void setRule(List<JsonObject> rules){
            put("rule",rules);
        }

        public void setUrl(String url){
            put("url",url);
        }

        public void setLabelField(String labelField){
            put("labelField",labelField);
        }

        public void setValueField(String valueField){
            put("valueField",valueField);
        }

        public void setDictCode(String dictCode){
            put("dictCode",dictCode);
        }

    }
}
