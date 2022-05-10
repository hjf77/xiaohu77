package com.fhs.demo.controller;

import com.fhs.basics.service.ServiceDictItemService;
import com.fhs.common.utils.IdHelper;
import com.fhs.core.exception.ParamException;
import com.fhs.demo.pojo.ExcelField;
import com.fhs.demo.utils.ExcelBaseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

/**
 * excel模板
 */
@Slf4j
@Api(tags = "excel模板")
@RestController
@RequestMapping("/excel_template/")
public class ExcelTemplateAction {



    @Autowired
    private IdHelper idHelper;

    @Autowired
    private ServiceDictItemService dictItemService;


    @Data
    public static class ExportDto{
        private String code;

        private String name;

        private String rate;
    }

    @PostMapping("exportTemplate")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "导出模板", notes = "数据来源", httpMethod = "POST")
    public void exportTemplate(HttpServletResponse response,  @RequestParam("name") String name, @RequestBody List<ExcelField> fields) {
        if (StringUtils.isBlank(name)) {
            name = "模板";
        }
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(name + ".xlsx", "utf-8"));
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            ExcelBaseUtils.exportTemplate(response.getOutputStream(), fields, dictItemService);
        } catch (Exception e) {
            throw new ParamException("模板导出异常");
        }
    }


  /*  @PostMapping("importExcel")
    @ApiOperation(value = "导入")
    public ResponseEntity importExcel(@RequestParam("file") MultipartFile file) {
        List<ExcelField> fields = new ArrayList<>();
        ExcelField field = new ExcelField();
        field.setName("商品编码");
        field.setCode("code");
        field.setRule("required");
        field.setWith(4000);
        field.setImportFlag(true);
        field.setExport(true);
        fields.add(field);
        ExcelField field2 = new ExcelField();
        field2.setName("商品名称");
        field2.setCode("name");
        field2.setRule("required");
        field2.setWith(4000);
        field2.setImportFlag(true);
        field2.setNotRepeat(true);
        field2.setExport(true);
        fields.add(field2);
        ExcelField field3 = new ExcelField();
        field3.setName("税率");
        field3.setCode("rate");
        field3.setRule("required");
        field3.setType("select");
        field3.setDictCode("monthly_type");
        field3.setImportFlag(true);
        field3.setExport(true);
        fields.add(field3);

        try {
            List<ExportDto> exportDtos = ExcelBaseUtils.easyImportExcel(file.getInputStream(), fields, ExportDto.class, dictItemService);
        } catch (Exception e) {
            throw new ParamException(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }*/

}
