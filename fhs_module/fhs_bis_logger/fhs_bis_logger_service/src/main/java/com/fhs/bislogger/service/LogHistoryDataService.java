package com.fhs.bislogger.service;

import com.fhs.bislogger.dox.LogHistoryDataDO;
import com.fhs.bislogger.vo.LogHistoryDataVO;
import com.fhs.core.base.service.BaseService;
import com.fhs.core.cache.annotation.Namespace;

/**
 * (LogHistoryData)}表服务接口
 *
 * @author wanglei
 * @since 2020-04-23 14:27:40
 */
@Namespace("log_history_data")
public interface LogHistoryDataService extends BaseService<LogHistoryDataVO,LogHistoryDataDO>{

    

}