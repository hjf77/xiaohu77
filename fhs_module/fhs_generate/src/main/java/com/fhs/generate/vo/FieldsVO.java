package com.fhs.generate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("表字段信息")
public class FieldsVO {

    @ApiModelProperty("字段名")
    private String filedName;

    @ApiModelProperty("字段注释")
    private String comment;

    @ApiModelProperty("字段类型")
    private String type;

    @ApiModelProperty("是否必填")
    private Integer isNotNull;
}