package com.fhs.basics.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.fhs.basics.po.ServiceDictGroupPO;
import com.fhs.basics.po.ServiceDictItemPO;
import com.fhs.basics.service.ServiceDictGroupService;
import com.fhs.basics.vo.ServiceDictGroupVO;
import com.fhs.basics.vo.ServiceDictItemVO;
import com.fhs.bislogger.api.anno.LogNamespace;
import com.fhs.core.result.HttpResult;
import com.fhs.logger.Logger;
import com.fhs.module.base.controller.ModelSuperController;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 字典管理controller
 *
 * @author wanglei
 * @date 2020-05-18 16:52:33
 */
@RestController
@RequestMapping("ms/dictGroup")
@Api(tags = {"字典分组"})
@ApiGroup(group = "group_default")
@LogNamespace(namespace = "dictGroup", module = "字典分组管理")
public class ServiceDictGroupController extends ModelSuperController<ServiceDictGroupVO, ServiceDictGroupPO> {

    private static final Logger LOG = Logger.getLogger(ServiceDictGroupController.class);

    @Autowired
    private ServiceDictGroupService wordbookAndGroupService;

    /**
     * 刷新redis缓存
     *
     * @param request
     * @param response
     */
    @SaCheckRole("dictGroup:refreshRedisCache")
    @GetMapping("refreshRedisCache")
    @ApiOperation("刷新字典缓存")
    public HttpResult<Boolean> refreshRedisCache(String groupCode, HttpServletRequest request, HttpServletResponse response) {
        wordbookAndGroupService.refreshRedisCache(groupCode);
        return HttpResult.success(true);
    }

}
