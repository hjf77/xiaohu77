package com.fhs.bislogger.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fhs.bislogger.po.LogHistoryDataPO;
import com.fhs.bislogger.vo.LogHistoryDataVO;
import com.fhs.bislogger.mapper.LogHistoryDataMapper;
import com.fhs.bislogger.service.LogHistoryDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fhs.core.base.service.impl.BaseServiceImpl;

/**
 * 日志历史(LogHistoryData)表服务实现类
 *
 * @author wanglei
 * @since 2020-04-23 14:27:40
 */
@Service
public class LogHistoryDataServiceImpl extends BaseServiceImpl<LogHistoryDataVO, LogHistoryDataPO> implements LogHistoryDataService {

    @Autowired
    private LogHistoryDataMapper logHistoryDataMapper;

    @Override
    public LogHistoryDataVO getLastVersionData(String pkey, String namespace) {
        return super.selectOneMP(new LambdaQueryWrapper<LogHistoryDataPO>().eq(LogHistoryDataPO::getPkey, pkey)
                .eq(LogHistoryDataPO::getNamespace, namespace).orderByDesc(LogHistoryDataPO::getVersion).last(" limit 0,1"));
    }

}
