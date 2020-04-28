package com.fhs.bislogger.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fhs.bislogger.dox.LogHistoryDataDO;
import com.fhs.bislogger.vo.LogHistoryDataVO;
import com.fhs.bislogger.mapper.LogHistoryDataMapper;
import com.fhs.bislogger.service.LogHistoryDataService;
import org.springframework.stereotype.Service;
import com.fhs.core.base.service.impl.BaseServiceImpl;

/**
 * (LogHistoryData)表服务实现类
 *
 * @author wanglei
 * @since 2020-04-23 14:27:40
 */
@Service
public class LogHistoryDataServiceImpl extends BaseServiceImpl<LogHistoryDataVO,LogHistoryDataDO> implements LogHistoryDataService {

    @Override
    public LogHistoryDataVO getLastVersionData(String pkey, String namespace) {
        return super.selectOneMP(new LambdaQueryWrapper<LogHistoryDataDO>().eq(LogHistoryDataDO::getPkey,pkey)
                .eq(LogHistoryDataDO::getNamespace,namespace).orderByDesc(LogHistoryDataDO::getVersion));
    }
}