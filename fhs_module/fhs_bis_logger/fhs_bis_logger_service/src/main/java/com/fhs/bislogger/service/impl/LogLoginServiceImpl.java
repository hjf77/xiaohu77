package com.fhs.bislogger.service.impl;

import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.bislogger.dox.LogLoginDO;
import com.fhs.bislogger.service.LogLoginService;
import com.fhs.bislogger.util.GetLoginUserMsgUtil;
import com.fhs.bislogger.vo.LogLoginVO;
import com.fhs.common.constant.Constant;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.db.ds.DataSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * 登录日志(LogLogin)表服务实现类
 *
 * @author wanglei
 * @since 2020-04-23 13:58:40
 */
@Service
@DataSource("log")
public class LogLoginServiceImpl extends BaseServiceImpl<LogLoginVO, LogLoginDO> implements LogLoginService {


    @Override
    public void getLoginUserMsg(HttpServletRequest request) throws IOException {
        LogLoginVO logLoginVO = new LogLoginVO();
        UcenterMsUserVO userVO = (UcenterMsUserVO) request.getSession().getAttribute(Constant.SESSION_USER);
        GetLoginUserMsgUtil getLoginUserMsgUtil = new GetLoginUserMsgUtil();
        //获取当前登录人ip信息
        String ip = getLoginUserMsgUtil.getIpAddress(request);
        //获取当前登录人地址信息
        String addresses = getLoginUserMsgUtil.getAddresses("ip=" + ip, "utf-8");
        //获取当前登录人浏览器信息
        Map<String, String> userAgent = getLoginUserMsgUtil.getUserAgent(request);
        //获取当前登录人操作系统信息
        String os = getLoginUserMsgUtil.getOsInfo(request);
        logLoginVO.setIpAddress(ip);
        logLoginVO.setUserId(userVO.getUserId());
        logLoginVO.setIpInfo(addresses);
        logLoginVO.setBrowser(userAgent.get("type"));
        logLoginVO.setOs(os);
        logLoginVO.setLoginName(userVO.getUserLoginName());
        this.add(logLoginVO);
    }


}