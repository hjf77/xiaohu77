package com.fhs.core.excel.service.impl;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fhs.basics.api.trans.WordBookTransServiceImpl;
import com.fhs.common.excel.ExcelUtils;
import com.fhs.common.spring.SpringContextUtil;
import com.fhs.common.utils.ReflectUtils;
import com.fhs.core.base.service.BaseService;
import com.fhs.core.base.vo.QueryFilter;
import com.fhs.core.excel.exception.ValidationException;
import com.fhs.core.excel.service.ExcelService;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.excel.anno.ExcelExport;
import com.fhs.excel.anno.Order;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import java.lang.reflect.Field;
import java.util.*;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public Workbook exportExcel(QueryFilter query, Class<? extends BaseService> targetService, Class<?> doClass) {
        BaseService service = SpringContextUtil.getBeanByName(targetService);
        List<?> dos = service.selectListMP(query.asWrapper(doClass));
        if (null == dos){
            return null;
        }

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet();

        List<Field> fields = ReflectUtils.getAllField(doClass);

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
    public void importExcel(Object[][] datas, Object[] title, Class<? extends BaseService> targetService, Class<?> doClass) throws ValidationException {

        BaseService service = SpringContextUtil.getBeanByName(targetService);
        List<Field> fields = ReflectUtils.getAnnotationField(doClass, ApiModelProperty.class);
        WordBookTransServiceImpl transService = SpringContextUtil.getBeanByName(WordBookTransServiceImpl.class);
        //excel格式检查
        StringBuilder valiStr = new StringBuilder();

        List<Object> doList = new ArrayList<>();
        for (int i = 0; i < datas.length; i++){
            doList.add(ReflectUtils.newInstance(doClass));
        }

        for (int i = 0; i < title.length; i++){
            for (Field field : fields){
                String fieldName = field.getAnnotation(ApiModelProperty.class).value();
                if (fieldName.equals(title[i])
                        && field.getAnnotation(TableField.class).exist()){
                    for (int j = 0; j < datas.length; j ++){
                        Object objDo = doList.get(j);
                        Object data = datas[j][i];
                        //反翻译
                        Trans trans = field.getAnnotation(Trans.class);
                        if (trans != null){
                            if (trans.type().equals(TransType.WORD_BOOK)){
                                String tranStr = transService.getUnWordBookTransMap().get(trans.key() + "_" + datas[j][i]);
                                if (StringUtils.isBlank(tranStr)){
                                    valiStr.append(fieldName + "找不到对应翻译，请检查第" + (j+2) + "行“" + fieldName + "”列;\r\n");
                                }
                                ReflectUtils.setValue(objDo, field, tranStr);
                            }
                        } else {
                            //不需要反翻译是进行非空和长度校验
                            if (field.getAnnotation(NotEmpty.class) != null
                                    && StringUtils.isBlank(data.toString())){
                                valiStr.append(fieldName + "不能为空，请检查第" + (j+2) + "行“" + fieldName + "”列;\r\n");
                            }
                            Length length = field.getAnnotation(Length.class);
                            if (length != null){
                                if (data.toString().length() > length.max() ){
                                    valiStr.append(fieldName + "长度不能超过" + length.max() + "，请检查第" + (j+2) + "行“" + fieldName + "”列;\r\n");
                                }
                                if (data.toString().length() < length.min() ){
                                    valiStr.append(fieldName + "长度不能小于" + length.max() + "，请检查第" + (j+2) + "行“" + fieldName + "”列;\r\n");
                                }

                            }
                            ReflectUtils.setValue(objDo, field, datas[j][i]);
                        }
                    }
                }
            }
        }
        if (valiStr.length() != 0){
            throw new ValidationException(valiStr.toString());
        }
        service.batchInsert(doList);
    }
}
