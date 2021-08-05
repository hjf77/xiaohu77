package com.fhs.core.excel.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fhs.core.base.service.BaseService;
import com.fhs.core.excel.exception.ValidationException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * 公共的excel导出导入服务
 */
public interface ExcelService {

    /**
     * 导出excel
     * @param query 过滤条件
     * @param targetService 服务
     * @param doClass do类
     * @return
     */
    Workbook exportExcel(QueryWrapper query, BaseService targetService, Class<?> doClass);

    /**
     *
     * @param datas
     * @param title
     * @param targetService
     * @param doClass
     * @throws ValidationException
     */
    void importExcel(Object[][] datas, Object[] title, BaseService targetService, Class<?> doClass) throws ValidationException;

    /**
     * 解析excel 入库
     * @param file 文件
     * @param targetService baseservice
     * @param doClass do的类
     * @param titleRowNum  title第几行
     * @param colNum 一共多少列
     * @throws ValidationException
     */
    void importExcel(MultipartFile file,  BaseService targetService, Class<?> doClass, int titleRowNum, int colNum) throws ValidationException;
}
