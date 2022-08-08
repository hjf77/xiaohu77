package com.fhs.basics.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.fhs.basics.api.anno.LogMethod;
import com.fhs.basics.api.anno.LogNamespace;
import com.fhs.basics.constant.LoggerConstant;
import com.fhs.basics.po.UcenterMsRolePO;
import com.fhs.basics.service.UcenterMsRoleService;
import com.fhs.basics.vo.UcenterMsRoleVO;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.common.constant.Constant;
import com.fhs.core.base.valid.group.Add;
import com.fhs.core.base.valid.group.Update;
import com.fhs.core.exception.ParamException;
import com.fhs.core.result.HttpResult;
import com.fhs.core.safe.repeat.anno.NotRepeat;
import com.fhs.module.base.controller.ModelSuperController;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 角色控制器类
 *
 * @author Administrator
 */
@RestController
@Api(tags = {"角色"})
@ApiGroup(group = "group_default")
@RequestMapping("ms/sysRole")
@LogNamespace(namespace = "sysRole", module = "角色管理")
public class UcenterMsRoleController extends ModelSuperController<UcenterMsRoleVO, UcenterMsRolePO, Integer> {

    /**
     * 角色服务
     */
    @Autowired
    private UcenterMsRoleService sysRoleService;


    /**
     * 查询角色的按钮信息
     *
     * @param sysRole
     */
    @RequestMapping("searchButtons")
    public List<Map<String, Object>> searchButtons(UcenterMsRoleVO sysRole) {
        return sysRoleService.searchButtons(sysRole);
    }

    /**
     * vue - element 查询角色的按钮信息
     *
     * @param roleId
     */
    @GetMapping("getRolePermissionButtons")
    public String[] getRolePermissionButtons(Integer roleId) {
        return sysRoleService.getRolePermissionButtons(roleId);
    }


    @Override
    @ResponseBody
    @PutMapping("/")
    @SaCheckRole("sysRole:update")
    @ApiOperation(value = "修改-vue专用")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE, voParamIndex = 0)
    public HttpResult<Boolean> update(@RequestBody @Validated(Update.class) UcenterMsRoleVO sysRole) {
        UcenterMsRoleVO oldRole = sysRoleService.selectById(sysRole.getRoleId());
        if (Constant.ENABLED.equals(oldRole.getIsEnable()) && Constant.DISABLE.equals(sysRole.getIsEnable())) {
            // 根据roleid查询用户关联表用户数
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("roleId", sysRole.getRoleId());
            Integer count = sysRoleService.findUserCountByRoleId(paramMap);
            if (count > 0) {
                throw new ParamException("该角色拥有关联用户,不可禁用");
            }
        }
        UcenterMsUserVO sysUser = super.getSessionuser();
        sysRole.setUpdateUser(sysUser.getUserId());
        sysRole.setUpdateTime(new Date());
        sysRoleService.updateRole(sysRole);
        return HttpResult.success(true);
    }


    @NotRepeat
    @Override
    @ResponseBody
    @SaCheckRole("sysRole:add")
    @PostMapping("/")
    @ApiOperation(value = "新增-vue专用")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_ADD, voParamIndex = 0)
    public HttpResult<Boolean> add(@RequestBody @Validated(Add.class) UcenterMsRoleVO sysRole) {
        UcenterMsUserVO sysUser = super.getSessionuser();
        sysRole.setIsDelete(Constant.ZERO);
        sysRole.setCreateUser(sysUser.getUserId());
        sysRole.setCreateTime(new Date());
        sysRole.setGroupCode(sysUser.getGroupCode());
        sysRoleService.addRole(sysRole);
        return HttpResult.success(true);
    }


    @Override
    @DeleteMapping("/{id}")
    @ResponseBody
    @SaCheckRole("sysRole:del")
    @ApiOperation(value = "删除-vue专用")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_DEL, pkeyParamIndex = 0)
    public HttpResult<Boolean> del(@ApiParam(name = "id", value = "实体id") @PathVariable Integer id) {

        sysRoleService.deleteById(id);
        return HttpResult.success(true);
    }

    /**
     * @param binder
     * @return void
     * @Description: 由于Spring在接受前台传入的List时，就会出现256的IndexOutOfBoundsException异常
     * 设置setAutoGrowCollectionLimit为Integer.MAX_VALUE
     * @author: ZhangBo
     * @date: 15:07 2021/9/26
     **/
    @InitBinder
    public void dataBinding(WebDataBinder binder) {
        // 给本controller配置接收list的长度Integer.MAX_VALUE，仅在本controller有效
        binder.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
    }


}
