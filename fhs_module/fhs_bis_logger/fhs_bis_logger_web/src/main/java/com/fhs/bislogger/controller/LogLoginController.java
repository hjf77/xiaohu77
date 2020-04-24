package com.fhs.bislogger.controller;

import com.fhs.bislogger.dox.LogLoginDO;
import com.fhs.bislogger.vo.LogLoginVO;
import org.springframework.web.bind.annotation.*;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;

/**
 * 登录日志(LogLogin)表控制层
 *
 * @author wanglei
 * @since 2020-04-23 13:58:41
 */

@RestController
@Api(tags={"登录日志"})
@RequestMapping("/ms/logLogin")
public class LogLoginController extends ModelSuperController<LogLoginVO,LogLoginDO> {
   

}