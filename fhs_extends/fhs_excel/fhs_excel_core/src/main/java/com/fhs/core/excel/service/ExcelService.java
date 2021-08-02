package com.fhs.core.excel.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fhs.core.base.service.BaseService;
import com.fhs.core.excel.exception.ValidationException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

public interface ExcelService {

    public Workbook exportExcel(QueryWrapper query, Class<? extends BaseService> targetService, Class<?> doClass);

    public void importExcel(Object[][] datas, Object[] title, Class<? extends BaseService> targetService, Class<?> doClass) throws ValidationException;

    public void importExcel(MultipartFile file, Class<? extends BaseService> targetService, Class<?> doClass, int titleRowNum, int colNum) throws ValidationException;
}
