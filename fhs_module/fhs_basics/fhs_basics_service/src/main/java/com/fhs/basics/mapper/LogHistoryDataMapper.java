package com.fhs.basics.mapper;

import com.fhs.basics.po.LogHistoryDataPO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 日志历史(LogHistoryData)表数据库访问层
 *
 * @author wanglei
 * @since 2020-04-23 14:27:43
 */
@Repository
public interface LogHistoryDataMapper extends FhsBaseMapper<LogHistoryDataPO> {

}
