package com.fhs.core.base.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(
        description = "排序对象"
)
public class FieldSort implements Serializable {
    private static final long serialVersionUID = -1712830705595375365L;
    @ApiModelProperty(
            name = "property",
            notes = "排序字段"
    )
    private String property;
    @ApiModelProperty(
            name = "direction",
            notes = "排序方向",
            example = "ASC"
    )
    private Direction direction;

    public FieldSort() {
        this.direction = Direction.ASC;
    }

    public FieldSort(String property) {
        this(property, Direction.ASC);
    }

    public FieldSort(String property, Direction direction) {
        this.direction = direction;
        this.property = property;
    }

    public static FieldSort create(String property, String direction) {
        return new FieldSort(property, Direction.fromString(direction));
    }


}
