package com.fhs.basics.controller;

import com.fhs.basics.constant.BaseTransConstant;
import com.fhs.basics.dox.UcenterMsRoleDO;
import com.fhs.basics.service.UcenterMsRoleService;
import com.fhs.basics.vo.UcenterMsRoleVO;
import com.fhs.bislogger.api.anno.LogMethod;
import com.fhs.bislogger.api.anno.LogNamespace;
import com.fhs.bislogger.constant.LoggerConstant;
import com.fhs.core.result.HttpResult;
import com.fhs.module.base.controller.ModelSuperController;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 权限控制器类
 *
 * @author Administrator
 */
@RestController
@Api(tags = {"权限"})
@ApiGroup(group = "group_default")
@RequestMapping("ms/jurisdiction")
@LogNamespace(namespace = BaseTransConstant.ROLE_INFO, module = "权限管理")
public class UcenterMsJurisdictionConttroller extends ModelSuperController<UcenterMsRoleVO, UcenterMsRoleDO> {

    /**
     * 角色服务
     */
    @Autowired
    private UcenterMsRoleService sysRoleService;
    /**
     * 角色权限管理
     *
     * @param request
     * @param response
     * @paramadminRole
     */
    @ResponseBody
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE, voParamIndex = 2)
    @RequestMapping("/updateSysRolePermission")
    public HttpResult<Boolean> updateSysRolePermission(HttpServletRequest request, HttpServletResponse response,UcenterMsRoleVO sysRole) {
        sysRoleService.updateRoleRermission(sysRole);
        return HttpResult.success(true);
    }

    /**
     * @Description: 由于Spring在接受前台传入的List时，就会出现256的IndexOutOfBoundsException异常
     * 设置setAutoGrowCollectionLimit为Integer.MAX_VALUE
     * @author: ZhangBo
     * @date: 15:07 2021/9/26
     * @param binder
     * @return void
     **/
    @InitBinder
    public void dataBinding(WebDataBinder binder) {
        // 给本controller配置接收list的长度Integer.MAX_VALUE，仅在本controller有效
        binder.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
    }
}
