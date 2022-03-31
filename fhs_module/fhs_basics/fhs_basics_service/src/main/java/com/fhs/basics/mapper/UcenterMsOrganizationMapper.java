package com.fhs.basics.mapper;

import com.fhs.basics.po.UcenterMsOrganizationPO;
import com.fhs.basics.vo.UcenterMsOrganizationVO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wanglei
 * @version [版本号, 2018-09-04]
 * @Description:后台组织机构表 Mapper 接口
 * @versio 1.0 陕西小伙伴网络科技有限公司  Copyright (c) 2018 All Rights Reserved.
 */
@Repository
public interface UcenterMsOrganizationMapper extends FhsBaseMapper<UcenterMsOrganizationPO> {


    /**
     * 查询当前最大的同爸爸排行是第几
     *
     * @param parentId 父亲id
     * @return 当前最大的排行
     */
    Integer findRank(@Param("parentId") String parentId);

    List<UcenterMsOrganizationVO> selectOrgTreeByCentralStat();

    List<UcenterMsOrganizationVO> selectOrgTreeByOpeArea();

    List<UcenterMsOrganizationPO> selectOrgTree(@Param("orgLevel") String orgLevel,@Param("organizationId") String organizationId);

    List<UcenterMsOrganizationPO> selectOrg(@Param("orgLevel") String orgLevel);
}
