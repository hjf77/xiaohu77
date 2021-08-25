package com.fhs.core.excel.service.impl;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fhs.basics.api.trans.WordBookTransServiceImpl;
import com.fhs.common.excel.ExcelUtils;
import com.fhs.common.spring.SpringContextUtil;
import com.fhs.common.utils.*;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.base.service.BaseService;
import com.fhs.core.excel.exception.ValidationException;
import com.fhs.core.valid.checker.ParamChecker;
import com.fhs.excel.dto.ExcelImportSett;
import com.fhs.excel.service.TransRpcService;
import com.fhs.core.excel.register.TransRpcServiceRegister;
import com.fhs.core.excel.service.ExcelService;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.excel.anno.IgnoreExport;
import com.fhs.excel.anno.Order;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
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
     * @param query         查询条件
     * @param targetService 对应的 Service
     * @param doClass       对应的 DO class
     * @return Workbook Excel对象
     */
    @Override
    public Workbook exportExcel(QueryWrapper query, BaseService targetService, Class<?> doClass) {
        BaseService service = targetService;
        List<?> dos = service.selectListMP(query);
        if (null == dos) {
            return null;
        }

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet();

        List<Field> fields = ReflectUtils.getAllField(doClass);


        //根据Order注解排序Excel头标题
        SortedMap<Integer, Field> fieldsMap = new TreeMap<>();
        int emptyOrder = 888;
        int titleNum = 0;
        for (Field field : fields) {
            if (null == field.getAnnotation(ApiModelProperty.class)
                    || field.isAnnotationPresent(IgnoreExport.class) || field.isAnnotationPresent(TableId.class)) {
                continue;
            }
            titleNum++;
            Order order = field.getAnnotation(Order.class);
            if (null == order) {
                fieldsMap.put(emptyOrder++, field);
            } else {
                fieldsMap.put(order.value(), field);
            }
        }
        Object[] titleArray = new Object[titleNum];
        Object[][] dataArray = new Object[dos.size()][titleNum];
        int num = 0;
        for (Map.Entry<Integer, Field> entry : fieldsMap.entrySet()) {
            titleArray[num] = getFieldRemark(entry.getValue());
            num++;
        }

        for (int i = 0; i < dos.size(); i++) {
            num = 0;
            for (Map.Entry<Integer, Field> entry : fieldsMap.entrySet()) {
                try {
                    Field field = entry.getValue();
                    field.setAccessible(true);
                    Object fieldVal = field.get(dos.get(i));
                    dataArray[i][num] = getFieldValue(field, dos.get(i));
                    num++;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        ExcelUtils.initSheet07(sheet, dataArray, titleArray, null, null, 1);
        return wb;
    }

    /**
     * 获取字段值
     *
     * @param field 字段
     * @param doObj 对象
     * @return
     */
    private String getFieldValue(Field field, Object doObj) {
        if (doObj == null) {
            return null;
        }
        if (field.isAnnotationPresent(Trans.class)) {
            Trans trans = field.getAnnotation(Trans.class);
            //如果翻译带ref返回null，这个列不改导出
            if (CheckUtils.isNotEmpty(trans.ref())) {
                return null;
            }
            //字典的话取字典 如果带ref 则不拼接
            if (TransType.WORD_BOOK.equals(trans.type()) && doObj instanceof VO) {
                return ((VO) doObj).getTransMap().get(field.getName() + "Name");
            }
            //auto trans处理
            if (TransType.AUTO_TRANS.equals(trans.type()) && doObj instanceof VO) {
                //如果jsonkey 是空则返回null
                if (CheckUtils.isNullOrEmpty(trans.jsonKey())) {
                    return null;
                }
                return ((VO) doObj).getTransMap().get(trans.jsonKey());
            }
        }
        field.setAccessible(true);
        Object value = null;
        try {
            value = getGetMethod(doObj , field.getName());
        } catch (IllegalAccessException e) {
            log.error("参数错误",e);
            return null;
        }catch (Exception e){
            log.error("反射调用错误",e);
        }
        //如果没有加翻译注解的id，注解导出null
        if (field.getName().endsWith("Id")) {
            return StringUtil.toString(value);
        }
        if (value instanceof Date) {
            //如果加了日期格式化则就按照格式化的来
            if (field.isAnnotationPresent(JSONField.class) && CheckUtils.isNotEmpty(field.getAnnotation(JSONField.class).format())) {
                return DateUtils.formartDate((Date) value, field.getAnnotation(JSONField.class).format());
            }
            //如果没加格式化代码则就直接导出yyyy-MM-dd HH:mm:ss
            return DateUtils.formartDate((Date) value, DateUtils.DATETIME_PATTERN);
        }
        return StringUtil.toString(value);
    }

    /**
     * 根据属性，获取get方法
     * @param ob 对象
     * @param name 属性名
     * @return
     * @throws Exception
     */
    public static Object getGetMethod(Object ob , String name)throws Exception{
        Method[] m = ob.getClass().getMethods();
        for(int i = 0;i < m.length;i++){
            if(("get"+name).toLowerCase().equals(m[i].getName().toLowerCase())){
                return m[i].invoke(ob);
            }
        }
        return null;
    }

    /**
     * 获取列中文注释
     *
     * @param field
     * @return
     */
    private String getFieldRemark(Field field) {
        String fieldName = field.getAnnotation(ApiModelProperty.class).value();
        if (fieldName.contains("(")) {
            return fieldName.substring(0, fieldName.indexOf("("));
        }
        return fieldName;
    }

    /**
     * Excel导入功能
     * 其中包括基本校验基于DO Class注解
     * 注解 NotEmpty 非空校验
     * 注解 Length 长度校验，包括max min
     * <p>
     * 反翻译
     * 通过Trans注解进行反翻译服务
     * <p>
     * 通过ExcelUtils工具类获取datas和title
     *
     * @param dataArray     Excel数据数组
     * @param titleArray    Excel标题数组
     * @param targetService 对应的service
     * @param importSett    对应的 DO Class
     * @throws ValidationException 返回整个Excel验证结果
     */
    public void importExcel(Object[][] dataArray, Object[] titleArray, BaseService targetService, ExcelImportSett importSett) throws Exception {
        importSett.getDoIniter().init(importSett.getDoModel());
        BaseService service = targetService;
        List<Field> fields = ReflectUtils.getAnnotationField(importSett.getDoModel().getClass(), ApiModelProperty.class);
        WordBookTransServiceImpl transService = SpringContextUtil.getBeanByName(WordBookTransServiceImpl.class);
        //excel错误格式提醒
        StringBuilder valiStr = new StringBuilder();
        //需要反翻译的名称
        Map<String, Set<String>> needTrans = new HashMap<>();

        //初始化数据集合
        List<Object> doList = new ArrayList<>();
        for (int i = 0; i < dataArray.length; i++) {
            doList.add(JsonUtils.jacksonDeserialize(JsonUtils.jacksonSerialize(importSett.getDoModel()), importSett.getDoModel().getClass()));
        }

        for (int i = 0; i < titleArray.length; i++) {
            for (Field field : fields) {
                String fieldName = getFieldRemark(field);
                if (fieldName.equals(titleArray[i])
                        && field.getAnnotation(TableField.class).exist()) {
                    for (int j = 0; j < dataArray.length; j++) {
                        Object objDo = doList.get(j);
                        Object data = dataArray[j][i];
                        if (CheckUtils.isNullOrEmpty(ConverterUtils.toString(data))) {
                            continue;
                        }
                        //反翻译
                        Trans trans = field.getAnnotation(Trans.class);
                        if (trans != null) {
                            if (trans.type().equals(TransType.WORD_BOOK)) {
                                if (data.toString().contains(",")) {
                                    String[] strs = data.toString().split(",");
                                    StringBuilder tranStr = new StringBuilder();
                                    for (int k = 0; k < strs.length; k++) {
                                        String tran = transService.getUnWordBookTransMap().get(trans.key() + "_" + strs[k]);
                                        if (StringUtils.isBlank(tran)) {
                                            valiStr.append("“" + data + "”找不到对应翻译，请检查第" + (j + 2) + "行“" + fieldName + "”列;\r\n");
                                        }
                                        tranStr.append(tran).append(",");
                                    }
                                    tranStr.deleteCharAt(tranStr.length() - 1);
                                    ReflectUtils.setValue(objDo, field, tranStr.toString());
                                } else {
                                    String tranStr = transService.getUnWordBookTransMap().get(trans.key() + "_" + data);
                                    if (StringUtils.isBlank(tranStr)) {
                                        valiStr.append("“" + data + "”找不到对应翻译，请检查第" + (j + 2) + "行“" + fieldName + "”列;\r\n");
                                        continue;
                                    }
                                    ReflectUtils.setValue(objDo, field, field.getGenericType().equals(String.class) ? tranStr : ConverterUtils.toInteger(tranStr));
                                }
                            } else if (trans.type().equals(TransType.AUTO_TRANS)) {
                                String namespace = trans.key() + "_" + field.getName();
                                if (!needTrans.containsKey(namespace)) {
                                    needTrans.put(namespace, new HashSet<>());
                                }
                                needTrans.get(namespace).add(ConverterUtils.toString(data));
                                ReflectUtils.setValue(objDo, field, data);
                            }
                        } else {
                            //不需要反翻译时进行非空和长度校验
                            if (field.getAnnotation(NotEmpty.class) != null
                                    && StringUtils.isBlank(data.toString())) {
                                valiStr.append(fieldName + "不能为空，请检查第" + (j + 2) + "行“" + fieldName + "”列;\r\n");
                                continue;
                            }
                            Length length = field.getAnnotation(Length.class);
                            if (length != null) {
                                if (data.toString().length() > length.max()) {
                                    valiStr.append(fieldName + "长度不能超过" + length.max() + "，请检查第" + (j + 2) + "行“" + fieldName + "”列;\r\n");
                                    continue;
                                }
                                if (data.toString().length() < length.min()) {
                                    valiStr.append(fieldName + "长度不能小于" + length.max() + "，请检查第" + (j + 2) + "行“" + fieldName + "”列;\r\n");
                                    continue;
                                }
                            }
                            if (field.getGenericType().equals(Integer.class)) {
                                ReflectUtils.setValue(objDo, field, ConverterUtils.toInteger(data));
                            } else if (field.getGenericType().equals(Double.class)) {
                                ReflectUtils.setValue(objDo, field, ConverterUtils.toDouble(data));
                            } else if (field.getGenericType().equals(Date.class)) {
                                if (StringUtils.isBlank(data.toString())) {
                                    continue;
                                }
                                try {
                                    ReflectUtils.setValue(objDo, field, DateUtils.parseStr(data.toString()));
                                } catch (Exception e) {
                                    valiStr.append(fieldName + "列请输入正确的时间格式，请检查第" + (j + 2) + "行“" + fieldName + "”列;\r\n");
                                    continue;
                                }
                            } else {
                                ReflectUtils.setValue(objDo, field, data);
                            }
                        }
                    }
                }
            }
        }
        //名称反翻译
        for (String namespaceKey : needTrans.keySet()) {
            String[] strs = namespaceKey.split("_");
            String fieldName = strs[1];
            //解析namespace获取到反向翻译服务
            TransRpcService transRpcService = transRpcServiceRegister.getTransRpcService(strs[0].contains("#") ? strs[0].split("#")[0] : strs[0]);
            ParamChecker.isNotNull(transRpcService, namespaceKey + "，没有找到对应的反向翻译服务，请到对应的service实现：TransRpcService");
            Map<String, Object> unTrans = transRpcService.unTrans(needTrans.get(namespaceKey));
            for (Object objDo : doList) {
                Field field = ReflectUtils.getDeclaredField(objDo.getClass(), fieldName);
                Object needTranName = ReflectUtils.getValue(objDo, fieldName);
                if (CheckUtils.isNullOrEmpty(needTranName)) {
                    ReflectUtils.setValue(objDo, field, null);
                    continue;
                }
                Object fieldValue = unTrans.get(needTranName);
                if (null == fieldValue) {
                    String fieldNameCh = field.getAnnotation(ApiModelProperty.class).value();
                    valiStr.append(needTranName + "找不到对应翻译，请确保“" + fieldNameCh + "”列“" + needTranName + "”填写正确;\r\n");
                    continue;
                }
                ReflectUtils.setValue(objDo, field, fieldValue);
            }
        }


        if (doList.size() > 0) {
            notNullNotEmptyCheck(doList, valiStr, titleArray);
            //如果Excel有数据验证错误，抛出异常并报告所有错误位置。
            if (valiStr.length() != 0) {
                throw new ValidationException(valiStr.toString());
            }
            service.batchInsert(doList);
        } else {
            throw new ValidationException("您选中的excel中不包含任何有效数据，请检查");
        }
    }

    /**
     * 非空校验
     *
     * @param doList
     * @param valiStr
     * @return
     */
    public StringBuilder notNullNotEmptyCheck(List<Object> doList, StringBuilder valiStr, Object[] titleArray) throws IllegalAccessException {
        Set<Field> hasCheckField = new HashSet<>();
        List<Field> notEmptyFieldList = ReflectUtils.getAnnotationField(doList.get(0).getClass(), NotEmpty.class);
        if (!notEmptyFieldList.isEmpty()) {
            hasCheckField.addAll(notEmptyFieldList);
        }
        List<Field> notNullFieldList = ReflectUtils.getAnnotationField(doList.get(0).getClass(), NotNull.class);
        if (!notNullFieldList.isEmpty()) {
            hasCheckField.addAll(notNullFieldList);
        }
        Set<Object> excelIncludeTitleSet = new HashSet<>(Arrays.asList(titleArray));
        for (Field field : hasCheckField) {
            String fieldName = getFieldRemark(field);
            // excel模板中不包含的不校验
            if (!excelIncludeTitleSet.contains(fieldName)) {
                continue;
            }
            field.setAccessible(true);
            for (int i = 0; i < doList.size(); i++) {
                if (CheckUtils.isNullOrEmpty(field.get(doList.get(i)))) {
                    valiStr.append("第" + (i + 2) + "行第" + fieldName + "不可为空");
                }
            }
        }
        return valiStr;
    }

    /**
     * Excel导入功能
     * 其中包括基本校验基于DO Class注解
     * 注解 NotEmpty 非空校验
     * 注解 Length 长度校验，包括max min
     * <p>
     * 反翻译
     * 通过Trans注解进行反翻译服务
     *
     * @param file          文件对象
     * @param targetService 对应的service
     * @param doClass       对应的 DO Class
     * @param importSett    导入配置
     * @throws ValidationException 返回整个Excel验证结果
     */
    @Override
    public void importExcel(MultipartFile file, BaseService targetService, Class<?> doClass, ExcelImportSett importSett) throws Exception {

        Object[][] dataArray = new Object[0][];
        Object[] titleArray = new Object[0];
        try {
            dataArray = ExcelUtils.importExcel(file, importSett.getTitleRowNum(), importSett.getColNum());
            titleArray = ExcelUtils.getExcelTitleRow(file, importSett.getTitleRowNum(), importSett.getColNum());
            for (int i = 0; i < titleArray.length; i++) {
                String tempTitle = ConverterUtils.toString(titleArray[i]);
                if (tempTitle.contains("(")) {
                    titleArray[i] = tempTitle.substring(0, tempTitle.indexOf("("));
                }
            }
        } catch (IOException e) {
            throw new ValidationException("获取文件IO流失败", e);
        }

        importExcel(dataArray, titleArray, targetService, importSett);
    }
}
