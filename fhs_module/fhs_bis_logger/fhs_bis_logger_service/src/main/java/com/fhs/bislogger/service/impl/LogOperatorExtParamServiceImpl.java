package com.fhs.bislogger.service.impl;

import com.fhs.bislogger.po.LogOperatorExtParamPO;
import com.fhs.bislogger.vo.LogOperatorExtParamVO;
import com.fhs.bislogger.service.LogOperatorExtParamService;
import com.fhs.core.db.ds.DataSource;
import org.springframework.stereotype.Service;
import com.fhs.core.base.service.impl.BaseServiceImpl;

/**
 * 扩展参数(LogOperatorExtParam)表服务实现类
 *
 * @author wanglei
 * @since 2020-04-23 13:58:59
 */
@Service
@DataSource("log")
public class LogOperatorExtParamServiceImpl extends BaseServiceImpl<LogOperatorExtParamVO, LogOperatorExtParamPO> implements LogOperatorExtParamService {

}
