package com.fhs.basics.controller;

import com.fhs.basics.po.LogOperatorMainPO;
import com.fhs.basics.vo.LogOperatorMainVO;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 操作日志(LogOperatorMain)表控制层
 *
 * @author wanglei
 * @since 2020-04-23 13:59:14
 */

@RestController
@Api(tags = {"操作日志"})
@RequestMapping("/ms/logOperatorMain")
public class LogOperatorMainController extends ModelSuperController<LogOperatorMainVO, LogOperatorMainPO, String> {


}
