package com.fhs.generate.vo;

import com.google.gson.JsonObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ApiModel("表单配置")
public class FormSettVO {

    @ApiModelProperty("表单项")
    public List<Control> controls;

    @ApiModelProperty("默认值")
    public String defaultValueData;

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

        public void setRule(List<FormSettVO.Rule> rules){
            put("rule",rules);
        }

        public void setUrl(String url){
            put("url",url);
        }

        public void setQuerys(Map<String,String> querys){
            put("querys",querys);
        }

        public void setMethodType(String methodType){
            put("methodType",methodType);
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

        public void setWidth(Integer width){put("width",width);}

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Rule{
        @ApiModelProperty("触发方式")
        private String trigger;
        @ApiModelProperty("数据类型(数组和日期要提供)")
        private String type;
        @ApiModelProperty("必填的话给true")
        private Boolean required;
        @ApiModelProperty("提示消息")
        private String message;
        @ApiModelProperty("内置规则")
        private String ruleCode;
        @ApiModelProperty("正则表达式")
        private String pattern;
        @ApiModelProperty("最小长度或者值")
        private Integer min;
        @ApiModelProperty("最大长度或者值")
        private Integer max;
    }
}
