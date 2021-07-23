package com.fhs.core.base.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Data
@ApiModel(
        description = "查询条件"
)
@AllArgsConstructor
public class QueryField implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(
            name = "property",
            notes = "实体类属性"
    )
    private String property;
    @ApiModelProperty(
            name = "operation",
            notes = "比较符",
            example = "="
    )
    private String operation;
    @ApiModelProperty(
            name = "value",
            notes = "比较值"
    )
    private Object value;
    @ApiModelProperty(
            name = "relation",
            notes = "同一个分组内的多个条件之间的组合关系，默认为and",
            example = "AND"
    )
    private String relation;
    @ApiModelProperty(
            name = "group",
            notes = "查询条件分组，默认分组为main，多个分组默认按照and的关系组合在一起",
            example = "main"
    )
    private String group;

    public QueryField() {
        this.operation = "=";
        this.relation = "AND";
        this.group = "main";
    }

}
