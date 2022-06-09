package com.fhs.basics.controller;

import com.fhs.basics.po.LogOperatorExtParamPO;
import com.fhs.basics.vo.LogOperatorExtParamVO;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 扩展参数(LogOperatorExtParam)表控制层
 *
 * @author wanglei
 * @since 2020-04-23 13:58:59
 */

@RestController
@Api(tags = {"日志扩展参数"})
@RequestMapping("/ms/logOperatorExtParam")
public class LogOperatorExtParamController extends ModelSuperController<LogOperatorExtParamVO, LogOperatorExtParamPO, String> {


}
