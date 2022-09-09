package com.fhs.generate.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.generate.util.ColumnNameUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@ApiModel("表字段信息")
public class FieldsVO {

    @ApiModelProperty("字段名")
    private String name;

    @ApiModelProperty("字段注释")
    private String label;

    @ApiModelProperty("列表是否显示")
    private Integer isListShow = Constant.INT_TRUE;

    @ApiModelProperty("是否需要翻译")
    private Integer isTrans = Constant.INT_FALSE;

    @ApiModelProperty("字典分组编码")
    private String dictCode;

    @ApiModelProperty("目标表")
    private String table;

    @ApiModelProperty("表字段")
    private  String titleFiled;

    @ApiModelProperty("翻译唯一键")
    private String uniqueField;

    @ApiModelProperty("key")
    private String key;

    @ApiModelProperty("字段类型")
    private String type;



    @ApiModelProperty("宽度")
    private Integer width;

    @ApiModelProperty("是否必填 NO必填 YES非必填")
    private String isNullable;

    @ApiModelProperty("是否参与列表过滤")
    private Integer isFilter = 0;

    @ApiModelProperty("是否忽略")
    private Integer isIgnore;


    @ApiModelProperty("长度")
    private Integer length;


    @ApiModelProperty("是否列表显示")
    private Integer isList = 0;

    @ApiModelProperty("页面元素")
    private String elementType;

    @ApiModelProperty("接口地址")
    private String api;

    @ApiModelProperty("labelField")
    private String labelField;

    @ApiModelProperty("valueField")
    private String valueField;

    @ApiModelProperty("扩展信息")
    private Map<String, Object> extParam = new HashMap<>();


    @JsonIgnore
    public String getCamelFieldName() {
        return ColumnNameUtil.underlineToCamel(this.name);
    }

    public Integer getLength() {
        if (this.type.contains("varchar")) {
            return ConverterUtils.toInt(this.type.replace("varchar(", "").replace(")", ""));
        }
        if (this.type.contains("text")) {
            return 1000;
        }
        return 0;
    }


    public String getLabel() {
        if (label == null) {
            return null;
        }
        if (label.contains("(")) {
            return label.substring(0, label.indexOf("("));
        }
        return label;
    }

}
