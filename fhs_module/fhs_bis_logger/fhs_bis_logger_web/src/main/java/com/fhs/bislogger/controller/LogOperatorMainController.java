package com.fhs.bislogger.controller;

import com.fhs.bislogger.dox.LogOperatorMainDO;
import com.fhs.bislogger.vo.LogOperatorMainVO;
import org.springframework.web.bind.annotation.*;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;

/**
 * (LogOperatorMain)表控制层
 *
 * @author wanglei
 * @since 2020-04-23 13:59:14
 */

@RestController
@Api(tags={""})
@RequestMapping("/ms/logOperatorMain")
public class LogOperatorMainController extends ModelSuperController<LogOperatorMainVO,LogOperatorMainDO> {
   

}