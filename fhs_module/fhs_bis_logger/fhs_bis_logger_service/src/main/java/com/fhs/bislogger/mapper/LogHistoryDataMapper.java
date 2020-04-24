package com.fhs.bislogger.mapper;

import com.fhs.bislogger.dox.LogHistoryDataDO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import com.mybatis.jpa.annotation.MapperDefinition;
import org.springframework.stereotype.Repository;

/**
 * (LogHistoryData)表数据库访问层
 *
 * @author wanglei
 * @since 2020-04-23 14:27:43
 */
@Repository
@MapperDefinition(domainClass = LogHistoryDataDO.class, orderBy = " update_time DESC")
public interface LogHistoryDataMapper extends FhsBaseMapper<LogHistoryDataDO> {

}