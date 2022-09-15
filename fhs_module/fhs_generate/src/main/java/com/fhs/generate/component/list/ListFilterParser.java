package com.fhs.generate.component.list;

import com.fhs.generate.constant.GenerateConstant;
import com.fhs.generate.service.GenerateCodeService;
import com.fhs.generate.vo.ListFieldVO;
import com.fhs.generate.vo.ListSettVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 过滤条件格式化
 */
@Component
public class ListFilterParser implements InitializingBean {

    @Autowired
    private GenerateCodeService generateCodeService;

    /**
     * 输入框
     * @param fieldVO
     * @return
     */
    public ListSettVO.Filter parseInput(ListFieldVO fieldVO) {
        return ListSettVO.Filter.builder().label(fieldVO.getLabel()).name(fieldVO.getCamelFieldName(false))
                .type("text").operation(GenerateConstant.FILTER_TYPE_INPUT_LIKE.equals(fieldVO.getElementType()) ? "like" : "=").build();
    }

    /**
     * 格式化tree和tree多选
     * @param fieldVO
     * @return
     */
    public ListSettVO.Filter parseTree(ListFieldVO fieldVO) {
        ListSettVO.Filter filter = ListSettVO.Filter.builder().label(fieldVO.getLabel()).name(fieldVO.getCamelFieldName(false))
                .type("treeSelect").operation(GenerateConstant.FILTER_TYPE_TREE.equals(fieldVO.getElementType()) ? "=" : "in")
                .multiple(GenerateConstant.FILTER_TYPE_TREE.equals(fieldVO.getElementType()) ? false : true).url(fieldVO.getUrl())
                .labelField(fieldVO.getLabelField()).valueField(fieldVO.getValueField()).build();
        return filter;
    }

    /**
     * 格式化select
     * @param fieldVO
     * @return
     */
    public ListSettVO.Filter parseSelect(ListFieldVO fieldVO) {
        ListSettVO.Filter filter = ListSettVO.Filter.builder().label(fieldVO.getLabel()).name(fieldVO.getCamelFieldName(false))
                .type("select").operation(GenerateConstant.FILTER_TYPE_SELECT.equals(fieldVO.getElementType()) ? "=" : "in")
                .multiple(GenerateConstant.FILTER_TYPE_SELECT.equals(fieldVO.getElementType()) ? false : true).url(fieldVO.getUrl())
                .dictCode(fieldVO.getDictCode())
                .labelField(fieldVO.getLabelField()).valueField(fieldVO.getValueField()).build();
        return filter;
    }

    /**
     * 日期区间
     * @param fieldVO
     * @return
     */
    public ListSettVO.Filter parseDate(ListFieldVO fieldVO) {
        ListSettVO.Filter filter = ListSettVO.Filter.builder().label(fieldVO.getLabel()).name(fieldVO.getCamelFieldName(false))
                .type("daterange").operation("between").build();
        return filter;
    }

    /**
     * 时间区间
     * @param fieldVO
     * @return
     */
    public ListSettVO.Filter parseTime(ListFieldVO fieldVO) {
        ListSettVO.Filter filter = ListSettVO.Filter.builder().label(fieldVO.getLabel()).name(fieldVO.getCamelFieldName(false))
                .type("datetimerange").operation("between").build();
        return filter;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        generateCodeService.regFilterParser(this::parseInput,GenerateConstant.FILTER_TYPE_INPUT_LIKE,GenerateConstant.FILTER_TYPE_INPUT);
        generateCodeService.regFilterParser(this::parseTree,GenerateConstant.FILTER_TYPE_TREE,GenerateConstant.FILTER_TYPE_TREE_IN);
        generateCodeService.regFilterParser(this::parseDate,GenerateConstant.FILTER_TYPE_DATE_RANGE);
        generateCodeService.regFilterParser(this::parseTime,GenerateConstant.FILTER_TYPE_TIME_RANGE);
        generateCodeService.regFilterParser(this::parseSelect,GenerateConstant.FILTER_TYPE_SELECT,GenerateConstant.FILTER_TYPE_SELECT_IN);
    }


}
