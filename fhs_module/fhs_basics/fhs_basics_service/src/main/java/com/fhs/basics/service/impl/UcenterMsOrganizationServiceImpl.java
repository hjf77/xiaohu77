package com.fhs.basics.service.impl;

import com.fhs.basics.constant.BaseTransConstant;
import com.fhs.basics.context.UserContext;
import com.fhs.basics.mapper.UcenterMsOrganizationMapper;
import com.fhs.basics.po.UcenterMsOrganizationPO;
import com.fhs.basics.po.UcenterMsUserPO;
import com.fhs.basics.service.UcenterMsOrganizationService;
import com.fhs.basics.service.UcenterMsUserService;
import com.fhs.basics.vo.UcenterMsOrganizationVO;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.common.constant.Constant;
import com.fhs.common.tree.TreeNode;
import com.fhs.common.tree.Treeable;
import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.common.utils.StringUtils;
import com.fhs.common.utils.TreeUtils;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.db.ds.DataSource;
import com.fhs.core.exception.ParamException;
import com.fhs.core.trans.anno.AutoTrans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wanglei
 * @version [版本号, 2018-09-04]
 * @Description:后台组织机构表
 * @versio 1.0 陕西小伙伴网络科技有限公司 Copyright (c) 2018 All Rights Reserved.
 */
@Service
@DataSource("basic")
@AutoTrans(namespace = BaseTransConstant.ORG, fields = "name", defaultAlias = "org")
public class UcenterMsOrganizationServiceImpl extends BaseServiceImpl<UcenterMsOrganizationVO, UcenterMsOrganizationPO> implements UcenterMsOrganizationService {

    @Autowired
    private UcenterMsOrganizationMapper mapper;

    /**
     * 后台用户服务
     */
    @Autowired
    private UcenterMsUserService sysUserService;


    @Override
    public int insertSelective(UcenterMsOrganizationPO organization) {
        if (!CheckUtils.isNullOrEmpty(organization.getParentId())) {
            UcenterMsOrganizationVO sysOrganizationQuery = this.selectById(organization.getParentId());
            if (!CheckUtils.isNullOrEmpty(sysOrganizationQuery) && Constant.ENABLED != sysOrganizationQuery.getIsEnable()) {
                throw new ParamException("父机构处于禁用状态，不能添加子机构");
            }
            organization.setCompanyId(sysOrganizationQuery.getCompanyId());
        }
        UcenterMsUserVO user = UserContext.getSessionuser();
        organization.setGroupCode(user.getGroupCode());
        organization.preInsert(user.getUserId());
        String parentId = organization.getParentId();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("parentId", parentId);
        Integer ranking = mapper.findRank(parentId);
        ranking = ranking == null ? 0 : ranking;
        ranking = ranking + 1;
        String id = parentId + StringUtils.formatCountWith0("", "%03d", ranking);
        organization.setRanking(ranking);
        //是管道时不重置ID
        if (organization.getIsPipeline() != 1) {
            organization.setId(id);
        }
        //如果当前节点是企业的话，那么企业id用自己的id
        if (organization.getIsCompany() != null && organization.getIsCompany() == Constant.INT_TRUE) {
            organization.setCompanyId(organization.getId());
        }
        return super.insertSelective(organization);
    }


    @Override
    public List<UcenterMsOrganizationVO> findByIds(List ids) {
        return initCompanyName(super.findByIds(ids));
    }

    /**
     * 初始化部门的单位名称
     *
     * @param vos
     * @return
     */
    private List<UcenterMsOrganizationVO> initCompanyName(List<UcenterMsOrganizationVO> vos) {
        Set<String> idsSet = vos.stream().map(UcenterMsOrganizationVO::getCompanyId).collect(Collectors.toSet());
        if (!idsSet.isEmpty()) {
            List<UcenterMsOrganizationVO> companyList = super.findByIds(new ArrayList<>(idsSet));
            Map<String, UcenterMsOrganizationVO> companyMap = companyList.stream().collect(Collectors
                    .toMap(UcenterMsOrganizationVO::getId, Function.identity()));
            for (UcenterMsOrganizationVO ucenterMsOrganizationVO : vos) {
                if (ucenterMsOrganizationVO.getIsCompany() == Constant.INT_TRUE) {
                    continue;
                }
                if (companyMap.containsKey(ucenterMsOrganizationVO.getCompanyId())) {
                    ucenterMsOrganizationVO.setName(ucenterMsOrganizationVO.getName() + "(" + companyMap.get(ucenterMsOrganizationVO.getCompanyId()).getName() + ")");
                }
            }
        }
        return vos;
    }

    @Override
    public int updateSelectiveById(UcenterMsOrganizationPO org) {
        UcenterMsOrganizationVO oldOrg = this.selectById(org.getId());
        // 如果是启用改为禁用
        if (Constant.ENABLED == oldOrg.getIsEnable() && Constant.DISABLE == org.getIsEnable()) {
            // 查询当前机构下级机构数
            Long orgCount = this.findCount(UcenterMsOrganizationPO.builder().parentId(org.getId()).isEnable(Constant.ENABLED).build());
            if (orgCount > 0) {
                throw new ParamException("拥有子结构不可禁用");
            }
            // 查询当前机构和下级机构人员
            Long userCount = sysUserService.findCount(UcenterMsUserPO.builder().organizationId(oldOrg.getId()).build());
            if (userCount > 0) {
                throw new ParamException("该机构下拥有用户,不可禁用!");
            }
        }
        if (CheckUtils.isNullOrEmpty(org.getExtJson())) {
            org.setExtJson(null);
        }
        return super.updateSelectiveById(org);
    }

    @Override
    public int deleteById(Serializable id) {
        // 查询当前机构下级机构数
        Long orgCount = this.findCount(UcenterMsOrganizationPO.builder().parentId(ConverterUtils.toString(id)).build());
        ;
        if (orgCount > 0) {
            throw new ParamException("该机构拥有子机构,不可删除!");
        }
        // 查询当前机构和下级机构人员
        Long userCount = sysUserService.findCount(UcenterMsUserPO.builder().organizationId(ConverterUtils.toString(id)).build());
        if (userCount > 0) {
            throw new ParamException("该机构下拥有用户,不可删除!");
        }
        return super.deleteById(id);
    }

    @Override
    public List<UcenterMsOrganizationVO> select() {
        return initCompanyName(super.select());
    }


    @Override
    public List<TreeNode<Treeable>> selectOrgTree(String needCompany, String orgLevel) {
        List<UcenterMsOrganizationPO> ucenterMsOrganizationPOS = mapper.selectOrgTree(orgLevel);
        if (StringUtils.isEmpty(needCompany) || needCompany.equals("yes")) {
            return TreeUtils.formartTree(ucenterMsOrganizationPOS);
        }
        return TreeUtils.formartTreeByCompany(ucenterMsOrganizationPOS);
    }

    @Override
    public List<UcenterMsOrganizationPO> selectOrg(String orgLevel) {
        return this.mapper.selectOrg(orgLevel);
    }

    @Override
    public List<String> selectAllParentId(UcenterMsOrganizationPO po) {
        List<String> code = new ArrayList<>();
        while (!po.getParentId().equals("001")){
            po = mapper.selectById(po.getParentId());
            code.add(po.getId());
        }
        Collections.reverse(code);
        return code;
    }

}
