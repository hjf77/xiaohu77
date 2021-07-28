package com.fhs.core.excel.service.impl;

import com.fhs.common.excel.ExcelUtils;
import com.fhs.common.spring.SpringContextUtil;
import com.fhs.common.utils.ReflectUtils;
import com.fhs.core.base.service.BaseService;
import com.fhs.core.base.vo.QueryFilter;
import com.fhs.core.excel.service.ExcelService;
import com.fhs.excel.anno.ExcelExport;
import com.fhs.excel.anno.Order;
import io.swagger.annotations.ApiModelProperty;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public Workbook exportExcel(QueryFilter query, Class<? extends BaseService> targetService, Class<?> doclass) {
        BaseService service = SpringContextUtil.getBeanByName(targetService);
        List<?> dos = service.selectListMP(query.asWrapper(doclass));
        if (null == dos){
            return null;
        }

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet();

        List<Field> fields = ReflectUtils.getAllField(doclass);

        Object[][] dataArray = new Object[dos.size()][fields.size()];
        Object[] titleArray = new Object[fields.size()];

        SortedMap<Integer, Field> fieldsMap = new TreeMap<>();
        int emptyOrder = 888;
        for (Field field : fields){
            if (null == field.getAnnotation(ApiModelProperty.class)
                    || null == field.getAnnotation(ExcelExport.class)){
                continue;
            }
            Order order = field.getAnnotation(Order.class);
            if (null == order){
                fieldsMap.put(emptyOrder ++, field);
            } else {
                fieldsMap.put(order.value(), field);
            }
        }

        int num = 0;
        for (Map.Entry<Integer, Field> entry : fieldsMap.entrySet()){
            titleArray[num] = entry.getValue().getAnnotation(ApiModelProperty.class).value();
            num ++;
        }

        for (int i = 0; i < dos.size(); i++){
            num = 0;
            for (Map.Entry<Integer, Field> entry : fieldsMap.entrySet()){
                try {
                    Field field = entry.getValue();
                    field.setAccessible(true);
                    Object fieldVal = field.get(dos.get(i));
                    dataArray[i][num] = fieldVal;
                    num ++;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        ExcelUtils.initSheet07(sheet, dataArray, titleArray, null, null, 1);

        return wb;
    }

    @Override
    public String importExcel(Object[][] datas, Object[] title, Class<? extends BaseService> targetService, Class<?> DOClass) {



        return null;
    }

}
