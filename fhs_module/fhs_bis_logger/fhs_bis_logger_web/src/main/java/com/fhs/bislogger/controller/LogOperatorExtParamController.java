package com.fhs.bislogger.controller;

import com.fhs.bislogger.dox.LogOperatorExtParamDO;
import com.fhs.bislogger.vo.LogOperatorExtParamVO;
import org.springframework.web.bind.annotation.*;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;

/**
 * (LogOperatorExtParam)表控制层
 *
 * @author wanglei
 * @since 2020-04-23 13:58:59
 */

@RestController
@Api(tags={""})
@RequestMapping("/ms/logOperatorExtParam")
public class LogOperatorExtParamController extends ModelSuperController<LogOperatorExtParamVO,LogOperatorExtParamDO> {
   

}