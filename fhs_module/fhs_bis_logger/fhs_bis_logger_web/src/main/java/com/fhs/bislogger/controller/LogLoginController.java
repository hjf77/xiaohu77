package com.fhs.bislogger.controller;

import com.fhs.bislogger.dox.LogLoginDO;
import com.fhs.bislogger.service.LogLoginService;
import com.fhs.bislogger.vo.LogLoginVO;
import com.fhs.bislogger.vo.LogOperatorMainVO;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 登录日志(LogLogin)表控制层
 *
 * @author wanglei
 * @since 2020-04-23 13:58:41
 */

@RestController
@Api(tags = {"登录日志"})
@RequestMapping("/ms/logLogin")
public class LogLoginController extends ModelSuperController<LogLoginVO, LogLoginDO> {

    @Autowired
    private LogLoginService logLoginService;

    /**
     * 获取前20名用户登录汇总
     *
     * @param statTime
     * @param endTime
     * @return
     */
    @RequestMapping("/getloginIogSummary")
    public List<LogLoginVO> getloginIogSummary(Date statTime, Date endTime) {
        List<LogLoginVO> loginIogSummary = logLoginService.getLoginIogSummary(statTime, endTime);
        return loginIogSummary;
    }

}
