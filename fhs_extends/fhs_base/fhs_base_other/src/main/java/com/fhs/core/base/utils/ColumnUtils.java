package com.fhs.core.base.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fhs.common.utils.ReflectUtils;
import com.fhs.common.utils.StringUtils;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.trans.vo.VO;
import com.fhs.excel.anno.IgnoreExport;
import io.swagger.annotations.ApiModelProperty;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Description 处理PO数据列
 * @Author ZhangDong
 * @Date 2022年10月31日 17:37 星期一
 */
public class ColumnUtils {

    /**
     * 解析PO的所有列数据
     *
     * @param poClass
     * @return
     */
    public static <P> LinkedHashMap<String, String> getAllColumn(Class<P> poClass) {
        LinkedHashMap<String, String> allField = new LinkedHashMap<>();
        List<Field> fields = ReflectUtils.getAllField(poClass);
        for (Field field : fields) {
            String fieldName = field.getName();
            String propertyName = fieldName;
            TableField tableField = field.getAnnotation(TableField.class);
            JsonIgnore jsonIgnore = field.getAnnotation(JsonIgnore.class);
            IgnoreExport IgnoreExport = field.getAnnotation(IgnoreExport.class);
            //没有被忽略的字段
            if (tableField != null && jsonIgnore == null && IgnoreExport == null) {
                Class<?> fieldType = field.getType();
                String typeName = fieldType.getName();
                if (typeName.startsWith("java.lang") || (typeName.startsWith(" java.util") && !fieldType.isAssignableFrom(Collection.class))) {
                    Trans trans = field.getAnnotation(Trans.class);
                    //处理带翻译的字段
                    if (trans != null) {
                        String alias = trans.alias();
                        String type = trans.type();
                        //字典翻译
                        if (TransType.DICTIONARY.equals(type)) {
                            handleDictionary(allField, field, alias, fieldName);
                        }
                        //简单翻译
                        if (TransType.SIMPLE.equals(type)) {
                            handleSimple(allField, trans, alias, propertyName);
                        }
                    } else {
                        //对没有翻译的字段处理
                        putToAllField(allField, field, fieldName);
                    }
                }
            }
        }
        return allField;
    }

    /**
     * 处理简单翻译字段
     * @param allField  需要添加到Map
     * @param trans     翻译注解
     * @param alias     别名
     * @param propertyName  字段属性名称
     */
    private static void handleSimple(LinkedHashMap<String, String> allField, Trans trans, String alias, String propertyName) {
        Class<? extends VO> target = trans.target();
        String fieldName;
        String[] transFields = trans.fields();
        for (String transField : transFields) {
            try {
                Field targetField = target.getDeclaredField(transField);
                //处理翻译带别名的字段
                if (StringUtils.isEmpty(alias)) {
                    fieldName = "transMap." + propertyName + StringUtils.firstCharUpperCase(transField);
                } else {
                    fieldName = "transMap." + alias + StringUtils.firstCharUpperCase(transField);
                }
                putToAllField(allField, targetField, fieldName);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理字典翻译字段
     * @param allField      需要添加到Map
     * @param targetField   需要翻译的字段
     * @param alias         翻译别名
     * @param fieldName     被翻译字段名
     */
    private static void handleDictionary(LinkedHashMap<String, String> allField, Field targetField, String alias, String fieldName) {
        if (StringUtils.isEmpty(alias)) {
            fieldName = "transMap." + fieldName + "Name";
        } else {
            fieldName = "transMap." + alias + "Name";
        }
        putToAllField(allField, targetField, fieldName);
    }

    /**
     * 添加字段列到Map
     * @param allField      需要添加到Map
     * @param targetField   需要添加的字段
     * @param fieldName     被添加字段名
     */
    private static void putToAllField(LinkedHashMap<String, String> allField, Field targetField, String fieldName) {
        ApiModelProperty apiModelProperty = targetField.getAnnotation(ApiModelProperty.class);
        if (apiModelProperty != null) {
            allField.put(fieldName, apiModelProperty.value());
        } else {
            allField.put(fieldName, "该字段没注释");
        }
    }
}
