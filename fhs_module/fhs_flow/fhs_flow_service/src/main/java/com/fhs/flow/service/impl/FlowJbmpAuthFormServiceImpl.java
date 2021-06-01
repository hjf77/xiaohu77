package com.fhs.flow.service.impl;

import com.fhs.flow.dox.FlowJbmpAuthFormDO;
import com.fhs.flow.vo.FlowJbmpAuthFormVO;
import com.fhs.flow.mapper.FlowJbmpAuthFormMapper;
import com.fhs.flow.service.FlowJbmpAuthFormService;
import org.springframework.stereotype.Service;
import com.fhs.core.cache.annotation.Namespace;
import com.fhs.core.base.service.impl.BaseServiceImpl;

/**
 * 自定义审批表单(FlowJbmpAuthForm)表服务实现类
 *
 * @author wanglei
 * @since 2021-06-01 15:03:49
 */
@Service
@Namespace("flow_jbmp_auth_form")
public class FlowJbmpAuthFormServiceImpl extends BaseServiceImpl<FlowJbmpAuthFormVO, FlowJbmpAuthFormDO> implements FlowJbmpAuthFormService {

}
