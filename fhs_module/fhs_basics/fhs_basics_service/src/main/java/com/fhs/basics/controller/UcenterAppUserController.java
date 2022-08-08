package com.fhs.basics.controller;

import com.fhs.basics.api.anno.LogNamespace;
import com.fhs.basics.po.UcenterMsUserPO;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.module.base.controller.ModelSuperController;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

/**
 * APP用户管理controller
 */
@RestController
@Api(tags = {"APP用户管理"})
@ApiGroup(group = "group_default")
@RequestMapping("ms/sysAppUser")
@LogNamespace(namespace = "sysAppUser", module = "APP用户管理")
public class UcenterAppUserController extends ModelSuperController<UcenterMsUserVO, UcenterMsUserPO, Long> {

}
