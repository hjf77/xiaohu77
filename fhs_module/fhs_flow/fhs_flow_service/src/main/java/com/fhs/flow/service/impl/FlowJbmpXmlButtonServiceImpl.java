package com.fhs.flow.service.impl;

import com.fhs.flow.dox.FlowJbmpXmlButtonDO;
import com.fhs.flow.vo.FlowJbmpXmlButtonVO;
import com.fhs.flow.mapper.FlowJbmpXmlButtonMapper;
import com.fhs.flow.service.FlowJbmpXmlButtonService;
import org.springframework.stereotype.Service;
import com.fhs.core.cache.annotation.Namespace;
import com.fhs.core.base.service.impl.BaseServiceImpl;

/**
 * 流程自定义按钮(FlowJbmpXmlButton)表服务实现类
 *
 * @author wanglei
 * @since 2020-06-16 14:54:50
 */
@Service
@Namespace("flow_jbmp_xml_button")
public class FlowJbmpXmlButtonServiceImpl extends BaseServiceImpl<FlowJbmpXmlButtonVO,FlowJbmpXmlButtonDO> implements FlowJbmpXmlButtonService {
    
}