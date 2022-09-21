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
public class CommonLanguageExportVO {
    /**
     * 序号
     */
    @ExcelColumn(order = 0, title = "序号")
    private Integer index;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    @ExcelColumn(order = 1, title = "名称")
    private String name;

    /**
     * 中文
     */
    @ApiModelProperty(value = "中文")
    @ExcelColumn(order = 2, title = "中文")
    private String valuesZh;

    /**
     * 英文
     */
    @ApiModelProperty(value = "英文")
    @ExcelColumn(order = 3, title = "英文")
    private String valuesEn;

    /**
     * 阿拉伯文
     */
    @ApiModelProperty(value = "阿拉伯文")
    @ExcelColumn(order = 3, title = "阿拉伯文")
    private String valuesAr;
}
