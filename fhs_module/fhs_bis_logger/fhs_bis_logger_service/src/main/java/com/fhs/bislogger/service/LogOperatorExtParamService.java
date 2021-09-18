package com.fhs.bislogger.service;

import com.fhs.bislogger.dox.LogOperatorExtParamDO;
import com.fhs.bislogger.vo.LogOperatorExtParamVO;
import com.fhs.core.base.service.BaseService;
import com.fhs.core.cache.annotation.Namespace;

import java.util.List;

/**
 * 扩展参数(LogOperatorExtParam)}表服务接口
 *
 * @author wanglei
 * @since 2020-04-23 13:58:59
 */
@Namespace("log_operator_ext_param")
public interface LogOperatorExtParamService extends BaseService<LogOperatorExtParamVO, LogOperatorExtParamDO> {


}
