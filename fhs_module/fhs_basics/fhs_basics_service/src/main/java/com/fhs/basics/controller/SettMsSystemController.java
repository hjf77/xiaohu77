package com.fhs.basics.controller;


import com.fhs.basics.po.SettMsSystemPO;
import com.fhs.basics.service.SettMsSystemService;
import com.fhs.basics.vo.SettMsSystemVO;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.basics.api.anno.LogNamespace;
import com.fhs.module.base.controller.ModelSuperController;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author wanglei
 * @version [版本号, 2018-09-26]
 * @Description: 子系统管理
 * @versio 1.0 陕西小伙伴网络科技有限公司 Copyright (c) 2018 All Rights Reserved.
 */
@RestController
@Api(tags = {"子系统"})
@ApiGroup(group = "group_default")
@RequestMapping("ms/settMsSystem")
@LogNamespace(namespace = "settMsSystem", module = "子系统管理")
public class SettMsSystemController extends ModelSuperController<SettMsSystemVO, SettMsSystemPO,String> {

    @Autowired
    private SettMsSystemService sysSystemService;


    /**
     * 查询当前用户拥有权限的子系统列表
     * @param request 请求
     * @return 子系统列表
     */
    @GetMapping("/getSystemList")
    @ApiOperation("获取子系统列表--根据权限")
    public List<SettMsSystemVO> getSystemList(HttpServletRequest request) {
        UcenterMsUserVO sessionUser = super.getSessionuser();
        return sysSystemService.getSystemList(sessionUser);
    }


}
