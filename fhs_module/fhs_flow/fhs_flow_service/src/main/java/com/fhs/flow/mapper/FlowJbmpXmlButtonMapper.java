package com.fhs.flow.mapper;

import com.fhs.flow.dox.FlowJbmpXmlButtonDO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import com.mybatis.jpa.annotation.MapperDefinition;
import org.springframework.stereotype.Repository;

/**
 * 流程自定义按钮(FlowJbmpXmlButton)表数据库访问层
 *
 * @author wanglei
 * @since 2020-06-16 14:54:53
 */
@Repository
@MapperDefinition(domainClass = FlowJbmpXmlButtonDO.class, orderBy = " update_time DESC")
public interface FlowJbmpXmlButtonMapper extends FhsBaseMapper<FlowJbmpXmlButtonDO> {

}