package com.fhs.basics.controller;

import com.fhs.basics.po.LogLoginPO;
import com.fhs.basics.service.LogLoginService;
import com.fhs.basics.vo.LogLoginVO;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class LogLoginController extends ModelSuperController<LogLoginVO, LogLoginPO> {

    @Autowired
    private LogLoginService logLoginService;

    /**
     * 获取前20名用户登录汇总
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
