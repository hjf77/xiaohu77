package com.fhs.basics.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fhs.basics.po.UcenterMsRolePO;
import com.fhs.basics.service.UcenterMsRoleService;
import com.fhs.basics.service.UcenterMsUserService;
import com.fhs.basics.vo.UcenterMsRoleVO;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.basics.api.anno.LogMethod;
import com.fhs.basics.api.anno.LogNamespace;
import com.fhs.basics.constant.LoggerConstant;
import com.fhs.common.constant.Constant;
import com.fhs.core.exception.ParamException;
import com.fhs.core.result.HttpResult;
import com.fhs.core.safe.repeat.anno.NotRepeat;
import com.fhs.core.valid.group.Add;
import com.fhs.core.valid.group.Update;
import com.fhs.module.base.controller.ModelSuperController;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


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
public class UcenterMsRoleController extends ModelSuperController<UcenterMsRoleVO, UcenterMsRolePO> {

    /**
     * 角色服务
     */
    @Autowired
    private UcenterMsRoleService sysRoleService;
    @Autowired
    private UcenterMsUserService ucenterMsUserService;


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


    @ResponseBody
    @PutMapping("/")
    @SaCheckRole("sysRole:update")
    @ApiOperation(value = "修改-vue专用")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE, voParamIndex = 0)
    public HttpResult<Boolean> update(@RequestBody @Validated(Update.class) UcenterMsRoleVO sysRole, HttpServletRequest request,
                                            HttpServletResponse response) {
        UcenterMsRoleVO oldRole = sysRoleService.selectById(sysRole.getRoleId());
        if (Constant.ENABLED == oldRole.getIsEnable() && Constant.DISABLE == sysRole.getIsEnable()) {
            // 根据roleid查询用户关联表用户数
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("roleId", sysRole.getRoleId());
            Integer count = sysRoleService.findUserCountByRoleId(paramMap);
            if (count > 0) {
                throw new ParamException("该角色拥有关联用户,不可禁用");
            }
        }
        //跟据角色名称、角色id 查询不包含要修改的角色总数 0为名称没有重复 大于0为角色名称有重复
        Long roleCount = sysRoleService.selectCountMP(new LambdaQueryWrapper<UcenterMsRolePO>()
                .eq(UcenterMsRolePO::getRoleName, sysRole.getRoleName())
                .ne(UcenterMsRolePO::getRoleId, sysRole.getRoleId()));
        //角色名称不能重复
        if(roleCount > 0){
            throw new ParamException("该角色名称重复,不能修改");
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
    public HttpResult<Boolean> add(@RequestBody @Validated(Add.class) UcenterMsRoleVO sysRole, HttpServletRequest request,
                                   HttpServletResponse response) {
        //跟据角色名称 查询角色名称是否有重复名称 0为名称没有重复 大于0为角色名称有重复
        Long roleCount = sysRoleService.selectCountMP(new LambdaQueryWrapper<UcenterMsRolePO>()
                .eq(UcenterMsRolePO::getRoleName, sysRole.getRoleName()));
        //角色名称不能重复
        if(roleCount > 0){
            throw new ParamException("该角色名称重复,不能新增");
        }
        UcenterMsUserVO sysUser = super.getSessionuser();
        sysRole.setIsDelete(Constant.ZERO);
        sysRole.setCreateUser(sysUser.getUserId());
        sysRole.setCreateTime(new Date());
        sysRole.setGroupCode(sysUser.getGroupCode());
        sysRoleService.addRole(sysRole);
        return HttpResult.success(true);
    }


    @DeleteMapping("/{id}")
    @ResponseBody
    @SaCheckRole("sysRole:del")
    @ApiOperation(value = "删除-vue专用")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_DEL, pkeyParamIndex = 0)
    public HttpResult<Boolean> del(@ApiParam(name = "id", value = "实体id") @PathVariable String id, HttpServletRequest request) {
        //判断用户下有没有该角色
        int userRoleCount = ucenterMsUserService.findUserRoleCount(Integer.parseInt(id));
        if (userRoleCount > 0) {
            return HttpResult.error(false,"该角色下有用户，不能删除！");
        }
        sysRoleService.deleteById(id);
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
