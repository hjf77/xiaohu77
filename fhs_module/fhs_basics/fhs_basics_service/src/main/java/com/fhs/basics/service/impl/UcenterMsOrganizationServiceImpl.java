package com.fhs.basics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fhs.basics.api.rpc.FeignOrganizationApiService;
import com.fhs.basics.constant.BaseTransConstant;
import com.fhs.basics.dox.UcenterMsOrganizationDO;
import com.fhs.basics.mapper.UcenterMsOrganizationMapper;
import com.fhs.basics.service.UcenterMsOrganizationService;
import com.fhs.basics.vo.ComboboxNodeVO;
import com.fhs.basics.vo.UcenterMsOrganizationVO;
import com.fhs.basics.vo.TreeModelVO;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.StringUtil;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.cache.service.RedisCacheService;
import com.fhs.core.db.ds.DataSource;
import com.fhs.core.result.HttpResult;
import com.fhs.core.trans.anno.AutoTrans;
import com.fhs.excel.form.NamesForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author qixiaobo
 * @version [版本号, 2018-09-04]
 * @Description:后台组织机构表
 * @versio 1.0 陕西小伙伴网络科技有限公司 Copyright (c) 2018 All Rights Reserved.
 */
@Service
@DataSource("base_business")
@AutoTrans(namespace = BaseTransConstant.ORG, fields = "name", useRedis = true, defaultAlias = "org")
public class UcenterMsOrganizationServiceImpl extends BaseServiceImpl<UcenterMsOrganizationVO, UcenterMsOrganizationDO> implements UcenterMsOrganizationService, FeignOrganizationApiService {

    @Autowired
    private UcenterMsOrganizationMapper mapper;

    /**
     * @desc 注入redisde的service对象
     */
    @Autowired
    private RedisCacheService redisCacheService;

    @Override
    public boolean insertOrganization(UcenterMsOrganizationDO adminOrganization) {
        String parentId = adminOrganization.getParentId();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("parentId", parentId);
        Integer ranking = mapper.findRank(parentId);
        ranking = ranking == null ? 0 : ranking;
        ranking = ranking + 1;
        String id = parentId + StringUtil.formatCountWith0("", "%03d", ranking);
        adminOrganization.setRanking(ranking );
        adminOrganization.setId(id);
        //如果当前节点是企业的话，那么企业id用自己的id
        if(adminOrganization.getIsCompany()!=null && adminOrganization.getIsCompany()== Constant.INT_TRUE){
            adminOrganization.setCompanyId(adminOrganization.getId());
        }
        int isAdd = super.insertSelective(adminOrganization);
        if (isAdd >= 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<ComboboxNodeVO> getSubNode(String userOrgId, String parentId) {

        //获取当前数据库
        List<ComboboxNodeVO> dbNode = mapper.getSubNodeDatas(userOrgId, null);

        Map<String, ComboboxNodeVO> comboboxNodeMap = new HashMap<>();
        List<ComboboxNodeVO> fatherNode = new ArrayList<>();
        dbNode.forEach(node -> {
            comboboxNodeMap.put(node.getWxOrgId(), node);
            node.setState("open");
        });
        dbNode.forEach(node -> {
            //如果我有爸爸，就吧我放到我爸爸的儿子集合中
            if (comboboxNodeMap.containsKey(node.getParentId())) {
                comboboxNodeMap.get(node.getParentId()).getChildren().add(node);
            } else {
                //如果我 找不到爸爸，我就是祖宗
                fatherNode.add(node);
            }
        });
//        String nodeJson = JsonUtils.list2json(fatherNode);
        return fatherNode;
    }

    @Override
    public List<TreeModelVO> getTreesData(Map<String, Object> map) {
        return mapper.getTreesData(map);
    }


    @Override
    public boolean validataOrgName(UcenterMsOrganizationDO sysOrganization) {
        int count = mapper.findCountByNameAndParentId(sysOrganization);
        return count <= 0;
    }

    @Override
    public Integer findChildCountById(Map<String, Object> paramMap) {
        return mapper.findChildCountById(paramMap);
    }

    @Override
    public HttpResult<UcenterMsOrganizationDO> getOrgById(String id) {
        return HttpResult.success(super.selectById(id));
    }

    @Override
    public HttpResult<List<UcenterMsOrganizationVO>> getOrgByIds(String ids) {
        if(CheckUtils.isNullOrEmpty(ids)){
            HttpResult.error();
        }
        return HttpResult.success(super.findByIds(Arrays.asList(ids.split(","))));
    }

    @Override
    public Map<String, Object> doUnTrans(NamesForm namesForm) {
        if(namesForm.getNames()==null || namesForm.getNames().isEmpty()){
            return new HashMap<>();
        }
        List<UcenterMsOrganizationVO> orgs = super.selectListMP(new LambdaQueryWrapper<UcenterMsOrganizationDO>().in(UcenterMsOrganizationDO::getName,namesForm.getNames()));
        return  orgs.stream().collect(Collectors
                .toMap(UcenterMsOrganizationVO::getName, UcenterMsOrganizationVO::getId));
    }

    @Override
    public String namespace() {
        return "org";
    }

    @Override
    public List<UcenterMsOrganizationVO> findByIds(List<?> ids) {
        List<UcenterMsOrganizationVO> result = super.findByIds(ids);
        Set<String> idsSet = result.stream().map(UcenterMsOrganizationVO::getCompanyId).collect(Collectors.toSet());
        if(!idsSet.isEmpty()){
            List<UcenterMsOrganizationVO> companyList =  super.findByIds(new ArrayList<>(idsSet));
            Map<String,UcenterMsOrganizationVO> companyMap = companyList.stream().collect(Collectors
                    .toMap(UcenterMsOrganizationVO::getId, Function.identity()));
            for (UcenterMsOrganizationVO ucenterMsOrganizationVO : result) {
                if(ucenterMsOrganizationVO.getIsCompany() == Constant.INT_TRUE){
                    continue;
                }
                if(companyMap.containsKey(ucenterMsOrganizationVO.getCompanyId())){
                    ucenterMsOrganizationVO.setName(ucenterMsOrganizationVO.getName()+"(" + companyMap.get(ucenterMsOrganizationVO.getCompanyId()).getName() + ")");
                }
            }
        }
        return result;
    }
}
