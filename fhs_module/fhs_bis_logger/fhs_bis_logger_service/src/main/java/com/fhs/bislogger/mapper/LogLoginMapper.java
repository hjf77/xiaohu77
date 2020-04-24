package com.fhs.bislogger.mapper;

import com.fhs.bislogger.dox.LogLoginDO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import com.mybatis.jpa.annotation.MapperDefinition;
import org.springframework.stereotype.Repository;

/**
 * 登录日志(LogLogin)表数据库访问层
 *
 * @author wanglei
 * @since 2020-04-23 13:58:43
 */
@Repository
@MapperDefinition(domainClass = LogLoginDO.class, orderBy = " update_time DESC")
public interface LogLoginMapper extends FhsBaseMapper<LogLoginDO> {

}