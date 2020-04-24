package com.fhs.bislogger.service;

import com.fhs.bislogger.dox.LogLoginDO;
import com.fhs.bislogger.vo.LogLoginVO;
import com.fhs.core.base.service.BaseService;
import com.fhs.core.cache.annotation.Namespace;

/**
 * 登录日志(LogLogin)}表服务接口
 *
 * @author wanglei
 * @since 2020-04-23 13:58:36
 */
@Namespace("log_login")
public interface LogLoginService extends BaseService<LogLoginVO,LogLoginDO>{

    

}