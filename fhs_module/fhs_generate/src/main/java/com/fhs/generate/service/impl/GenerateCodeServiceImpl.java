package com.fhs.generate.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.DateUtils;
import com.fhs.common.utils.FileUtils;
import com.fhs.common.utils.StringUtil;
import com.fhs.core.config.EConfig;
import com.fhs.generate.service.GenerateCodeService;
import com.fhs.generate.vo.FieldsVO;
import com.fhs.generate.vo.TableInfoVO;
import com.fhs.pagex.common.BeetlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class GenerateCodeServiceImpl implements GenerateCodeService {


    private static Set<String> needTransElementType = new HashSet<>();

    /**
     * form的类型对应的过滤条件类型
     */
    private static Map<String, String> typeConverter = new HashMap<>();

    /**
     * 各个页面元素对应的操作符
     */
    private static Map<String, String> operationMap = new HashMap<>();


    static {
        needTransElementType.add("select");
        needTransElementType.add("treeSelect");
        needTransElementType.add("radio");
        needTransElementType.add("checkbox");
        needTransElementType.add("switch");

        operationMap.put("dates", "between");
        operationMap.put("textarea", "like");
        operationMap.put("text", "like");
        operationMap.put("checkbox", "find_in_set_in");

        typeConverter.put("dates", "datetimerange");
        typeConverter.put("textarea", "text");
    }

    @Override
    public String[] generateCode(TableInfoVO tableInfoVO) {
        String tempPath = EConfig.getPathPropertiesValue("fileSavePath") + "/" + StringUtil.getUUID();
        new File(tempPath + "/components").mkdirs();
        String listVueCode = generateListVue(tableInfoVO);
        String listVueFile = tempPath + "/index.vue";
        try {
            FileUtils.write(new File(listVueFile), listVueCode, "UTF-8");
        } catch (IOException e) {
            log.error("写入文件错误", e);
        }
        String formVueCode = generateFormVue(tableInfoVO);
        String formVueFile = tempPath + "/components/" + tableInfoVO.getNamespace() + "Form.vue";
        try {
            FileUtils.write(new File(formVueFile), formVueCode, "UTF-8");
        } catch (IOException e) {
            log.error("写入文件错误", e);
        }
        return new String[]{listVueFile, formVueFile};
    }

    /**
     * 生成列表vue代码
     *
     * @param tableInfoVO 表对象
     * @return 代码
     */
    private String generateListVue(TableInfoVO tableInfoVO) {
        //过滤条件
        StringBuilder filters = new StringBuilder("\n");
        //列表展示列
        StringBuilder columns = new StringBuilder("\n");
        for (FieldsVO fieldsVO : tableInfoVO.getFieldsVOS()) {
            if (Constant.INT_TRUE == fieldsVO.getIsList()) {
                columns.append("                     {label: '" + fieldsVO.getComment() + "', name: '" +
                        (needTransElementType.contains(fieldsVO.getPageElementType())
                                ? fieldsVO.getCamelFieldName() + "Name" : fieldsVO.getCamelFieldName())
                        + "', width: 150},\n");
            }
            //如果没指定元素类型 或者不是过滤条件则看下一个字段
            if (CheckUtils.isNullOrEmpty(fieldsVO.getPageElementType()) || Constant.INT_TRUE != fieldsVO.getIsFilter()) {
                continue;
            }
            filters.append("                     " + this.getFilterJson(fieldsVO).toJSONString() + ",\n");
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tableComment", tableInfoVO.getComment());
        paramMap.put("author", tableInfoVO.getAuthor());
        paramMap.put("nowDate", DateUtils.formartDate(new Date(), DateUtils.DATETIME_PATTERN_DATE));
        paramMap.put("namespace", tableInfoVO.getNamespace());
        paramMap.put("filters", filters.toString());
        paramMap.put("columns", columns.toString());
        return BeetlUtil.renderBeelt("/template/list_vue.html", paramMap);
    }

    /**
     * 生成表单 vue代码
     *
     * @param tableInfoVO 表对象
     * @return 代码
     */
    private String generateFormVue(TableInfoVO tableInfoVO) {
        //过滤条件
        StringBuilder controls = new StringBuilder("\n");
        //列表展示列
        StringBuilder formData = new StringBuilder("\n");
        for (FieldsVO fieldsVO : tableInfoVO.getFieldsVOS()) {
            if (Constant.INT_FALSE == fieldsVO.getIsIgnore() && Constant.INT_TRUE != fieldsVO.getIsForm()) {
                formData.append("                     " + fieldsVO.getCamelFieldName() + ": this.init." + fieldsVO.getCamelFieldName()
                        + " ? this.init." + fieldsVO.getCamelFieldName() + " : null,\n");
            }
            //如果没指定元素类型 或者不是过滤条件则看下一个字段
            if (CheckUtils.isNullOrEmpty(fieldsVO.getPageElementType()) || Constant.INT_TRUE != fieldsVO.getIsForm()) {
                continue;
            }
            controls.append("                     " + this.getFormJson(fieldsVO).toJSONString() + ",\n");
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tableComment", tableInfoVO.getComment());
        paramMap.put("author", tableInfoVO.getAuthor());
        paramMap.put("nowDate", DateUtils.formartDate(new Date(), DateUtils.DATETIME_PATTERN_DATE));
        paramMap.put("namespace", tableInfoVO.getNamespace());
        paramMap.put("controls", controls.toString());
        paramMap.put("formData", formData.toString());
        return BeetlUtil.renderBeelt("/template/form_vue.html", paramMap);
    }


    /**
     * 获取过滤条件json
     *
     * @param fieldsVO
     * @return
     */
    private JSONObject getFilterJson(FieldsVO fieldsVO) {
        JSONObject result = new JSONObject();
        result.put("type", typeConverter.containsKey(fieldsVO.getPageElementType()) ? typeConverter.get(fieldsVO.getPageElementType()) : fieldsVO.getPageElementType());
        result.put("label", fieldsVO.getComment());
        result.put("name", fieldsVO.getCamelFieldName());
        result.put("operation", operationMap.containsKey(fieldsVO.getPageElementType()) ? operationMap.get(fieldsVO.getPageElementType()) : "=");
        result.putAll(fieldsVO.getExtParam());
        return result;
    }

    /**
     * 获取基础json
     *
     * @param fieldsVO
     * @return
     */
    private JSONObject getFormJson(FieldsVO fieldsVO) {
        JSONObject result = getFilterJson(fieldsVO);
        result.remove("operation");
        result.put("type", fieldsVO.getPageElementType());
        if (Constant.INT_TRUE == fieldsVO.getIsRequired()) {
            result.put("rule", "required");
        }
        return result;
    }


}

