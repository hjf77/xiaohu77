package com.fhs.core.excel.service;

import com.fhs.core.base.service.BaseService;
import com.fhs.core.base.vo.QueryFilter;
import com.fhs.core.excel.exception.ValidationException;
import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelService {

    public Workbook exportExcel(QueryFilter query, Class<? extends BaseService> targetService, Class<?> doClass);

    public void importExcel(Object[][] datas, Object[] title, Class<? extends BaseService> targetService, Class<?> doClass) throws ValidationException;
}
