package com.fhs.basics.controller;

import com.fhs.basics.po.LogOperationPO;
import com.fhs.basics.vo.LogOperationVO;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * (LogOperation)表控制层
 *
 * @author LiYuLin
 * @since 2022-03-10 16:44:36
 */

@RestController
@Api(tags={""})
@RequestMapping("/ms/logOperation")
public class LogOperationController extends ModelSuperController<LogOperationVO, LogOperationPO> {
   

}
