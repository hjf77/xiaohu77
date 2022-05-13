package com.fhs.basics.controller;

import com.fhs.basics.po.UcenterMsUserPO;
import com.fhs.basics.service.UcenterMsUserService;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.basics.po.LogHistoryDataPO;
import com.fhs.basics.po.LogOperatorExtParamPO;
import com.fhs.basics.po.LogOperatorMainPO;
import com.fhs.basics.service.LogHistoryDataService;
import com.fhs.basics.service.LogOperatorExtParamService;
import com.fhs.basics.service.LogOperatorMainService;
import com.fhs.basics.vo.LogHistoryDataVO;
import com.fhs.basics.vo.LogOperatorExtParamVO;
import com.fhs.basics.vo.LogOperatorMainVO;
import com.fhs.core.base.pojo.pager.Pager;
import com.fhs.trans.service.impl.TransService;
import com.fhs.core.valid.checker.ParamChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;

import java.util.*;

/**
 * 操作日志(LogOperatorMain)表控制层
 *
 * @author wanglei
 * @since 2020-04-23 13:59:14
 */

@RestController
@Api(tags = {"操作日志"})
@RequestMapping("/ms/logOperatorMain")
public class LogOperatorMainController extends ModelSuperController<LogOperatorMainVO, LogOperatorMainPO> {


}
