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
public class CommonLanguageExportArVO {
    /**
     * 序号
     */
    @ExcelColumn(order = 0, title = "الرقم المتسلسل")
    private Integer index;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    @ExcelColumn(order = 1, title = "الاسم")
    private String name;

    /**
     * 中文
     */
    @ApiModelProperty(value = "中文")
    @ExcelColumn(order = 2, title = "الصينية")
    private String valuesZh;

    /**
     * 英文
     */
    @ApiModelProperty(value = "英文")
    @ExcelColumn(order = 3, title = "الإنكليزية")
    private String valuesEn;

    /**
     * 阿拉伯文
     */
    @ApiModelProperty(value = "阿拉伯文")
    @ExcelColumn(order = 3, title = "العربية")
    private String valuesAr;
}
