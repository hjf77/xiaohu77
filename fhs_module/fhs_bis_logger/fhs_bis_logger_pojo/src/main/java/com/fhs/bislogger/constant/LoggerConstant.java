package com.fhs.bislogger.constant;

/**
 * 日志模块常量
 *
 * @author user
 * @date 2020-05-18 14:21:23
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

    /**
     * 登录成功
     */
    int LOG_LOGIN_TRUE = 0;

    /**
     * 登录失败
     */
    int LOG_LOGIN_ERROR = 1;

    /**
     * 登录失败，验证码失效
     */
    int LOG_LOGIN_ERROR_CODE_INVALID = 3;

    /**
     * 登录失败，验证码错误
     */
    int LOG_LOGIN_ERROR_CODE = 2;

    /**
     * 登录失败，用户名或者密码错误
     */
    int LOG_LOGIN_ERROR_USER = 1;

    /**
     * 登录 ：登入
     */
    int LOG_LOGIN_IN = 0;

    /**
     * 登录 ：登入
     */
    int LOG_LOGIN_OUT = 1;

    /*
     * 是软删除
     */
    int SOFT_DEL = 0;

    /**
     * 已经软删除
     */
    int HAS_SOFT_DEL = 1;
}

