package com.fhs.flow.mapper;

import com.fhs.flow.dox.FlowJbmpAuthFormDO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import com.mybatis.jpa.annotation.MapperDefinition;
import org.springframework.stereotype.Repository;

/**
 * 自定义审批表单(FlowJbmpAuthForm)表数据库访问层
 *
 * @author wanglei
 * @since 2021-06-01 15:03:57
 */
@Repository
@MapperDefinition(domainClass = FlowJbmpAuthFormDO.class, orderBy = " update_time DESC")
public interface FlowJbmpAuthFormMapper extends FhsBaseMapper<FlowJbmpAuthFormDO> {

}
