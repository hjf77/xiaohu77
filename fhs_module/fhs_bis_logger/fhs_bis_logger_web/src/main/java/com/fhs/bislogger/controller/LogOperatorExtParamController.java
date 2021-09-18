package com.fhs.bislogger.controller;

import com.fhs.bislogger.dox.LogOperatorExtParamDO;
import com.fhs.bislogger.service.LogOperatorExtParamService;
import com.fhs.bislogger.vo.LogOperatorExtParamVO;
import com.fhs.core.base.pojo.pager.Pager;
import com.fhs.core.trans.service.impl.TransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;

import java.util.List;

/**
 * 扩展参数(LogOperatorExtParam)表控制层
 *
 * @author wanglei
 * @since 2020-04-23 13:58:59
 */

@RestController
@Api(tags = {"日志扩展参数"})
@RequestMapping("/ms/logOperatorExtParam")
public class LogOperatorExtParamController extends ModelSuperController<LogOperatorExtParamVO, LogOperatorExtParamDO> {


}
