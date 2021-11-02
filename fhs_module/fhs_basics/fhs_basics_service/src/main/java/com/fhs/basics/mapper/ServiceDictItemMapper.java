package com.fhs.basics.mapper;

import com.fhs.basics.po.ServiceDictItemPO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import com.mybatis.jpa.annotation.MapperDefinition;

/**
 * 字典表dao
 *
 * @author wanglei
 * @version [版本号, 2015年8月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@MapperDefinition(domainClass = ServiceDictItemPO.class)
public interface ServiceDictItemMapper extends FhsBaseMapper<ServiceDictItemPO> {

}
