package com.fhs.bislogger.service;

import com.fhs.bislogger.dox.LogLoginDO;
import com.fhs.bislogger.vo.LogLoginVO;
import com.fhs.core.base.service.BaseService;
import com.fhs.core.cache.annotation.Namespace;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * 登录日志(LogLogin)}表服务接口
 *
 * @author wanglei
 * @since 2020-04-23 13:58:36
 */
@Namespace("log_login")
public interface LogLoginService extends BaseService<LogLoginVO,LogLoginDO>{


    /**
     * 添加登录日志信息
     * @param request
     * @param userName
     * @param isLogin 登录失败 = true;
     * @param errorType
     * @param userId
     * @param typeOut 登出 = true
     */
    void addLoginUserInfo(HttpServletRequest request, String userName, boolean isLogin, Integer errorType, String userId, boolean typeOut);

    /**
     * 汇总前20名 登录玩家的登录次数，根基时间段查询
     * @param statTime
     * @param endTime
     * @return
     */
    List<LogLoginVO> getLoginIogSummary(Date statTime, Date endTime);
}