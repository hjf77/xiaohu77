package com.fhs.core.excel.service.impl;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fhs.basics.api.trans.WordBookTransServiceImpl;
import com.fhs.common.excel.ExcelUtils;
import com.fhs.common.spring.SpringContextUtil;
import com.fhs.common.utils.ReflectUtils;
import com.fhs.core.base.service.BaseService;
import com.fhs.core.excel.exception.ValidationException;
import com.fhs.core.excel.register.TransRpcService;
import com.fhs.core.excel.register.TransRpcServiceRegister;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    public TransRpcServiceRegister transRpcServiceRegister;

    /**
     * Excel导出功能
     * 需要导出的DoClass列中必须添加ApiModelProperty和ExcelExport注解
     * ApiModelProperty 注解用于获取字段中文描述
     * ExcelExport 注解用于确定哪些字段需要导出
     * 需要导出的DoClass列中选择添加Order注解
     * Order注解用于导出字段排序，默认正序
     *
     * @param query 查询条件
     * @param targetService 对应的 Service
     * @param doClass 对应的 DO class
     * @return Workbook Excel对象
     */
    @Override
    public Workbook exportExcel(QueryWrapper query, Class<? extends BaseService> targetService, Class<?> doClass) {
        BaseService service = SpringContextUtil.getBeanByName(targetService);
        List<?> dos = service.selectListMP(query);
        if (null == dos){
            return null;
        }

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet();

        List<Field> fields = ReflectUtils.getAllField(doClass);

        Object[][] dataArray = new Object[dos.size()][fields.size()];
        Object[] titleArray = new Object[fields.size()];

        //根据Order注解排序Excel头标题
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

    /**
     * Excel导入功能
     * 其中包括基本校验基于DO Class注解
     * 注解 NotEmpty 非空校验
     * 注解 Length 长度校验，包括max min
     *
     * 反翻译
     * 通过Trans注解进行反翻译服务
     *
     * 通过ExcelUtils工具类获取datas和title
     * @param dataArray Excel数据数组
     * @param titleArray Excel标题数组
     * @param targetService 对应的service
     * @param doClass 对应的 DO Class
     * @throws ValidationException 返回整个Excel验证结果
     */
    @Override
    public void importExcel(Object[][] dataArray, Object[] titleArray, Class<? extends BaseService> targetService, Class<?> doClass) throws ValidationException {

        BaseService service = SpringContextUtil.getBeanByName(targetService);
        List<Field> fields = ReflectUtils.getAnnotationField(doClass, ApiModelProperty.class);
        WordBookTransServiceImpl transService = SpringContextUtil.getBeanByName(WordBookTransServiceImpl.class);
        //excel错误格式提醒
        StringBuilder valiStr = new StringBuilder();
        //需要反翻译的名称
        Map<String, Set<Object>> needTrans = new HashMap<>();

        //初始化数据集合
        List<Object> doList = new ArrayList<>();
        for (int i = 0; i < dataArray.length; i++){
            doList.add(ReflectUtils.newInstance(doClass));
        }

        for (int i = 0; i < titleArray.length; i++){
            for (Field field : fields){
                String fieldName = field.getAnnotation(ApiModelProperty.class).value();
                if (fieldName.equals(titleArray[i])
                        && field.getAnnotation(TableField.class).exist()){
                    for (int j = 0; j < dataArray.length; j ++){
                        Object objDo = doList.get(j);
                        Object data = dataArray[j][i];
                        //反翻译
                        Trans trans = field.getAnnotation(Trans.class);
                        if (trans != null){
                            if (trans.type().equals(TransType.WORD_BOOK)){
                                String tranStr = transService.getUnWordBookTransMap().get(trans.key() + "_" + dataArray[j][i]);
                                if (StringUtils.isBlank(tranStr)){
                                    valiStr.append(fieldName + "找不到对应翻译，请检查第" + (j+2) + "行“" + fieldName + "”列;\r\n");
                                }
                                ReflectUtils.setValue(objDo, field, tranStr);
                            } else if (trans.type().equals(TransType.AUTO_TRANS)){
                                String namespace = trans.key() + "_" + field.getName();
                                String namespaceKey = trans.key() + "_" + (j+2) + "_" + fieldName;
                                if (!needTrans.containsKey(namespace)){
                                    needTrans.put(namespace, new HashSet<>());
                                }
                                needTrans.get(namespace).add(dataArray[j][i]);
                                ReflectUtils.setValue(objDo, field, dataArray[j][i]);
                            }
                        } else {
                            //不需要反翻译时进行非空和长度校验
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
                            ReflectUtils.setValue(objDo, field, dataArray[j][i]);
                        }
                    }
                }
            }
        }
        //名称反翻译
        for (String namespaceKey : needTrans.keySet()){
            String[] strs = namespaceKey.split("_");
            String namespace = strs[0];
            String fieldName = strs[1];
            TransRpcService transRpcService = transRpcServiceRegister.getTransRpcService(namespace);
            Map<String, Object> unTrans = transRpcService.unTrans(needTrans.get(namespaceKey));
            for (Object objDo : doList){
                Field field = ReflectUtils.getDeclaredField(objDo.getClass(), fieldName);
                Object needTranName = ReflectUtils.getValue(objDo, fieldName);
                Object fieldValue = unTrans.get(needTranName);
                if (null == fieldValue){
                    String fieldNameCh = field.getAnnotation(ApiModelProperty.class).value();
                    valiStr.append(needTranName + "找不到对应翻译，请确保“" + fieldNameCh + "”列“" + needTranName + "”填写正确;\r\n");
                    continue;
                }
                ReflectUtils.setValue(objDo, field, fieldValue);
            }
        }
        //如果Excel有数据验证错误，抛出异常并报告所有错误位置。
        if (valiStr.length() != 0){
            throw new ValidationException(valiStr.toString());
        }
        service.batchInsert(doList);
    }

    /**
     * Excel导入功能
     * 其中包括基本校验基于DO Class注解
     * 注解 NotEmpty 非空校验
     * 注解 Length 长度校验，包括max min
     *
     * 反翻译
     * 通过Trans注解进行反翻译服务
     *
     * @param file 文件对象
     * @param targetService 对应的service
     * @param doClass 对应的 DO Class
     * @param titleRowNum title所在行，默认0
     * @param colNum 共多少列
     * @throws ValidationException 返回整个Excel验证结果
     */
    @Override
    public void importExcel(MultipartFile file, Class<? extends BaseService> targetService, Class<?> doClass, int titleRowNum, int colNum) throws ValidationException {

        Object[][] dataArray = new Object[0][];
        Object[] titleArray = new Object[0];
        try {
            dataArray = ExcelUtils.importExcel(file, titleRowNum, colNum);
            titleArray = ExcelUtils.getExcelTitleRow(file, titleRowNum, colNum);
        } catch (IOException e) {
            throw new ValidationException("获取文件IO流失败", e);
        }

        importExcel(dataArray, titleArray, targetService, doClass);
    }
}
