package com.fhs.basics.controller;


import com.fhs.basics.constant.BaseTransConstant;
import com.fhs.basics.dox.UcenterMsOrganizationDO;
import com.fhs.basics.service.UcenterMsOrganizationService;
import com.fhs.basics.service.UcenterMsUserService;
import com.fhs.basics.vo.TreeModelVO;
import com.fhs.basics.vo.UcenterMsOrganizationVO;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.bislogger.api.anno.LogMethod;
import com.fhs.bislogger.api.anno.LogNamespace;
import com.fhs.bislogger.constant.LoggerConstant;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.JsonUtils;
import com.fhs.core.result.HttpResult;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qixiaobo
 * @version [版本号, 2018-09-04]
 * @Description:后台组织机构表
 * @versio 1.0 陕西小伙伴网络科技有限公司 Copyright (c) 2018 All Rights Reserved.
 */
@RestController
@Api(tags = {"后台组织机构"})
@RequestMapping("ms/sysOrganization")
@LogNamespace(namespace = BaseTransConstant.ORG,module = "机构管理")
public class UcenterMsOrganizationController extends ModelSuperController<UcenterMsOrganizationVO, UcenterMsOrganizationDO> {

    /**
     * 机构服务
     */
    @Autowired
    private UcenterMsOrganizationService sysOrganizationService;

    /**
     * 后台用户服务
     */
    @Autowired
    private UcenterMsUserService sysUserService;

    /**
     * 获取机构菜单树结构json字符串对象 菜单管理左侧树形结构
     */
    @RequiresPermissions("sysOrganization:see")
    @RequestMapping("getTreesData")
    public void getTreesData(String id) {
        Map<String, Object> map = super.getPageTurnNum();
        UcenterMsUserVO sysUser = super.getSessionuser();
        map.put("organizationId", sysUser.getOrganizationId());
        map.put("pid", id);
        List<TreeModelVO> treesData = sysOrganizationService.getTreesData(map);
        String jsonTree = JsonUtils.list2json(treesData);
        super.getRequest().setAttribute("datas", jsonTree);
        super.outJsonp(jsonTree);
    }



    /**
     * 更新
     * @param e  bean
     * @param request
     * @param response
     * @return
     */
    @Override
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE,voParamIndex = 0)
    public HttpResult<Boolean> update(UcenterMsOrganizationVO e,  HttpServletRequest request, HttpServletResponse response) {
        UcenterMsOrganizationVO oldOrg = sysOrganizationService.selectById(e.getId());
        // 如果是启用改为禁用
        if (Constant.ENABLED == oldOrg.getIsEnable() && Constant.DISABLE == e.getIsEnable()) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("organizationId", e.getId());
            paramMap.put("isDisable", Constant.ENABLED);
            // 查询当前机构下级机构数
            Integer orgCount = sysOrganizationService.findChildCountById(paramMap);
            if (orgCount > Constant.ENABLED) {
                return HttpResult.error(null, "该机构拥有子机构,不可禁用!");
            }
            // 查询当前机构和下级机构人员
            Integer userCount = sysUserService.findUserByOrgId(paramMap);
            if (userCount > Constant.ENABLED) {
                return HttpResult.error(null, "该机构下拥有用户,不可禁用!");
            }
        }
        if(CheckUtils.isNullOrEmpty(e.getExtJson())){
            e.setExtJson(null);
        }
        return super.update(e, request, response);
    }

    /**
     * 删除
     *
     * @param id      id
     * @param request
     * @return
     */
    @Override
    @LogMethod(type=LoggerConstant.METHOD_TYPE_DEL,pkeyParamIndex = 0)
    public HttpResult del(String id, HttpServletRequest request) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("organizationId", id);
        paramMap.put("isDisable", null);
        // 查询当前机构下级机构数
        Integer orgCount = sysOrganizationService.findChildCountById(paramMap);
        if (orgCount > Constant.ENABLED) {
            return HttpResult.error(null, "该机构拥有子机构,不可删除!");
        }
        // 查询当前机构和下级机构人员
        Integer userCount = sysUserService.findUserByOrgId(paramMap);
        if (userCount > Constant.ENABLED) {
            return HttpResult.error(null, "该机构下拥有用户,不可删除!");
        }
        // 正常删除
        return super.del(id, request);
    }

    /**
     * 新增机构
     *
     * @param request
     * @param response
     * @param sysOrganization
     */
    @RequiresPermissions("sysOrganization:add")
    @RequestMapping("insertOrganization")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_ADD,voParamIndex = 2)
    public HttpResult insertOrganization(HttpServletRequest request, HttpServletResponse response,
                                         UcenterMsOrganizationVO sysOrganization) {
        if (!CheckUtils.isNullOrEmpty(sysOrganization.getParentId())) {
            UcenterMsOrganizationVO sysOrganizationQuery = sysOrganizationService.selectById(sysOrganization.getParentId());
            if (!CheckUtils.isNullOrEmpty(sysOrganizationQuery) && Constant.ENABLED != sysOrganizationQuery.getIsEnable()) {
                return HttpResult.error(sysOrganizationQuery, "父机构处于禁用状态，不能添加子机构");
            }
        }
        sysOrganization.setGroupCode(super.getSessionuser().getGroupCode());
        sysOrganization.preInsert(super.getSessionuser().getUserId());
        sysOrganizationService.insertOrganization(sysOrganization);
        return HttpResult.success();
    }

    /**
     * 获取机构ID combotree格式
     *
     * @param request 请求
     * @return combotree数据格式
     */
    @RequestMapping(value = "/getOrgIdComBoxData")
    public void getOrgIdComBoxData(HttpServletRequest request, HttpServletResponse response) {
        // 查询根级组织 为当前系统的登录用户组织
        UcenterMsUserVO sysUser = super.getSessionuser();
        super.outJsonp(JsonUtils.list2json(sysOrganizationService.getSubNode(sysUser.getOrganizationId(), request.getParameter("parentId"))));
    }
}
