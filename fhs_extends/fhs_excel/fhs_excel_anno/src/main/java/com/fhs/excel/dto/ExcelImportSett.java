package com.fhs.excel.dto;

import com.fhs.excel.service.DoIniter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * excel导入配置
 *
 * @param <D>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExcelImportSett<D> {

    /**
     * do初始化器
     */
    private DoIniter<D> doIniter;

    /**
     * 标题行 从0开始
     */
    private int titleRowNum;


    /**
     * 一共有多少列
     */
    private int colNum;

    /**
     * 基础的do
     */
    private D doModel;

}
