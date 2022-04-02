package com.fhs.system.dao;

import com.mybatis.jpa.annotation.MapperDefinition;
import com.fhs.core.base.dao.BaseDao;
import com.fhs.system.bean.LogAdminOperatorLog;
import com.mybatis.jpa.annotation.MultiTenancyCheck;

/**
 * @author qixiaobo
 * @version [版本号, 2018-08-11]
 * @Description: Mapper 接口
 * @versio 1.0 陕西小伙伴网络科技有限公司  Copyright (c) 2018 All Rights Reserved.
 */
@MapperDefinition(domainClass = LogAdminOperatorLog.class, orderBy = "create_time DESC")
@MultiTenancyCheck
public interface LogAdminOperatorLogDAO extends BaseDao<LogAdminOperatorLog> {

}
