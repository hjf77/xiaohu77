package com.fhs.basics.controller;

import com.fhs.basics.constant.BaseTransConstant;
import com.fhs.basics.dox.UcenterMsRoleDO;
import com.fhs.basics.service.UcenterMsRoleService;
import com.fhs.basics.vo.UcenterMsRoleVO;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.bislogger.api.anno.LogMethod;
import com.fhs.bislogger.api.anno.LogNamespace;
import com.fhs.bislogger.constant.LoggerConstant;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.*;
import com.fhs.core.base.pojo.pager.Pager;
import com.fhs.core.exception.ParamException;
import com.fhs.core.result.HttpResult;
import com.fhs.core.safe.repeat.anno.NotRepeat;
import com.fhs.core.valid.group.Add;
import com.fhs.core.valid.group.Update;
import com.fhs.logger.anno.LogDesc;
import com.fhs.module.base.controller.ModelSuperController;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
@LogNamespace(namespace = BaseTransConstant.ROLE_INFO, module = "角色管理")
public class UcenterMsRoleController extends ModelSuperController<UcenterMsRoleVO, UcenterMsRoleDO> {

    /**
     * 角色服务
     */
    @Autowired
    private UcenterMsRoleService sysRoleService;

    /**
     * 获取角色集合
     *
     * @param organizationId 机构id
     */
    @RequiresPermissions("sysRole:see")
    @RequestMapping("/listData/{organizationId}")
    @LogMethod
    public Pager<UcenterMsRoleVO> listData(@PathVariable(value = "organizationId") String organizationId) {
        UcenterMsUserVO sysUser = super.getSessionuser();
        Map<String, Object> map = super.getPageTurnNum();
        if (CheckUtils.isNotEmpty(organizationId)) {
            map.put("organizationId", organizationId);
        } else {
            map.put("organizationId", sysUser.getOrganizationId());
        }
        List<UcenterMsRoleVO> dataList = sysRoleService.findForListFromMap(map);
        int count = sysRoleService.findCountFromMap(map);
        return new Pager<UcenterMsRoleVO>(count, dataList);
    }

    /**
     * 获取角色列表jsonp
     */
    @RequestMapping("getRolesForJsonp")
    @LogMethod
    public void getRolesForJsonp(HttpServletRequest request) {
        PageSizeInfo pageSizeInfo = super.getPageSizeInfo();
        UcenterMsRoleDO roleName = UcenterMsRoleDO.builder().roleName(request.getParameter("roleName")).build();
        List<UcenterMsRoleVO> roles =
                sysRoleService.selectPage(roleName, pageSizeInfo.getPageStart(), pageSizeInfo.getPageSize());
        super.outJsonp(new Pager<UcenterMsRoleVO>(sysRoleService.selectCount(roleName), roles).asJson());
        //        super.outJsonp(listData(null).asJson());
    }

    /**
     * 根据角色ids获取角色列表
     *
     * @param ids
     */
    @RequestMapping("getRoleForJsonpByIds")
    public void getRoleForJsonpByIds(String ids) {
        if (CheckUtils.isNullOrEmpty(ids)) {
            super.outJsonp("[]");
            return;
        }
        List<UcenterMsRoleVO> roles = sysRoleService.findByIds(Arrays.asList(ids.split(",")));
        super.outJsonp(JsonUtils.list2json(roles));
    }


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
    public String[] getRolePermissionButtons(String roleId) {
        return sysRoleService.getRolePermissionButtons(roleId);
    }

    /**
     * @desc 新增修改后台用户  获取当前机构下的角色数据
     */
    @RequestMapping("/getCurrentOrganizationSysRoles")
    public List<UcenterMsRoleVO> getCurrentOrganizationSysRoles(HttpServletRequest request, HttpServletResponse response) {
        //获取当前登录用户
        UcenterMsUserVO sysUser = super.getSessionuser();
        UcenterMsRoleVO sysRole = new UcenterMsRoleVO();
        return sysRoleService.findForList(sysRole);
    }

    /**
     * 角色下拉框获取数据
     *
     * @return
     */
    @RequestMapping("realRoleComBoxData")
    public void realRoleComBoxData() {
        Map<String, Object> map = super.getPageTurnNum();
        map.put("organizationId", super.getSessionuser().getOrganizationId());
        super.outJsonp(JsonUtils.list2json(sysRoleService.findForListFromMap(map)));
    }

