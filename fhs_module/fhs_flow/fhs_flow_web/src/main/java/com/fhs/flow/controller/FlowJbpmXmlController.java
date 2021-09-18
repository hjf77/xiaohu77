package com.fhs.flow.controller;


import com.fhs.basics.constant.BaseTransConstant;
import com.fhs.bislogger.api.anno.LogMethod;
import com.fhs.bislogger.api.anno.LogNamespace;
import com.fhs.bislogger.constant.LoggerConstant;
import com.fhs.common.utils.StringUtil;
import com.fhs.core.result.HttpResult;
import com.fhs.core.valid.checker.ParamChecker;
import com.fhs.core.valid.group.Add;
import com.fhs.flow.constant.FlowConstant;
import com.fhs.flow.dox.FlowJbpmXmlDO;
import com.fhs.flow.service.FlowJbpmXmlService;
import com.fhs.flow.vo.FlowJbpmXmlVO;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程列表-xml(FlowJbpmXml)表控制层
 *
 * @author jackwong
 * @since 2019-11-11 14:29:04
 */
@RestController
@Api(tags = {"流程列表"})
@RequestMapping("/ms/flow_jbpm_xml")
@LogNamespace(namespace = "flow_jbpm_xml", module = "流程列表")
public class FlowJbpmXmlController extends ModelSuperController<FlowJbpmXmlVO, FlowJbpmXmlDO> {

    @Autowired
    private FlowJbpmXmlService flowJbpmXmlService;

    /**
     * 部署工作流到引擎里面
     *
     * @param xmlId 里面包含id即可
     */
    @RequestMapping("releaseWorkFlow")
    @RequiresPermissions("flow_jbpm_xml:releaseWorkFlow")
    public HttpResult<Boolean> releaseWorkFlow(String xmlId) {
        ParamChecker.isNotNull(xmlId, "id不能为空");
        flowJbpmXmlService.releaseWorkFlow(xmlId);
        return HttpResult.success(true);
    }

    /**
     * 添加流程数据
     *
     * @param flowJbpmXmlVO 里面包含id即可
     */
    @RequestMapping("addFlow")
    @RequiresPermissions("flow_jbpm_xml:add")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_ADD, voParamIndex = 0)
    public HttpResult<Boolean> addFlow(@Validated(Add.class) FlowJbpmXmlVO flowJbpmXmlVO) {
        flowJbpmXmlVO.setStatus(FlowConstant.XML_STATUS_DRAFT);
        flowJbpmXmlVO.setVersion(1);
        flowJbpmXmlVO.preInsert(super.getSessionuser().getUserId());
        flowJbpmXmlService.insertSelective(flowJbpmXmlVO);
        return HttpResult.success(true);
    }


    /**
     * 更新xml内容
     *
     * @param xml
     * @param xmlId
     * @return
     */
    @RequestMapping("updateXml")
    @RequiresPermissions("flow_jbpm_xml:update")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE)
    public HttpResult<Boolean> updateXml(String xml, String xmlId) {
        ParamChecker.isNotNullOrEmpty(xml, "xml不能为空");
        ParamChecker.isNotNullOrEmpty(xmlId, "xmlId不能为空");
        FlowJbpmXmlVO dbData = flowJbpmXmlService.selectById(xmlId);
        ParamChecker.isNotNullOrEmpty(dbData, "xmlId无效");
        FlowJbpmXmlDO updateParam = FlowJbpmXmlVO.builder().id(xmlId).xml(xml).build();
        if (dbData.getStatus() == FlowConstant.XML_STATUS_RELEASE) {
            updateParam.setStatus(FlowConstant.XML_STATUS_DRAFT);
        }
        updateParam.preUpdate(super.getSessionuser().getUserId());
        flowJbpmXmlService.updateSelectiveById(updateParam);
        return HttpResult.success(true);
    }


}
