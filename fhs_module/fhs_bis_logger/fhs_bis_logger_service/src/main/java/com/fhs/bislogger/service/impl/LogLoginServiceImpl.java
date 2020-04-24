package com.fhs.bislogger.service.impl;

import com.fhs.bislogger.dox.LogLoginDO;
import com.fhs.bislogger.vo.LogLoginVO;
import com.fhs.bislogger.mapper.LogLoginMapper;
import com.fhs.bislogger.service.LogLoginService;
import com.fhs.core.db.ds.DataSource;
import org.springframework.stereotype.Service;
import com.fhs.core.base.service.impl.BaseServiceImpl;

/**
 * 登录日志(LogLogin)表服务实现类
 *
 * @author wanglei
 * @since 2020-04-23 13:58:40
 */
@Service
@DataSource("log")
public class LogLoginServiceImpl extends BaseServiceImpl<LogLoginVO,LogLoginDO> implements LogLoginService {
    
}