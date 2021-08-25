package com.fhs.basics.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fhs.basics.constant.BaseTransConstant;
import com.fhs.basics.constant.OrgConstant;
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
import com.fhs.common.tree.TreeNode;
import com.fhs.common.tree.Treeable;
import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.JsonUtils;
import com.fhs.common.utils.TreeUtils;
import com.fhs.core.base.vo.QueryFilter;
import com.fhs.core.result.HttpResult;
import com.fhs.core.valid.checker.ParamChecker;
import com.fhs.module.base.controller.ModelSuperController;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author qixiaobo
 * @version [版本号, 2018-09-04]
 * @Description:后台组织机构表
 * @versio 1.0 陕西小伙伴网络科技有限公司 Copyright (c) 2018 All Rights Reserved.
 */
@RestController
@Api(tags = {"后台组织机构"})
@ApiGroup(group = "group_default")
@RequestMapping("ms/sysOrganization")
@LogNamespace(namespace = BaseTransConstant.ORG, module = "机构管理")
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
    @GetMapping("getTreesData")
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
     *
     * @param e        bean
     * @param request
     * @param response
     * @return
     */
    @Override
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE, voParamIndex = 0)
    public HttpResult<Boolean> update(UcenterMsOrganizationVO e, HttpServletRequest request, HttpServletResponse response) {
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
        if (CheckUtils.isNullOrEmpty(e.getExtJson())) {
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
    @LogMethod(type = LoggerConstant.METHOD_TYPE_DEL, pkeyParamIndex = 0)
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
    @PostMapping("insertOrganization")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_ADD, voParamIndex = 2)
    public HttpResult insertOrganization(HttpServletRequest request, HttpServletResponse response,
                                         UcenterMsOrganizationVO sysOrganization) {
        if (!CheckUtils.isNullOrEmpty(sysOrganization.getParentId())) {
            UcenterMsOrganizationVO sysOrganizationQuery = sysOrganizationService.selectById(sysOrganization.getParentId());
            if (!CheckUtils.isNullOrEmpty(sysOrganizationQuery) && Constant.ENABLED != sysOrganizationQuery.getIsEnable()) {
                return HttpResult.error(sysOrganizationQuery, "父机构处于禁用状态，不能添加子机构");
            }
            sysOrganization.setCompanyId(sysOrganizationQuery.getCompanyId());
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
    @PostMapping(value = "/getOrgIdComBoxData")
    public void getOrgIdComBoxData(HttpServletRequest request, HttpServletResponse response) {
        // 查询根级组织 为当前系统的登录用户组织
        UcenterMsUserVO sysUser = super.getSessionuser();
        super.outJsonp(JsonUtils.list2json(sysOrganizationService.getSubNode(sysUser.getOrganizationId(), request.getParameter("parentId"))));
    }

    /**
     * 下拉tree
     *
     * @param filter 过滤条件
     * @return
     */
    @ApiOperation("下拉专用tree")
    @PostMapping("selectTree")
    public List<TreeNode<Treeable>> selectTree(@RequestBody QueryFilter<UcenterMsOrganizationDO> filter) {
        List<UcenterMsOrganizationVO> orgs = sysOrganizationService.selectListMP(filter.asWrapper(UcenterMsOrganizationDO.class));
        Map<String, UcenterMsOrganizationVO> orgMap = orgs.stream().collect(Collectors
                .toMap(UcenterMsOrganizationVO::getId, Function.identity()));
        for (UcenterMsOrganizationVO org : orgs) {
            //如果不是个单位，则设置名字为xx(单位名称)
            if (org.getIsCompany() != null && org.getIsCompany() != Constant.INT_TRUE && orgMap.containsKey(org.getCompanyId())) {
                org.setName(org.getName() + "(" + orgMap.get(org.getCompanyId()).getName() + ")");
            }
        }
        return TreeUtils.formartTree(orgs);
    }

    @GetMapping("getUserParentCompany")
    @ApiOperation("获取当前登录人的上级单位信息")
    public UcenterMsOrganizationVO getUserParentCompany() {
        String conpanyId = super.getSessionuser().getCompanyId();
        ParamChecker.isNotNull(conpanyId, "当前登录人没有公司id");
        UcenterMsOrganizationVO company = sysOrganizationService.selectById(conpanyId);
        if (OrgConstant.ORG_ID_ROOT.equals(company.getId())) {
            return company;
        }
        ParamChecker.isNotNull(company, conpanyId + "id无效");
        UcenterMsOrganizationVO parentOrg = sysOrganizationService.selectById(company.getParentId());
        ParamChecker.isNotNull(parentOrg, company.getParentId() + "id无效");
        if (parentOrg.getIsCompany() != null && parentOrg.getIsCompany() == Constant.INT_TRUE) {
            return parentOrg;
        }
        return sysOrganizationService.selectById(parentOrg.getCompanyId());
    }


    /**
     * 获取单位tree
     * @param hierarchy 层级 比如2 代表总公司和一级分公司
     * @param isChild 是否只显示当前登录人下级单位
     * @param isHandleId 是否处理id字段
     * @return
     */
    @GetMapping(value = "/getCompanyTree")
    @ApiOperation("获取公司tree")
    public List<TreeNode> getCompanyTree(Integer hierarchy, Integer isChild,Integer isHandleId) {
        //设置默认为false
        isChild = isChild == null ? Constant.INT_FALSE : isChild;
        isHandleId = isHandleId == null ? Constant.INT_TRUE : isHandleId;

        hierarchy = hierarchy == null ? 0 : hierarchy;
        LambdaQueryWrapper queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.apply(hierarchy != 0, "LENGTH(id)<=" + (hierarchy * 3));
        String companyId = super.getSessionuser().getCompanyId().replace("b", "");
        //如果只要子孙类的话，那么只选子孙类
        queryWrapper.apply(isChild == Constant.INT_TRUE, " id LIKE '" + companyId + "%'");
        List<UcenterMsOrganizationVO> orgs = sysOrganizationService.selectListMP(queryWrapper);
        Map<String, UcenterMsOrganizationVO> orgMap = orgs.stream().collect(Collectors
                .toMap(UcenterMsOrganizationVO::getId, Function.identity()));
        Map<String, TreeNode> nodeMap = new HashMap<>();
        List<TreeNode> result = new ArrayList<>();
        for (UcenterMsOrganizationVO org : orgs) {
            String orgId = org.getId();
            if(isHandleId == Constant.INT_TRUE){
                if (OrgConstant.ORG_ID_ROOT.equals(org.getId())) {
                    orgId = "";
                } else if (!OrgConstant.ORG_ROOT_COMPANY_REAL.equals(org.getId())) {
                    orgId = (org.getId() + "b");
                }
            }
            nodeMap.put(org.getId(), TreeNode.builder().name(org.getName()).id(orgId).parentId(org.getParentId()).data(org).children(new ArrayList<>()).build());
        }
        for (UcenterMsOrganizationVO org : orgs) {

            if (CheckUtils.isNullOrEmpty(org.getParentId()) || (companyId.equals(org.getId()) && isChild == Constant.INT_TRUE)) {
                result.add(nodeMap.get(org.getId()));
                //如果是个组织则找我爸爸的公司id
            } else if (org.getIsCompany() != null && Constant.INT_TRUE == org.getIsCompany() && orgMap.containsKey(org.getParentId())) {
                nodeMap.get(orgMap.get(org.getParentId()).getCompanyId()).getChildren().add(nodeMap.get(org.getId()));
            }
            if(isHandleId == Constant.INT_TRUE) {
                if (OrgConstant.ORG_ID_ROOT.equals(org.getId())) {
                    org.setId("");
                } else if (!OrgConstant.ORG_ROOT_COMPANY_REAL.equals(org.getId())) {
                    org.setId(org.getId() + "b");
                }
            }
        }
        return result;
    }
}
