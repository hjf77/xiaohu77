package com.fhs.bislogger.mapper;

import com.fhs.bislogger.dox.LogOperatorMainDO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import com.mybatis.jpa.annotation.MapperDefinition;
import org.springframework.stereotype.Repository;

/**
 * (LogOperatorMain)表数据库访问层
 *
 * @author wanglei
 * @since 2020-04-23 13:59:14
 */
@Repository
@MapperDefinition(domainClass = LogOperatorMainDO.class, orderBy = " update_time DESC")
public interface LogOperatorMainMapper extends FhsBaseMapper<LogOperatorMainDO> {

}