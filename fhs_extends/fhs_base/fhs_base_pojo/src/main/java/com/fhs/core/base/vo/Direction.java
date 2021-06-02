package com.fhs.core.base.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        description = "排序对象"
)
public enum Direction {
    @ApiModelProperty(
            name = "ASC",
            notes = "升序"
    )
    ASC,
    @ApiModelProperty(
            name = "DESC",
            notes = "降序"
    )
    DESC;

    private Direction() {
    }

    public static Direction fromString(String value) {
        try {
            return valueOf(value.toUpperCase());
        } catch (Exception var2) {
            return ASC;
        }
    }
}
