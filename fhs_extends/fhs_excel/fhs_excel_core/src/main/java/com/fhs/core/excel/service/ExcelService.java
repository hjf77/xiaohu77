package com.fhs.core.excel.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fhs.core.base.service.BaseService;
import com.fhs.core.base.vo.QueryFilter;
import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelService {

    public Workbook exportExcel(QueryFilter query, Class<? extends BaseService> targetService, Class<?> DOClass);

    public String importExcel(Object[][] datas, Object[] title, Class<? extends BaseService> targetService, Class<?> DOClass);
}
