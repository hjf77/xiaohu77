package com.fhs.pagex.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fhs.common.excel.ExcelUtils;
import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.ReflectUtils;
import com.fhs.common.utils.StringUtil;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.config.EConfig;
import com.fhs.core.result.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * excel导出工具类
 * @author user
 * @date 2020-05-19 13:43:30
 */
@Slf4j
public class ExcelExportTools {




    /**
     * 格式化导出数据
     * @param fieldMap 字段
     * @param dataList 需要被格式化的数据
     * @return 格式化后的数据
     */
    public static Object[][] parseExportData( final Map<String,String> fieldMap, List<?> dataList){
        final Object[][] rows = new Object[dataList.size()][fieldMap.size()];
        Set<String> fieldSet = fieldMap.keySet();
        String transFieldName = null;
        boolean isMap = false;
        if(dataList.size()!=0)
        {
            if(dataList.get(0) instanceof  Map)
            {
                isMap = true;
            }
        }
        for(int i = 0;i<dataList.size();i++)
        {
            Object rowData = dataList.get(i);
            Object[] row = new Object[fieldSet.size()];
            rows[i] = row;
            int fieldIndex = 0;
            for(String field:fieldSet)
            {
                Object value = null;
                if(field.contains("transMap"))
                {
                    transFieldName = field.replace("transMap.","");
                    if(!isMap)
                    {
                        value = ((VO)rowData).getTransMap().get(transFieldName);
                    }
                    else
                    {
                        value = ((Map)((Map)rowData).get("transMap")).get(transFieldName);
                    }
                }
                else
                {
                    value = ReflectUtils.getValue(rowData,field);
                }
                row[fieldIndex] = value;
                fieldIndex ++;
            }
        }
        return  rows;
    }

    /**
     * 格式化导出数据
     * @param request request
     * @param dataList 需要被格式化的数据
     * @return 格式化后的数据
     */
    public static Object[][] parseExportData(HttpServletRequest request, List<?> dataList){
        final Map<String,String> fieldMap = (Map<String, String>) request.getSession().getAttribute("exportField");
        return parseExportData(  fieldMap, dataList);
    }

    /**
     * 获取excel的title
     * @param request request
     * @return title集合
     */
    public static String[] getExportTitleArray(HttpServletRequest request){
        final Map<String,String> fieldMap = (Map<String, String>) request.getSession().getAttribute("exportField");
        String[] titles = new String[fieldMap.size()];
        Set<String> set = fieldMap.keySet();
        int i = 0;
        for(String field:set)
        {
            titles[i] = fieldMap.get(field);
            i ++;
        }
        return titles;
    }

    /**
     * 将导出的列配置信息缓存到session中
     * @param fieldSett 导出配置
     * @param request request
     * @return 成功
     */
    public static HttpResult setExportField(String fieldSett, HttpServletRequest request){
        JSONArray fields = JSON.parseArray(fieldSett);
        // key field value title
        final Map<String,String> fieldMap = new LinkedHashMap<>();
        JSONObject tempObj = null;
        for(int i=0;i<fields.size();i++)
        {
            tempObj = fields.getJSONObject(i);
            fieldMap.put(tempObj.getString("field"),tempObj.getString("title"));
        }
        request.getSession().setAttribute("exportField",fieldMap );
        return HttpResult.success();
    }


    /**
     * 有数据导出excel by jackwang
     * @param dataList  数据集合
     * @param request request
     * @param response response
     */
    public  static   void exportExcel(List<?> dataList,HttpServletRequest request, HttpServletResponse response)
    {
        Object[][] rows = parseExportData(request,dataList);
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet();
        String[] titleArray = getExportTitleArray(request);
        ExcelUtils.initSheet07(sheet, rows, titleArray, null, null,1);
        String excelName = "data_list.xlsx";
        if(CheckUtils.isNotEmpty(request.getParameter("excelName")))
        {
            try {
                excelName = URLDecoder.decode(request.getParameter("excelName"),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        try {
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(excelName + ".xlsx","UTF-8"));
            wb.write(response.getOutputStream());
        } catch (IOException e) {
            log.error("导出excel出错，URI:" + request.getRequestURI());
            log.error("导出excel出错",e);
        }
    }


    /**
     * 有数据导出excel by jackwang
     * @param dataList  数据集合
     * @param request request
     */
    public static File exportExcel(List<?> dataList, HttpServletRequest request)
    {
        Object[][] rows = parseExportData(request,dataList);
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet();
        String[] titleArray = getExportTitleArray(request);
        ExcelUtils.initSheet07(sheet, rows, titleArray, null, null,1);
        try {
            File excelFile = new File(EConfig.getPathPropertiesValue("fileSavePath") + "/temp/excel/" + StringUtil.getUUID() + ".xlsx");
            wb.write(new FileOutputStream(excelFile));
        } catch (IOException e) {
            log.error("导出excel出错",e);
        }
        return null;
    }



}
