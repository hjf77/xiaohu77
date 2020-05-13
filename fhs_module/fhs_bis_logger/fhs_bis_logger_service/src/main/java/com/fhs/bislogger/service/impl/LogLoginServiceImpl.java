package com.fhs.bislogger.service.impl;

import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.bislogger.constant.LoggerConstant;
import com.fhs.bislogger.dox.LogLoginDO;
import com.fhs.bislogger.service.LogLoginService;
import com.fhs.bislogger.util.GetLoginUserMsgUtil;
import com.fhs.bislogger.vo.LogLoginVO;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.StringUtil;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.db.ds.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * 登录日志(LogLogin)表服务实现类
 *
 * @author wanglei
 * @since 2020-04-23 13:58:40
 */
@Slf4j
@Service
@DataSource("log")
public class LogLoginServiceImpl extends BaseServiceImpl<LogLoginVO, LogLoginDO> implements LogLoginService {


    @Override
    public void addLoginUserInfo(HttpServletRequest request, String userName, boolean isLogin, Integer errorType,String userId){
        LogLoginVO logLoginVO = new LogLoginVO();
        int count = this.findCount(logLoginVO);
        GetLoginUserMsgUtil getLoginUserMsgUtil = new GetLoginUserMsgUtil();
        String ip = null;
        String addresses = null;
        try {
            //获取当前登录人ip信息
            ip = getLoginUserMsgUtil.getIpAddress(request);
            //获取当前登录人地址信息
            addresses = getLoginUserMsgUtil.getAddresses("ip=" + ip, "utf-8");
        } catch (IOException e) {
            log.error("获取当前登录人信息失败 : ",e);
        }
        //获取当前登录人浏览器信息
        Map<String, String> userAgent = getLoginUserMsgUtil.getUserAgent(request);
        //获取当前登录人操作系统信息
        String os = getLoginUserMsgUtil.getOsInfo(request);
        logLoginVO.setUserId(userId);
        logLoginVO.setLogId(StringUtil.getUUID());
        logLoginVO.setIpAddress(ip);
        logLoginVO.setIpInfo(addresses);
        logLoginVO.setBrowser(userAgent.get("type"));
        logLoginVO.setOs(os);
        logLoginVO.setLoginName(userName);
        logLoginVO.setCreateTime(new Date());
        logLoginVO.setType(LoggerConstant.LOG_LOGIN_TRUE);
        logLoginVO.setVisitNumber(count + 1);
        if(isLogin){
            logLoginVO.setType(LoggerConstant.LOG_LOGIN_ERROR);
            logLoginVO.setErrorInfo(errorType);
        }
        this.add(logLoginVO);
    }

}