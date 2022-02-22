package com.fhs.basics.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.fhs.basics.constant.BaseTransConstant;
import com.fhs.basics.po.ServiceAreaPO;
import com.fhs.basics.service.ServiceAreaService;
import com.fhs.basics.vo.ServiceAreaVO;
import com.fhs.basics.api.anno.LogNamespace;
import com.fhs.common.tree.TreeNode;
import com.fhs.common.tree.Treeable;
import com.fhs.common.utils.TreeUtils;
import com.fhs.core.base.vo.QueryFilter;
import com.fhs.core.result.HttpResult;
import com.fhs.module.base.controller.ModelSuperController;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 省市区字典controller
 *
 * @Filename: serviceAreaController.java
 * @Description:
 * @Version: 1.0
 * @Author: wanglei
 * @Email: qxb@sxpartner.com
 * @History:<br> 陕西小伙伴网络科技有限公司
 * Copyright (c) 2017 All Rights Reserved.
 */
@RestController
@RequestMapping("ms/area")
@Api(tags = {"省市区信息"})
@ApiGroup(group = "group_default")
@LogNamespace(namespace = BaseTransConstant.AREA, module = "地区管理")
public class ServiceAreaController extends ModelSuperController<ServiceAreaVO, ServiceAreaPO> {
    @Autowired
    private ServiceAreaService areaService;


    /**
     * 刷新区域缓存
     */
    @SaCheckRole("area:refreshRedisCache")
    @GetMapping("/refreshRedisCache")
    @ResponseBody
    public HttpResult<Boolean> refreshRedisCache() {
        areaService.refreshRedisCache();
        return HttpResult.success(true);
    }

    /**
     * 区域下拉tree
     * @param filter
     * @return
     */
    @ApiOperation("下拉专用tree")
    @PostMapping("selectAreaTree")
    public List<TreeNode<Treeable>> selectTree(@RequestBody QueryFilter<ServiceAreaPO> filter) {
        List<ServiceAreaVO> areas = areaService.selectListMP(filter.asWrapper(ServiceAreaPO.class));
        return TreeUtils.formartTree(areas);
    }

    /**
     * 无权限也可以查询省市区
     * @param e
     * @param request response
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    public List<ServiceAreaVO> findList(ServiceAreaVO e, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return areaService.findForList(e);
    }

    /**
     * 无权限也可以查询省市区
     * @param filter
     * @param request
     * @return
     */
    @Override
    public List<ServiceAreaVO> findListAdvance(QueryFilter<ServiceAreaPO> filter, HttpServletRequest request) {
        return areaService.selectListMP(filter.asWrapper(getDOClass()));
    }
}
