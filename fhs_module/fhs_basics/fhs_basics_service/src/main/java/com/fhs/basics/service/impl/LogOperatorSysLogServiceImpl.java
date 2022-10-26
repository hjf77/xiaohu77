package com.fhs.basics.service.impl;


import com.fhs.basics.vo.LogOperatorSysLogVO;
import com.fhs.basics.po.LogOperatorSysLogPO;
import com.fhs.basics.service.LogOperatorSysLogService;
import org.springframework.stereotype.Service;
import com.fhs.core.db.ds.DataSource;
import com.fhs.core.cache.annotation.Namespace;
import com.fhs.core.base.service.impl.BaseServiceImpl;

/**
 * 系统日志(LogOperatorSysLog)表服务实现类
 *
 * @author wanglei
 * @since 2022-10-26 11:20:30
 */
@Service
@Namespace("log_operator_sys_log")
public class LogOperatorSysLogServiceImpl extends BaseServiceImpl<LogOperatorSysLogVO,LogOperatorSysLogPO> implements LogOperatorSysLogService {
    
}
