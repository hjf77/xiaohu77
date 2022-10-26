package com.fhs.basics.service.impl;

import com.fhs.basics.constant.LoggerConstant;
import com.fhs.basics.po.LogLoginPO;
import com.fhs.basics.mapper.LogLoginMapper;
import com.fhs.basics.service.LogLoginService;
import com.fhs.basics.util.GetLoginUserMsgUtil;
import com.fhs.basics.vo.LogLoginVO;
import com.fhs.common.utils.FileUtils;
import com.fhs.common.utils.NetworkUtil;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.db.ds.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 登录日志(LogLogin)表服务实现类
 *
 * @author wanglei
 * @since 2020-04-23 13:58:40
 */
@Slf4j
@Service
@DataSource("basic")
public class LogLoginServiceImpl extends BaseServiceImpl<LogLoginVO, LogLoginPO> implements LogLoginService {

    @Autowired
    private LogLoginMapper logLoginMapper;

    @Override
    public void addLoginUserInfo(HttpServletRequest request, String userName, boolean isLogin, Integer errorType, Long userId, boolean typeOut) {
        LogLoginVO logLoginVO = new LogLoginVO();
        logLoginVO.setLoginName(userName);
        GetLoginUserMsgUtil getLoginUserMsgUtil = new GetLoginUserMsgUtil();
        String ip = null;
        String addresses = null;
        try {
            //获取当前登录人ip信息
            ip = NetworkUtil.getIpAddress(request);

            if ("0:0:0:0:0:0:0:1".equals(ip) || "172.".equals(ip.substring(0, 4)) || "192.".equals(ip.substring(0, 4))) {

            } else {
                addresses = getCityInfo(ip);
//                addresses = getLoginUserMsgUtil.getAddresses( ip);
            }
        } catch (IOException e) {
            log.error("获取当前登录人信息失败 : ", e);
        }
        //获取当前登录人浏览器信息
        Map<String, String> userAgent = getLoginUserMsgUtil.getUserAgent(request);
        //获取当前登录人操作系统信息
        String os = getLoginUserMsgUtil.getOsInfo(request);
        logLoginVO.setLogId(idHelper.nextId());
        logLoginVO.setUserId(userId);
        logLoginVO.setIpAddress(ip);
        logLoginVO.setIpInfo(addresses);
        logLoginVO.setBrowser(userAgent.get("type"));
        logLoginVO.setOs(os);
        logLoginVO.setLoginName(userName);
        logLoginVO.setCreateTime(new Date());
        logLoginVO.setState(LoggerConstant.LOG_LOGIN_TRUE);
       /* logLoginVO.setVisitNumber(count + 1);*/
        if (isLogin) {
            logLoginVO.setState(LoggerConstant.LOG_LOGIN_ERROR);
            logLoginVO.setErrorInfo(errorType);
        }
        logLoginVO.setType(LoggerConstant.LOG_LOGIN_IN);
        if (typeOut) {
            logLoginVO.setType(LoggerConstant.LOG_LOGIN_OUT);
        }
        this.insert(logLoginVO);
    }

    @Override
    public List<LogLoginVO> getLoginIogSummary(Date statTime, Date endTime) {
        return logLoginMapper.getLoginIogSummary(statTime, endTime);
    }

    /**
     * 获取IP地址所在城市
     *
     * @param ip
     * @return
     */
    private String getCityInfo(String ip) {
        DbSearcher searcher = null;
        try {
            String dbPath = LogLoginServiceImpl.class.getResource("/data/ip2region.db").getPath();
            File file = new File(dbPath);
            if (!file.exists()) {
                String tmpDir = System.getProperties().getProperty("java.io.tmpdir");
                dbPath = tmpDir + "ip.db";
                file = new File(dbPath);
                FileUtils.copyInputStreamToFile(Objects.requireNonNull(LogLoginServiceImpl.class.getClassLoader().getResourceAsStream("classpath:data/ip2region.db")), file);
            }
            int algorithm = DbSearcher.BTREE_ALGORITHM;
            DbConfig config = new DbConfig();
            searcher = new DbSearcher(config, file.getPath());
            Method method = searcher.getClass().getMethod("btreeSearch", String.class);
            DataBlock dataBlock = null;
            if (!Util.isIpAddress(ip)) {
                log.error("Error: Invalid ip address");
            }
            dataBlock = (DataBlock) method.invoke(searcher, ip);
            return dataBlock.getRegion();
        } catch (Exception e) {
            log.error("获取地址信息异常", e);
        } finally {
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
}
