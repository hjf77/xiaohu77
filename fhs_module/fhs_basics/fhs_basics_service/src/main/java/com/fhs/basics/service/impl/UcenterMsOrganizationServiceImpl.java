package com.fhs.basics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.springframework.context.annotation.Primary;
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
@Primary
@Service
@DataSource("basic")
@AutoTrans(namespace = BaseTransConstant.ORG, fields = {"name","aliasName"}, defaultAlias = "org")
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
        String organizationId = UserContext.getSessionuser().getOrganizationId();
        List<UcenterMsOrganizationPO> ucenterMsOrganizationPOS = mapper.selectOrgTree(orgLevel,organizationId);
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
    public Long findOrgCount(UcenterMsOrganizationPO bean) {
        bean.setIsDelete(Constant.INT_FALSE);
        return baseMapper.selectCount(bean.asWrapper());
    }

    @Override
    public List<UcenterMsOrganizationVO> findOrgForList(UcenterMsOrganizationPO bean) {
        bean.setIsDelete(Constant.INT_FALSE);
        List<UcenterMsOrganizationPO> dos = baseMapper.selectList(bean.asWrapper());
        return pos2vos(dos);
    }

    @Override
    public List<UcenterMsOrganizationVO> findOrgList() {
        return this.selectListMP(new LambdaQueryWrapper<UcenterMsOrganizationPO>().in(UcenterMsOrganizationPO::getParentId,"001001","001002","001003"));
    }

    @Override
    public List<String> selectAllParentId(String id) {
        UcenterMsOrganizationPO organizationPO = mapper.selectById(id);
        List<String> code = new ArrayList<>();
        while (!organizationPO.getId().equals("001")){
            code.add(organizationPO.getId());
            organizationPO = mapper.selectById(organizationPO.getParentId());
        }
        Collections.reverse(code);
        return code;
    }

    @Override
    public String selectOrgList(String id) {
        String ids = "";
        QueryWrapper wrapper = new QueryWrapper();
        if(id.length() == 3 || id.length() == 6){//判断用户是否所属公司 / 输油处、采油厂
            wrapper.apply(" ( id like '"+ id +"%' and length(id) <= 12) ");
            List<UcenterMsOrganizationPO> ucenterMsOrganizationPOS = mapper.selectList(wrapper);
            for (UcenterMsOrganizationPO ucenterMsOrganizationPO : ucenterMsOrganizationPOS) {
                ids += ucenterMsOrganizationPO.getId() + ",";
            }
            ids = ids.substring(0,ids.length() -1);
        }else if(id.length() == 9){//用户所属采油厂
            wrapper.apply(" ( id like '"+ id +"%' and length(id) <= 12) ");
            List<UcenterMsOrganizationPO> ucenterMsOrganizationPOS = mapper.selectList(wrapper);
            for (UcenterMsOrganizationPO ucenterMsOrganizationPO : ucenterMsOrganizationPOS) {
                ids += ucenterMsOrganizationPO.getId() + ",";
            }
            ids += id.substring(0,6);
        }else if(id.length() == 12){//用户所属作业区
            ids += id.substring(0,6)+","+id.substring(0,9)+","+id.substring(0,12);
        }else if(id.length() == 15){//用户所属作业区
            ids += id.substring(0,6)+","+id.substring(0,9)+","+id.substring(0,12)+","+id.substring(0,15);
        }
        return ids;
    }

}
