package com.fhs.generate.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.*;
import com.fhs.core.config.EConfig;
import com.fhs.core.exception.ParamException;
import com.fhs.generate.constant.GenerateConstant;
import com.fhs.generate.service.GenerateCodeService;
import com.fhs.generate.util.ColumnNameUtil;
import com.fhs.generate.vo.ListFieldVO;
import com.fhs.generate.vo.ListSettVO;
import com.fhs.generate.vo.TableInfoVO;
import com.fhs.beetl.common.BeetlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 这段代码正在重构种
 */
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

    private Map<String, SFunction<ListFieldVO, ListSettVO.Filter>> filterParserMap = new HashMap<>();


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
        String tempPath = EConfig.getPathPropertiesValue("fileSavePath") + "/" + StringUtils.getUUID();
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
        for (ListFieldVO fieldsVO : tableInfoVO.getFields()) {
            if (Constant.INT_TRUE == fieldsVO.getIsList()) {
                columns.append("                     {label: '" + fieldsVO.getLabel() + "', name: '" +
                        (needTransElementType.contains(fieldsVO.getElementType())
                                ? fieldsVO.getCamelFieldName(false) + "Name" : fieldsVO.getCamelFieldName(false))
                        + "', width: 150},\n");
            }
            //如果没指定元素类型 或者不是过滤条件则看下一个字段
            if (CheckUtils.isNullOrEmpty(fieldsVO.getElementType()) || Constant.INT_TRUE != fieldsVO.getIsFilter()) {
                continue;
            }
            filters.append("                     " + this.getFilterJson(fieldsVO).toJSONString() + ",\n");
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tableComment", tableInfoVO.getTableComment());
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
       /* for (FieldsVO fieldsVO : tableInfoVO.getFields()) {
            if (Constant.INT_FALSE == fieldsVO.getIsIgnore() && Constant.INT_TRUE != fieldsVO.getIsForm()) {
                formData.append("                     " + fieldsVO.getCamelFieldName() + ": this.init." + fieldsVO.getCamelFieldName()
                        + " ? this.init." + fieldsVO.getCamelFieldName() + " : null,\n");
            }
            //如果没指定元素类型 或者不是过滤条件则看下一个字段
            if (CheckUtils.isNullOrEmpty(fieldsVO.getElementType()) || Constant.INT_TRUE != fieldsVO.getIsForm()) {
                continue;
            }
            controls.append("                     " + this.getFormJson(fieldsVO).toJSONString() + ",\n");
        }*/
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tableComment", tableInfoVO.getTableComment());
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
    private JSONObject getFilterJson(ListFieldVO fieldsVO) {
        JSONObject result = new JSONObject();
        result.put("type", typeConverter.containsKey(fieldsVO.getElementType()) ? typeConverter.get(fieldsVO.getElementType()) : fieldsVO.getElementType());
        result.put("label", fieldsVO.getLabel());
        result.put("name", fieldsVO.getCamelFieldName(true));
        result.put("operation", operationMap.containsKey(fieldsVO.getElementType()) ? operationMap.get(fieldsVO.getElementType()) : "=");
        result.putAll(fieldsVO.getExtParam());
        return result;
    }

    /**
     * 获取基础json
     *
     * @param fieldsVO
     * @return
     */
    private JSONObject getFormJson(ListFieldVO fieldsVO) {
        JSONObject result = getFilterJson(fieldsVO);
        result.remove("operation");
        result.put("type", fieldsVO.getElementType());
        return result;
    }


    public ListSettVO getListJson(TableInfoVO tableInfoVO) {
        ListSettVO listSettVO = new ListSettVO();
        listSettVO.setColumns(parseColumn(tableInfoVO));
        listSettVO.setFilters(parseFilter(tableInfoVO));
        listSettVO.setNamespace(ColumnNameUtil.underlineToCamel(tableInfoVO.getTableName().replace("t_", "")));
        List<ListFieldVO> fieldVOS = Arrays.asList(tableInfoVO.getFields()).stream().filter(fieldVO -> {
            return "PRI".equals(fieldVO.getKey());
        }).collect(Collectors.toList());
        if (fieldVOS.isEmpty()) {
            throw new ParamException("表没主键，联系后台设置哦");
        }
        listSettVO.setIdFieldName(fieldVOS.get(0).getCamelFieldName(false));
        Set<String> operationCodes = tableInfoVO.getTableOperation() == null ? new HashSet<>() :
                new HashSet<>(Arrays.asList(tableInfoVO.getTableOperation().split(",")));
        listSettVO.setIsHasAdd(operationCodes.contains(GenerateConstant.OPERATOR_TYPE_ADD));
        return listSettVO;
    }



    /**
     * 格式化列
     *
     * @param tableInfoVO 表配置
     * @return
     */
    public List<ListSettVO.Colmn> parseColumn(TableInfoVO tableInfoVO) {
        List<ListSettVO.Colmn> result = new ArrayList<>();
        List<ListFieldVO> listFieldVOList = Arrays.asList(tableInfoVO.getFields()).stream().filter(f -> {
            return f.getIsListShow() != null && f.getIsListShow() == Constant.INT_TRUE;
        }).collect(Collectors.toList());
        //字段注入
        for (ListFieldVO listFieldVO : listFieldVOList) {
            result.add(ListSettVO.Colmn.builder().name(listFieldVO.getCamelFieldName(true)).width(listFieldVO.getWidth()).label(listFieldVO.getLabel()).build());
        }
        //操作列处理
        Set<String> operationCodes = tableInfoVO.getTableOperation() == null ? new HashSet<>() :
                new HashSet<>(Arrays.asList(tableInfoVO.getTableOperation().split(",")));
        ListSettVO.Colmn operationColumn = ListSettVO.Colmn.builder().name("operation").label("操作").type("textBtn")
                .isHasEdit(operationCodes.contains(GenerateConstant.OPERATOR_TYPE_UPDATE))
                .isHasDel(operationCodes.contains(GenerateConstant.OPERATOR_TYPE_DELETE)).build();
        //有设置编辑或者删除才有操作列
        if (operationColumn.isHasDel() || operationColumn.isHasEdit()) {
            result.add(operationColumn);
        }
        return result;
    }

    @Override
    public void regFilterParser(SFunction<ListFieldVO, ListSettVO.Filter> parser, String... types) {
        for (String type : types) {
            filterParserMap.put(type, parser);
        }
    }

    /**
     * 格式化过滤条件
     *
     * @param tableInfoVO
     * @return
     */
    public List<ListSettVO.Filter> parseFilter(TableInfoVO tableInfoVO) {
        List<ListFieldVO> fieldVOS = Arrays.asList(tableInfoVO.getFields()).stream()
                .filter(f -> {
                    return !StringUtil.isEmpty(f.getElementType());
                })
                .collect(Collectors.toList());
        List<ListSettVO.Filter> filters = new ArrayList<>();
        for (ListFieldVO fieldVO : fieldVOS) {
            if (!filterParserMap.containsKey(fieldVO.getElementType())) {
                throw new ParamException(fieldVO.getElementType() + "后端没有实现相关代码生成处理，不要只加字典呀！！");
            }
            filters.add(filterParserMap.get(fieldVO.getElementType()).apply(fieldVO));
        }
        return filters;
    }

}

