package com.fhs.bislogger.service.impl;

import com.fhs.bislogger.dox.LogOperatorMainDO;
import com.fhs.bislogger.vo.LogOperatorMainVO;
import com.fhs.bislogger.mapper.LogOperatorMainMapper;
import com.fhs.bislogger.service.LogOperatorMainService;
import com.fhs.core.db.ds.DataSource;
import org.springframework.stereotype.Service;
import com.fhs.core.base.service.impl.BaseServiceImpl;

/**
 * (LogOperatorMain)表服务实现类
 *
 * @author wanglei
 * @since 2020-04-23 13:59:14
 */
@Service
@DataSource("log")
public class LogOperatorMainServiceImpl extends BaseServiceImpl<LogOperatorMainVO,LogOperatorMainDO> implements LogOperatorMainService {
    
}