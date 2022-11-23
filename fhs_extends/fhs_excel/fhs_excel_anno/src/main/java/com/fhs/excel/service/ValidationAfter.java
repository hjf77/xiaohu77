package com.fhs.excel.service;

import java.util.List;

/**
 * @Description fhs-framework-com.fhs.excel.service
 * @Author ZhangDong
 * @Date 2022年11月23日 11:41 星期三
 */
@FunctionalInterface
public interface ValidationAfter {

    /**
     * 导入数据校验完成之后执行自定义校验规则
     * @param doList        需要导入的数据
     * @param valiStr       校验的消息
     */
    void excelValidationAfter(List<Object> doList, StringBuilder valiStr);
}
