package com.fhs.generate.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fhs.basics.po.UcenterMsUserPO;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.*;
import com.fhs.core.config.EConfig;
import com.fhs.core.exception.ParamException;
import com.fhs.generate.component.FormControlParser;
import com.fhs.generate.constant.GenerateConstant;
import com.fhs.generate.service.GenerateCodeService;
import com.fhs.generate.service.TableInfoService;
import com.fhs.generate.util.ColumnNameUtil;
import com.fhs.generate.vo.*;
import com.fhs.beetl.common.BeetlUtil;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    @Lazy
    @Autowired
    private FormControlParser formControlParser;

    @Autowired
    private TableInfoService tableInfoService;


    private static Set<String> needTransElementType = new HashSet<>();

    /**
     * form的类型对应的过滤条件类型
     */
    private static Map<String, String> typeConverter = new HashMap<>();


    private Map<String, SFunction<ListFieldVO, ListSettVO.Filter>> filterParserMap = new HashMap<>();

    private Map<String, SFunction<FormFiledVO, FormSettVO.Control>> controlParserMap = new HashMap<>();

    private static Map<String, String> TYPE_CONVERTER_MAP = new HashMap<>();

    static {
        TYPE_CONVERTER_MAP.put("fc-editor", "editor");
        TYPE_CONVERTER_MAP.put("datePicker", "dates");
        TYPE_CONVERTER_MAP.put("timePicker", "dateTimePicker");
        TYPE_CONVERTER_MAP.put("tree", "treeSelect");
        TYPE_CONVERTER_MAP.put("el-transfer", "transfer");

    }


    @Override
    public String generateCode(GenerateCodeVO generateCodeVO) {
        String tempPath = EConfig.getPathPropertiesValue("fileSavePath") + "/" + StringUtils.getUUID();
        new File(tempPath + "/components").mkdirs();
        TableInfoVO tableInfoVO = tableInfoService.getTableInfo(generateCodeVO.getTableSchema(), generateCodeVO.getTableName(), GenerateConstant.CONFIG_TYPE_LIST_COLUMN);


        //所有或者列表页
        if ("all".equals(generateCodeVO.getGenType()) || "list".equals(generateCodeVO.getGenType())) {
            String listVueFile = tempPath + "/index.vue";
            String listVueCode = generateListVue(tableInfoVO, generateCodeVO);
            try {
                FileUtils.write(new File(listVueFile), listVueCode, "UTF-8");
            } catch (IOException e) {
                log.error("写入文件错误", e);
            }
        }
        //所有或者表单页面

        if ("all".equals(generateCodeVO.getGenType()) || "list".equals(generateCodeVO.getGenType())) {
            String formVueFile = tempPath + "/components/" + tableInfoVO.getNamespace() + "Form.vue";
            String formVueCode = generateFormVue(tableInfoVO, generateCodeVO);
            try {
                FileUtils.write(new File(formVueFile), formVueCode, "UTF-8");
            } catch (IOException e) {
                log.error("写入文件错误", e);
            }
        }

        return tempPath;
    }


    /**
     * 生成列表vue代码
     *
     * @param tableInfoVO 表对象
     * @return 代码
     */
    private String generateListVue(TableInfoVO tableInfoVO, GenerateCodeVO generateCodeVO) {
        //过滤条件
        ListSettVO listSettVO = getListJson(tableInfoVO);
        String columns = parseJson(JSON.toJSONString(listSettVO.getColumns().stream().filter(column -> {
            return !"textBtn".equals(column.getType());
        }).collect(Collectors.toList()),SerializerFeature.PrettyFormat),true);
        String filters = parseJson(JSON.toJSONString(listSettVO.getFilters(),SerializerFeature.PrettyFormat),true);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tableComment", tableInfoVO.getTableComment());
        paramMap.put("author", generateCodeVO.getAuthor());
        paramMap.put("nowDate", DateUtils.formartDate(new Date(), DateUtils.DATETIME_PATTERN_DATE));
        paramMap.put("namespace", tableInfoVO.getNamespace());
        paramMap.put("filters", filters);
        paramMap.put("columns", columns);
        paramMap.put("idFieldName", listSettVO.getIdFieldName());
        paramMap.put("microServiceName", generateCodeVO.getMicroServiceName());
        return BeetlUtil.renderBeelt("/template/list_vue.html", paramMap);
    }

    public static final String[] KEYWORDS = new String[]{"name","label","api","clearable","rule","message","required"
            ,"type","options","url","labelField","valueField","dictCode","methodType","param","isValueNum","remote","multiple","trigger","title"};


    /**
     * 格式化json
     * @param jsonStr
     * @return
     */
    public String parseJson(String jsonStr,boolean appendComma){
        if(jsonStr.startsWith("[")){
            jsonStr = jsonStr.substring(1);
            jsonStr = jsonStr.substring(0,jsonStr.length()-1);
        }
        for (String keyword : KEYWORDS) {
            jsonStr = jsonStr.replaceAll("\"" + keyword + "\":",keyword + " : ");
        }
        if(appendComma){
            jsonStr = jsonStr + ",";
        }
        return jsonStr;
    }

    /**
     * 生成表单 vue代码
     *
     * @param tableInfoVO 表对象
     * @return 代码
     */
    private String generateFormVue(TableInfoVO tableInfoVO, GenerateCodeVO generateCodeVO) {
        FormSettVO formSettVO = getFormJson(tableInfoVO);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("controls", parseJson(JSON.toJSONString(formSettVO.getControls(),SerializerFeature.PrettyFormat),false));
        paramMap.put("formData", parseJson(JSON.toJSONString(formSettVO.getDefaultValueData(),SerializerFeature.PrettyFormat),false));
        paramMap.put("tableComment", tableInfoVO.getTableComment());
        paramMap.put("author", generateCodeVO.getAuthor());
        paramMap.put("nowDate", DateUtils.formartDate(new Date(), DateUtils.DATETIME_PATTERN_DATE));
        paramMap.put("namespace", tableInfoVO.getNamespace());
        paramMap.put("microServiceName", generateCodeVO.getMicroServiceName());
        return BeetlUtil.renderBeelt("/template/form_vue.html", paramMap);
    }


    public ListSettVO getListJson(TableInfoVO tableInfoVO) {
        ListSettVO listSettVO = new ListSettVO();
        listSettVO.setColumns(parseColumn(tableInfoVO));
        listSettVO.setFilters(parseFilter(tableInfoVO));
        listSettVO.setNamespace(ColumnNameUtil.underlineToCamel(tableInfoVO.getTableName().replace("t_", "")));
        List<ListFieldVO> fieldVOS = Arrays.asList(tableInfoVO.getFields()).stream().filter(fieldVO -> {
            return !StringUtils.isEmpty(fieldVO.getKey());
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


    public FormSettVO getFormJson(TableInfoVO tableInfoVO) {
        FormSettVO formSettVO = new FormSettVO();
        formSettVO.setControls(parseController(tableInfoVO));
        formSettVO.setDefaultValueData(parseDefaultValue(tableInfoVO));
        return formSettVO;
    }


    /**
     * 格式化默认值
     *
     * @param tableInfoVO
     * @return
     */
    public String parseDefaultValue(TableInfoVO tableInfoVO) {
        List<String> defaultValues = new ArrayList<>();
        List<FormFiledVO> formFiledVOS = JSONObject.parseArray(tableInfoVO.getFormConfig(), FormFiledVO.class).stream().filter(formFiledVO ->
        {
            return !StringUtil.isEmpty(formFiledVO.getDefaultValue());
        }).collect(Collectors.toList());
        for (FormFiledVO formFiledVO : formFiledVOS) {
            if (formFiledVO.getDefaultValue().startsWith("[")) {
                defaultValues.add("\"" + formFiledVO.getName() + "\":" + formFiledVO.getDefaultValue());
            } else {
                defaultValues.add("\"" + formFiledVO.getName() + "\":\"" + formFiledVO.getDefaultValue() + "\"");
            }

        }
        return "{" + defaultValues.stream().collect(Collectors.joining(",")) + "}";
    }


    /**
     * 格式化form表单 项
     *
     * @param tableInfoVO 表信息
     * @return 表单项
     */
    public List<FormSettVO.Control> parseController(TableInfoVO tableInfoVO) {
        List<FormFiledVO> formFiledVOS = JSONObject.parseArray(tableInfoVO.getFormConfig(), FormFiledVO.class);
        List<FormSettVO.Control> controls = new ArrayList<>();
        FormSettVO.Control control = null;
        for (FormFiledVO formFiledVO : formFiledVOS) {
            if (!controlParserMap.containsKey(formFiledVO.getType())) {
                control = new FormSettVO.Control();
                control.setType(TYPE_CONVERTER_MAP.containsKey(formFiledVO.getType()) ? TYPE_CONVERTER_MAP.get(formFiledVO.getType()) : formFiledVO.getType());
            } else {
                control = controlParserMap.get(formFiledVO.getType()).apply(formFiledVO);
            }
            control.setName(ColumnNameUtil.underlineToCamel(formFiledVO.getName()));
            control.setLabel(formFiledVO.getTitle());
            control.setDictCode(formFiledVO.getDictCode());
            control.setQuerys(formFiledVO.getEffect().getFetch().getData());
            control.setMethodType(formFiledVO.getEffect().getFetch().getMethod());
            control.setUrl(formFiledVO.getEffect().getFetch().getAction());
            control.setLabelField(formFiledVO.getEffect().getFetch().getLabelField());
            control.setValueField(formFiledVO.getEffect().getFetch().getValueField());
            control.setWidth(formFiledVO.getWidth());
            if (formFiledVO.getProps() != null) {
                formFiledVO.getProps().remove("optionType");
                control.putAll(formFiledVO.getProps());
                if (formFiledVO.getProps().containsKey("props")) {
                    control.putAll((Map) formFiledVO.getProps().get("props"));
                    formFiledVO.getProps().remove("props");
                }
            }
            // 设置验证规则
            control.setRule(formControlParser.parseRules(formFiledVO));
            control.remove("options");
            controls.add(control);
        }
        return controls;
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
        if (operationColumn.getIsHasDel() || operationColumn.getIsHasEdit()) {
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

    @Override
    public void regControlParser(SFunction<FormFiledVO, FormSettVO.Control> parser, String... types) {
        for (String type : types) {
            controlParserMap.put(type, parser);
        }
    }


}

