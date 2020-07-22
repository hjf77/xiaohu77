package com.fhs.basics.mapper;

import com.fhs.basics.dox.SettMsMenuServerDO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import com.mybatis.jpa.annotation.MapperDefinition;
import org.springframework.stereotype.Repository;

/**
 * @author jianbo.qin
 * @version [版本号, 2018-06-01]
 * @Description: 服务管理Mapper 接口
 * @versio 1.0 陕西小伙伴网络科技有限公司 Copyright (c) 2017 All Rights Reserved.
 */
@MapperDefinition(domainClass = SettMsMenuServerDO.class)
@Repository
public interface SettMsMenuServerMapper extends FhsBaseMapper<SettMsMenuServerDO> {

}
