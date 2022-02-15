package com.fhs.basics.controller;


import com.fhs.basics.po.UcenterMsOrganizationPO;
import com.fhs.basics.service.UcenterMsOrganizationService;
import com.fhs.basics.vo.UcenterMsOrganizationVO;
import com.fhs.basics.api.anno.LogNamespace;
import com.fhs.common.constant.Constant;
import com.fhs.common.tree.TreeNode;
import com.fhs.common.tree.Treeable;
import com.fhs.common.utils.TreeUtils;
import com.fhs.core.base.vo.QueryFilter;
import com.fhs.core.exception.ParamException;
import com.fhs.core.result.HttpResult;
import com.fhs.module.base.controller.ModelSuperController;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wanglei
 * @version [版本号, 2018-09-04]
 * @Description:后台组织机构表
 * @versio 1.0 陕西小伙伴网络科技有限公司 Copyright (c) 2018 All Rights Reserved.
 */
@RestController
@Api(tags = {"后台组织机构"})
@ApiGroup(group = "group_default")
@RequestMapping("ms/sysOrganization")
@LogNamespace(namespace = "sysOrganization", module = "机构管理")
public class UcenterMsOrganizationController extends ModelSuperController<UcenterMsOrganizationVO, UcenterMsOrganizationPO> {

    /**
     * 机构服务
     */
    @Autowired
    private UcenterMsOrganizationService sysOrganizationService;

    /**
     * 下拉tree
     *
     * @param filter 过滤条件
     * @return
     */
    @ApiOperation("下拉专用tree")
    @PostMapping("selectTree")
    public List<TreeNode<Treeable>> selectTree(@RequestBody QueryFilter<UcenterMsOrganizationPO> filter) {
        List<UcenterMsOrganizationVO> orgs = sysOrganizationService.selectListMP(filter.asWrapper(UcenterMsOrganizationPO.class));
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

    /**
     * 跟据parentId联动查询组织机构
     * @param ucenterMsOrganizationPO
     * @return
     */
    @ApiOperation("跟据parentId联动查询组织机构")
    @PostMapping("selectOrgList")
    public List<UcenterMsOrganizationVO> selectOrgList(@RequestBody UcenterMsOrganizationPO ucenterMsOrganizationPO){
        List<UcenterMsOrganizationVO> orgList = sysOrganizationService.findForList(ucenterMsOrganizationPO);
        return orgList;
    }
}
