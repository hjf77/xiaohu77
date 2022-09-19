package com.fhs.generate.vo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;

@Data
@ApiModel("表单字段配置")
public class FormFiledVO {

    @ApiModelProperty("唯一键")
    private String field;

    @ApiModelProperty("字段名称")
    @NotEmpty(message = "字段名不能为空")
    private String title;

    @ApiModelProperty("类型")
    @NotEmpty(message = "类型不能为空")
    private String type;

    @ApiModelProperty("绑定字段")
    @NotEmpty(message = "绑定字段不能为空")
    private String name;

    @ApiModelProperty("是否必填")
    private Boolean required;

    @ApiModelProperty("宽度")
    private Integer width;

    @ApiModelProperty("默认值")
    private String defaultValue;

    @ApiModelProperty("扩展属性")
    private Props props;

    @ApiModelProperty("提示信息")
    private String info;

    @ApiModelProperty("选项")
    private String options;

    @ApiModelProperty("是否隐藏")
    private Boolean hidden;

    @ApiModelProperty("是否展示")
    private Boolean display;

    @ApiModelProperty("字典编码")
    private String dictCode;

    @ApiModelProperty("后端接口配置")
    private Effect effect;

    @ApiModelProperty("校验配置")
    private Validate[] validate;

    public Props getProps() {
        if (this.props != null) {
            return this.props;
        }
        return new Props();
    }

    public Effect getEffect() {
        if (this.effect != null) {
            return this.effect;
        }
        return new Effect();
    }

    @Data
    public static class Props extends JSONObject {

    }

    @Data
    public static class Validate {
        @ApiModelProperty("内置规则")
        private String ruleCode;
        @ApiModelProperty("使用内置规则还是自定义规则")
        private String type;
        @ApiModelProperty("验证方式")
        private String mode;
        @ApiModelProperty("正则")
        private String pattern;
        @ApiModelProperty("最小长度或者值")
        private Integer min;
        @ApiModelProperty("最大长度或者值")
        private Integer max;
        @ApiModelProperty("错误信息")
        private String message;
    }

    @Data
    public static class Effect {

        @ApiModelProperty("远程url配置")
        private Fetch fetch;

        public Fetch getFetch(){
            if(this.fetch!=null){
                return fetch;
            }
            return new Fetch();
        }

        @Data
        public static class Fetch {
            @ApiModelProperty("附带数据")
            private HashMap<String, String> data;
            @ApiModelProperty("url")
            private String action;
            @ApiModelProperty("http method")
            private String method;
            @ApiModelProperty("标题字段")
            private String labelField;
            @ApiModelProperty("值字段")
            private String valueField;
        }

    }

}
