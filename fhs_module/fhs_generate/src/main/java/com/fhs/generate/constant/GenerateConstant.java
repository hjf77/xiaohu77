package com.fhs.generate.constant;

public interface GenerateConstant {

    /**
     * 列表列
     */
    String CONFIG_TYPE_LIST_COLUMN = "listColumn";

    /**
     * 列表过滤条件
     */
    String CONFIG_TYPE_LIST_FLITER = "listFilter";

    /**
     * 表单字段
     */
    String CONFIG_TYPE_FORM_FIELD = "formField";

    /**
     * 新增
     */
    String OPERATOR_TYPE_ADD = "0";

    /**
     * 修改
     */
    String OPERATOR_TYPE_UPDATE = "1";

    /**
     * 删除
     */
    String OPERATOR_TYPE_DELETE = "2";

    /**
     * input like
     */
    String FILTER_TYPE_INPUT_LIKE = "inputLike";

    /**
     * like
     */
    String FILTER_TYPE_INPUT = "input=";

    /**
     * tree
     */
    String FILTER_TYPE_TREE = "selectTree";

    /**
     * tree 多选
     */
    String FILTER_TYPE_TREE_IN = "selectTreeIn";

    /**
     * select
     */
    String FILTER_TYPE_SELECT = "select";

    /**
     * select in
     */
    String FILTER_TYPE_SELECT_IN = "selectIn";

    /**
     * date 日期区间
     */
    String FILTER_TYPE_DATE_RANGE = "dateRange";

    /**
     * time 时间区间
     */
    String FILTER_TYPE_TIME_RANGE = "timeRange";
}
