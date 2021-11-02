package com.fhs.basics.controller;

import com.fhs.basics.po.ServiceDictItemPO;
import com.fhs.basics.vo.ServiceDictItemVO;
import com.fhs.bislogger.api.anno.LogNamespace;
import com.fhs.module.base.controller.ModelSuperController;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

/**
 * 字典管理controller
 *
 * @author wanglei
 * @date 2020-05-18 16:52:33
 */
@RestController
@RequestMapping("ms/dictItem")
@Api(tags = {"字典"})
@ApiGroup(group = "group_default")
@LogNamespace(namespace = "dict", module = "字典管理")
public class ServiceDictItemController extends ModelSuperController<ServiceDictItemVO, ServiceDictItemPO> {

}
