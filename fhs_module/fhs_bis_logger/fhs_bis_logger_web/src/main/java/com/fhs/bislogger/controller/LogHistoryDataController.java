package com.fhs.bislogger.controller;

import com.fhs.bislogger.dox.LogHistoryDataDO;
import com.fhs.bislogger.vo.LogHistoryDataVO;
import org.springframework.web.bind.annotation.*;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;

/**
 * (LogHistoryData)表控制层
 *
 * @author wanglei
 * @since 2020-04-23 14:27:40
 */

@RestController
@Api(tags={""})
@RequestMapping("/ms/logHistoryData")
public class LogHistoryDataController extends ModelSuperController<LogHistoryDataVO,LogHistoryDataDO> {
   

}