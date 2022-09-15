package com.fhs.generate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
@ApiModel("列表配置")
public class ListSettVO {

    @ApiModelProperty("列配置")
    private List<Colmn> columns;

    @ApiModelProperty("过滤条件配置")
    private List<Filter> filters;

    @ApiModelProperty("命名空间")
    private String namespace;

    @ApiModelProperty("ID字段名称")
    private String idFieldName;

    @ApiModelProperty("是否有新增按钮")
    private Boolean isHasAdd;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Filter{
        @ApiModelProperty("字段")
        private String name;
        @ApiModelProperty("标题")
        private String label;
        @ApiModelProperty("默认提示")
        private String placeholder;
        @ApiModelProperty("类型")
        private String type;
        @ApiModelProperty("比较符号")
        private String operation;
        @ApiModelProperty("是否多选")
        private Boolean multiple;
        @ApiModelProperty("显示字段")
        private String labelField;
        @ApiModelProperty("值字段")
        private String valueField;
        @ApiModelProperty("网址")
        private String url;
        @ApiModelProperty("字典编码")
        private String dictCode;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Colmn{
        private String name;
        private String label;
        private Integer width;
        private String type;
        private boolean isHasEdit;
        private boolean isHasDel;
    }
}
