package com.fhs.excel.dto;

import com.fhs.excel.service.DoIniter;
import com.fhs.excel.service.ValidationAfter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * excel导入配置
 *
 * @param <V>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExcelImportSett<V> {

    /**
     * do初始化器
     */
    private DoIniter<V> voIniter;

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
    private V voModel;

    /**
     * 调用的Controller
     */
    private Class<?> controllerClass;

    /**
     * 自定义校验
     */
    private ValidationAfter<V> validationAfter;

}
