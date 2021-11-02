package com.fhs.basics.mapper;

import com.fhs.basics.po.ServiceAreaPO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import com.mybatis.jpa.annotation.MapperDefinition;

/**
 * 省市区字典
 *
 * @Filename: AreaDAO.java
 * @Description:
 * @Version: 1.0
 * @Author: wanglei
 * @Email: qxb@sxpartner.com
 * @History:<br> 陕西小伙伴网络科技有限公司
 * Copyright (c) 2017 All Rights Reserved.
 */
@MapperDefinition(domainClass = ServiceAreaPO.class)
public interface ServiceAreaMapper extends FhsBaseMapper<ServiceAreaPO> {
}