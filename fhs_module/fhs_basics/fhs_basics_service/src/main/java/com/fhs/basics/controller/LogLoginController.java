package com.fhs.basics.controller;

import com.fhs.basics.po.LogLoginPO;
import com.fhs.basics.vo.LogLoginVO;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录日志(LogLogin)表控制层
 *
 * @author wanglei
 * @since 2020-04-23 13:58:41
 */

@RestController
@Api(tags = {"登录日志"})
@RequestMapping("/ms/logLogin")
public class LogLoginController extends ModelSuperController<LogLoginVO, LogLoginPO, Long> {


}
