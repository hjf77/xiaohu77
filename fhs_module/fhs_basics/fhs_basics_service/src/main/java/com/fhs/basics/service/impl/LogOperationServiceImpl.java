package com.fhs.basics.service.impl;


import com.fhs.basics.po.LogOperationPO;
import com.fhs.basics.service.LogOperationService;
import com.fhs.basics.vo.LogOperationVO;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.cache.annotation.Namespace;
import com.fhs.core.db.ds.DataSource;
import org.springframework.stereotype.Service;

/**
 * (LogOperation)表服务实现类
 *
 * @author LiYuLin
 * @since 2022-03-10 16:44:36
 */
@Service
@DataSource("basic")
@Namespace("log_operation")
public class LogOperationServiceImpl extends BaseServiceImpl<LogOperationVO, LogOperationPO> implements LogOperationService {
    
}
