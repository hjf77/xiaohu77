package com.fhs.basics.controller;

import com.fhs.basics.constant.BaseTransConstant;
import com.fhs.basics.dox.ServiceAreaDO;
import com.fhs.basics.service.ServiceAreaService;
import com.fhs.basics.vo.ServiceAreaVO;
import com.fhs.bislogger.api.anno.LogNamespace;
import com.fhs.core.result.HttpResult;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 省市区字典controller
 *
 * @Filename: serviceAreaController.java
 * @Description:
 * @Version: 1.0
 * @Author: qixiaobo
 * @Email: qxb@sxpartner.com
 * @History:<br> 陕西小伙伴网络科技有限公司
 * Copyright (c) 2017 All Rights Reserved.
 */
@RestController
@RequestMapping("ms/area")
@Api(tags = {"省市区信息"})
@LogNamespace(namespace = BaseTransConstant.AREA,module = "地区管理")
public class ServiceAreaController extends ModelSuperController<ServiceAreaVO, ServiceAreaDO> {
    @Autowired
    private ServiceAreaService areaService;


    /**
     * 刷新区域缓存
     */
    @RequiresPermissions("area:refreshRedisCache")
    @RequestMapping("/refreshRedisCache")
    @ResponseBody
    public HttpResult<Boolean> refreshRedisCache(){
        areaService.refreshRedisCache();
        return HttpResult.success(true);
    }

}