    /**
     * 根据机构id获取角色下拉框数据
     *
     * @return
     */
    @RequestMapping("/getSelectOrganSysRoles/{organizationId}")
    public List<UcenterMsRoleVO> getSelectOrganSysRoles(@PathVariable("organizationId") String organizationId) {
        if (StringUtil.isEmpty(organizationId)) {
            throw new ParamException("organizationId 为必传");
        } else {
            Map<String, Object> map = super.getParameterMap();
            map.put("organizationId", organizationId);
            return sysRoleService.findForListFromMap(map);
        }
    }

    @ResponseBody
    @PutMapping("/")
    @RequiresPermissions("sysRole:update")
    @ApiOperation(value = "修改-vue专用")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE, voParamIndex = 0)
    public HttpResult<Boolean> updateForVue(@RequestBody @Validated(Update.class) UcenterMsRoleVO e, HttpServletRequest request,
                                            HttpServletResponse response) {
        return updateSysRole(e);
    }

    /**
     * 更新角色信息
     * @paramadminRole
     */
    @ResponseBody
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE, voParamIndex = 2)
    @RequestMapping("updateSysRole")
    @RequiresPermissions("sysRole:update")
    public HttpResult<Boolean> updateSysRole(UcenterMsRoleVO sysRole) {
        UcenterMsRoleVO oldRole = sysRoleService.selectById(sysRole.getRoleId());
        if (Constant.ENABLED == oldRole.getIsEnable() && Constant.DISABLE == sysRole.getIsEnable()) {
            // 根据roleid查询用户关联表用户数
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("roleId", sysRole.getRoleId());
            Integer count = sysRoleService.findUserCountByRoleId(paramMap);
            if (count > Constant.ENABLED) {
                return HttpResult.error(count > Constant.ENABLED, "该角色拥有关联用户,不可禁用!");
            }
        }
        UcenterMsUserVO sysUser = super.getSessionuser();
        sysRole.setUpdateUser(sysUser.getUserId());
        sysRole.setUpdateTime(new Date());
        sysRoleService.updateRole(sysRole);
        return HttpResult.success(true);
    }

    @NotRepeat
    @ResponseBody
    @RequiresPermissions("sysRole:add")
    @PostMapping("/")
    @ApiOperation(value = "新增-vue专用")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_ADD, voParamIndex = 0)
    public HttpResult<Boolean> save(@RequestBody @Validated(Add.class) UcenterMsRoleVO sysRole, HttpServletRequest request,
                                    HttpServletResponse response) {
        return add(sysRole);
    }

    /**
     * 添加角色
     *
     * @param sysRole
     */
    @RequiresPermissions("sysRole:add")
    @RequestMapping("addSysRole")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_ADD, voParamIndex = 0)
    public HttpResult<Boolean> add(UcenterMsRoleVO sysRole) {
        UcenterMsUserVO sysUser = super.getSessionuser();
        sysRole.setIsDelete(Constant.ZERO);
        sysRole.setCreateUser(sysUser.getUserId());
        sysRole.setCreateTime(new Date());
        sysRole.setGroupCode(sysUser.getGroupCode());
        sysRoleService.addRole(sysRole);
        return HttpResult.success(true);
    }

    /**
     * 根据Id删除角色
     *
     * @param sysRole
     */
    @RequiresPermissions("sysRole:del")
    @RequestMapping("delSysRole")
    @ApiOperation(value = "删除-Easyui专用")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_DEL, voParamIndex = 2)
    public HttpResult<Boolean> del(UcenterMsRoleVO sysRole) {
        sysRoleService.deleteRole(sysRole);
        return HttpResult.success(true);

    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @RequiresPermissions("sysRole:del")
    @ApiOperation(value = "删除-vue专用")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_DEL, pkeyParamIndex = 0)
    public HttpResult<Boolean> delForVue(@ApiParam(name = "id", value = "实体id") @PathVariable String id, HttpServletRequest request) {
        UcenterMsRoleVO role = new UcenterMsRoleVO();
        role.setRoleId(ConverterUtils.toInt(id));
        return del(role);
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
