package com.fhs.core.excel.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fhs.core.base.service.BaseService;
import com.fhs.core.excel.exception.ValidationException;
import com.fhs.excel.dto.ExcelImportSett;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

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
    Workbook exportExcel(QueryWrapper query, BaseService targetService, Class<?> doClass);

    /**
     * @Description: Excel导出功能
     * 需要导出的DoClass列中必须添加ApiModelProperty和ExcelExport注解
     * ApiModelProperty 注解用于获取字段中文描述
     * ExcelExport 注解用于确定哪些字段需要导出
     * 需要导出的DoClass列中选择添加Order注解
     * Order注解用于导出字段排序，默认正序
     * @author: cdpha
     * @date: 16:21 2021/10/15
     * @param query 查询条件
     * @param targetService 对应的Service
     * @param doClass 对应的 DO class
     * @param commonFieldSet 需要导出的公共字段(createUser,createTime,updateUser,updateTime)
     * @return org.apache.poi.ss.usermodel.Workbook
     **/
    Workbook exportExcel(QueryWrapper query, BaseService targetService, Class<?> doClass, Set<String> commonFieldSet);

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
