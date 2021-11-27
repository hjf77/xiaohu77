package com.fhs.front.mapper;

import com.fhs.core.base.mapper.FhsBaseMapper;
import com.fhs.front.po.UcenterFrontUserBindPO;
import com.mybatis.jpa.annotation.MapperDefinition;
import org.springframework.stereotype.Repository;

/**
 * (UcenterFrontUserBind)表数据库访问层
 *
 * @author jackwong
 * @since 2019-03-11 14:37:18
 */
@Repository
@MapperDefinition(domainClass = UcenterFrontUserBindPO.class, orderBy = " update_time DESC")
public interface UcenterFrontUserBindMapper extends FhsBaseMapper<UcenterFrontUserBindPO> {

}
