package com.fhs.core.base.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel("更新某几个字段表单")
public class UpdateFieldVO<D> {
    @ApiModelProperty("需要被更新的")
    @NotEmpty(message = "doContent不可为空")
    private D doContent;

    @ApiModelProperty("id集合逗号分隔")
    @NotEmpty(message = "ids不可为空")
    private String ids;
}
