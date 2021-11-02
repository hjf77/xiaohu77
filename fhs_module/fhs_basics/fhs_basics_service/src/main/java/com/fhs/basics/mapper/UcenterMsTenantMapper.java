package com.fhs.basics.mapper;

import com.fhs.basics.po.UcenterMsTenantPO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import com.mybatis.jpa.annotation.MapperDefinition;

/**
 * 租户管理(UcenterMsTenant)表数据库访问层
 *
 * @author makejava
 * @since 2019-05-15 14:21:04
 */
@MapperDefinition(domainClass = UcenterMsTenantPO.class, orderBy = " update_time DESC")
public interface UcenterMsTenantMapper extends FhsBaseMapper<UcenterMsTenantPO> {

}