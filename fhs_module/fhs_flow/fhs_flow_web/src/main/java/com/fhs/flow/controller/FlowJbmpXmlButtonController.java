package com.fhs.flow.controller;

import com.fhs.flow.dox.FlowJbmpXmlButtonDO;
import com.fhs.flow.vo.FlowJbmpXmlButtonVO;
import org.springframework.web.bind.annotation.*;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;

/**
 * 流程自定义按钮(FlowJbmpXmlButton)表控制层
 *
 * @author wanglei
 * @since 2020-06-16 14:54:50
 */

@RestController
@Api(tags={"流程自定义按钮"})
@RequestMapping("/ms/flow_jbmp_xml_button")
public class FlowJbmpXmlButtonController extends ModelSuperController<FlowJbmpXmlButtonVO,FlowJbmpXmlButtonDO> {
   

}