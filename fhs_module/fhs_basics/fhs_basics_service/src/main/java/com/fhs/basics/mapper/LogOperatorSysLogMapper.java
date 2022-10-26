package com.fhs.basics.mapper;

import com.fhs.basics.po.LogOperatorSysLogPO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 系统日志(LogOperatorSysLog)表数据库访问层
 *
 * @author wanglei
 * @since 2022-10-26 11:20:33
 */
@Repository
public interface LogOperatorSysLogMapper extends FhsBaseMapper<LogOperatorSysLogPO> {

}
