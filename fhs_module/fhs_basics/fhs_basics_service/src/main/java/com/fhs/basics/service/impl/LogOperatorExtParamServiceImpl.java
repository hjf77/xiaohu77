package com.fhs.basics.service.impl;

import com.fhs.basics.po.LogOperatorExtParamPO;
import com.fhs.basics.vo.LogOperatorExtParamVO;
import com.fhs.basics.service.LogOperatorExtParamService;
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
@DataSource("basic")
public class LogOperatorExtParamServiceImpl extends BaseServiceImpl<LogOperatorExtParamVO, LogOperatorExtParamPO> implements LogOperatorExtParamService {

}
