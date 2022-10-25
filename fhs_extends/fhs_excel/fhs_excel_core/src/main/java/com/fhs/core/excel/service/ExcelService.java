package com.fhs.core.excel.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaJoinQueryWrapper;
import com.fhs.core.base.service.BaseService;
import com.fhs.core.excel.exception.ValidationException;
import com.fhs.excel.dto.ExcelImportSett;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * 公共的excel导出导入服务
 */
public interface ExcelService {

    /**
     * 导出excel
     *
     * @param query         过滤条件
     * @param targetService 服务
     * @param doClass       do类
     * @return
     */
    Workbook exportExcel(LambdaJoinQueryWrapper query, BaseService targetService, Class<?> doClass);


    /**
     * 解析excel 入库
     *
     * @param file          文件
     * @param targetService baseservice
     * @param doClass       do的类
     * @param importSett    其他配置
     * @throws ValidationException
     */
    void importExcel(MultipartFile file, BaseService targetService, Class<?> doClass, ExcelImportSett importSett) throws Exception;


}
