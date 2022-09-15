package com.fhs.generate.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

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

    public static class Props{
        @ApiModelProperty("最大长度")
        private Integer maxLength;
        @ApiModelProperty("最小长度")
        private Integer minLength;
        @ApiModelProperty("最大值")
        private String max;
        @ApiModelProperty("最小值")
        private String min;
        @JSONField(name = "_optionType")
        private Integer optionType;
        
    }

}
