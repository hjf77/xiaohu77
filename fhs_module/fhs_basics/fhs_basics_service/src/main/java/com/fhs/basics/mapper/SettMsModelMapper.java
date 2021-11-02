package com.fhs.basics.mapper;

import com.fhs.basics.po.SettMsModelPO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import com.mybatis.jpa.annotation.MapperDefinition;
import org.springframework.stereotype.Repository;

/**
 * 系统模块mapper
 *
 * @author jianbo.qin
 * @since 2018-05-29
 */
@Repository
@MapperDefinition(domainClass = SettMsModelPO.class)
public interface SettMsModelMapper extends FhsBaseMapper<SettMsModelPO> {

}
