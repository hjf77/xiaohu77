package com.fhs.basics.controller;

import com.fhs.basics.po.LogHistoryDataPO;
import com.fhs.basics.vo.LogHistoryDataVO;
import org.springframework.web.bind.annotation.*;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;

/**
 * 日志历史(LogHistoryData)表控制层
 *
 * @author wanglei
 * @since 2020-04-23 14:27:40
 */

@RestController
@Api(tags = {"日志历史"})
@RequestMapping("/ms/logHistoryData")
public class LogHistoryDataController extends ModelSuperController<LogHistoryDataVO, LogHistoryDataPO,String> {


}
