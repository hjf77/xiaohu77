package com.fhs.core.base.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description 指定字段导出ExcelVO
 * @Author ZhangDong
 * @Date 2022年10月28日 10:39 星期五
 */
@Data
public class ExcelExportFieldVO {

    @ApiModelProperty("导出数据文件名")
    private String fileName;

    @ApiModelProperty("需要查询数据的IDS")
    private String ids;

    @ApiModelProperty("需要导出数据的字段")
    private List<ExcelFieldVO> fieldVOList;
}
