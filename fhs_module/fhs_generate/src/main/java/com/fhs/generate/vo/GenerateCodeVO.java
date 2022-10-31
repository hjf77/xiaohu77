package com.fhs.generate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotEmpty;

@Data
@ApiModel("生成代码配置")
public class GenerateCodeVO {

    @NotEmpty(message = "生成类型必填")
    @ApiModelProperty("生成类型-form,list,all")
    private String genType;

    @ApiModelProperty("开发者名字")
    @NotEmpty(message = "开发者名字必填")
    private String author;

    @ApiModelProperty("表名字")
    @NotEmpty(message = "表名字必填")
    private String tableName;

    @ApiModelProperty("数据库")
    @NotEmpty(message = "数据库必填")
    private String tableSchema;

    @ApiModelProperty("微服务名称")
    @NotEmpty(message = "微服务名称必填")
    private String microServiceName;
}
