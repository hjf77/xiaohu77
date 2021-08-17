package com.fhs.generate.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.DateUtils;
import com.fhs.generate.service.GenerateCodeService;
import com.fhs.generate.vo.FieldsVO;
import com.fhs.generate.vo.TableInfoVO;
import com.fhs.pagex.common.BeetlUtil;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GenerateCodeServiceImpl implements GenerateCodeService {


    private static Set<String> needTransElementType = new HashSet<>();

    static {
        needTransElementType.add("select");
        needTransElementType.add("treeSelect");
        needTransElementType.add("radio");
        needTransElementType.add("checkbox");
        needTransElementType.add("switch");
    }

    @Override
    public String[] generateCode(TableInfoVO tableInfoVO) {
        System.out.println(generateListVue(tableInfoVO));
        return new String[0];
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
                                ? fieldsVO.getCamelFieldName() + "Name" : fieldsVO.getFiledName())
                        + "', width: 150},\n");
            }
            //如果没指定元素类型 或者不是过滤条件则看下一个字段
            if (CheckUtils.isNullOrEmpty(fieldsVO.getPageElementType()) || Constant.INT_TRUE != fieldsVO.getIsFilter()) {
                continue;
            }
            filters.append("                     " + this.getBasic(fieldsVO).toJSONString() + ",\n");
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
     * 获取基础json
     * @param fieldsVO
     * @return
     */
    private JSONObject getBasic(FieldsVO fieldsVO) {

        Map<String,String> typeConverter = new HashMap<>();
        typeConverter.put("dates","datetimerange");
        typeConverter.put("textarea","text");

        Map<String,String> operationMap = new HashMap<>();
        operationMap.put("dates","between");
        operationMap.put("textarea","like");
        operationMap.put("text","like");
        operationMap.put("checkbox","find_in_set_in");

        JSONObject result = new JSONObject();
        result.put("type",typeConverter.containsKey(fieldsVO.getPageElementType()) ? typeConverter.get(fieldsVO.getPageElementType()) : fieldsVO.getPageElementType());
        result.put("label",fieldsVO.getComment());
        result.put("name",fieldsVO.getCamelFieldName());
        result.put("operation",operationMap.containsKey(fieldsVO.getPageElementType()) ? operationMap.get(fieldsVO.getPageElementType()) : "=");
        /*if(Constant.INT_TRUE==fieldsVO.getIsRequired()){
            result.put("rule","required");
        }*/
        result.putAll(fieldsVO.getExtParam());
        return result;
    }



}

