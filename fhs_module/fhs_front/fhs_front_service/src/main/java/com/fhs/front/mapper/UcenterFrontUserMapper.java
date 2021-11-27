package com.fhs.front.mapper;

import com.fhs.core.base.mapper.FhsBaseMapper;
import com.fhs.front.po.UcenterFrontUserPO;
import com.mybatis.jpa.annotation.MapperDefinition;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 前端用户表(UcenterFrontUser)表数据库访问层
 *
 * @author jackwong
 * @since 2019-03-11 14:14:58
 */
@Repository
@MapperDefinition(domainClass = UcenterFrontUserPO.class, orderBy = " update_time DESC")
public interface UcenterFrontUserMapper extends FhsBaseMapper<UcenterFrontUserPO> {

    List<UcenterFrontUserPO> findListFilterMobile();

}
