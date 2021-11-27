package com.fhs.bislogger.service;

import com.fhs.bislogger.po.LogHistoryDataPO;
import com.fhs.bislogger.vo.LogHistoryDataVO;
import com.fhs.core.base.service.BaseService;
import com.fhs.core.cache.annotation.Namespace;

/**
 * 日志历史(LogHistoryData)}表服务接口
 *
 * @author wanglei
 * @since 2020-04-23 14:27:40
 */
@Namespace("log_history_data")
public interface LogHistoryDataService extends BaseService<LogHistoryDataVO, LogHistoryDataPO> {

    /**
     * 获取某个主键某个namespace最后一个版本的数据
     *
     * @param pkey      主键
     * @param namespace namespace
     * @return 最后版本的数据
     */
    LogHistoryDataVO getLastVersionData(String pkey, String namespace);


}
