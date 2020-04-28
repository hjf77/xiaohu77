package com.fhs.bislogger.constant;

/**
 * 日志模块常量
 */
public interface LoggerConstant {
    /**
     * 新增
     */
    int METHOD_TYPE_ADD = 0;

    /**
     * 修改
     */
    int METHOD_TYPE_UPATE = 1;

    /**
     * 删除
     */
    int METHOD_TYPE_DEL = 2;

    /**
     * 查询
     */
    int METHOD_TYPE_VIEW = 3;

    /**
     * 导入
     */
    int METHOD_TYPE_IMPORT = 4;

    /**
     * 导出
     */
    int METHOD_TYPE_EXPORT = 5;

    /**
     * 没有vo
     */
    int INDEX_NOT = -1;

    /**
     * 新增
     */
    int OPERATOR_TYPE_ADD = 0;

    /**
     * 修改
     */
    int OPERATOR_TYPE_UPDATE = 1;

    /**
     * 删除
     */
    int OPERATOR_TYPE_DEL = 2;

    /**
     * 成功的日志
     */
    int LOG_STATE_SUCCESS = 0;

    /**
     * 失败的日志
     */
    int LOG_STATE_ERROR = 1;
}

