package com.fhs.basics.vo;

import com.github.liaochong.myexcel.core.annotation.ExcelColumn;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonLanguageExportEnVO {
    /**
     * 序号
     */
    @ExcelColumn(order = 0, title = "order")
    private Integer index;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    @ExcelColumn(order = 1, title = "name")
    private String name;

    /**
     * 中文
     */
    @ApiModelProperty(value = "中文")
    @ExcelColumn(order = 2, title = "chinese")
    private String valuesZh;

    /**
     * 英文
     */
    @ApiModelProperty(value = "英文")
    @ExcelColumn(order = 3, title = "english")
    private String valuesEn;

    /**
     * 阿拉伯文
     */
    @ApiModelProperty(value = "阿拉伯文")
    @ExcelColumn(order = 3, title = "arabic")
    private String valuesAr;
}
