package com.fhs.flow.controller.admin.definition;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.fhs.common.utils.ClassUtils;
import com.fhs.core.base.vo.FhsPager;
import com.fhs.core.base.vo.QueryFilter;
import com.fhs.core.result.HttpResult;
import com.fhs.flow.controller.admin.definition.vo.process.BpmProcessDefinitionListReqVO;
import com.fhs.flow.controller.admin.definition.vo.process.BpmProcessDefinitionPageItemRespVO;
import com.fhs.flow.controller.admin.definition.vo.process.BpmProcessDefinitionPageReqVO;
import com.fhs.flow.controller.admin.definition.vo.process.BpmProcessDefinitionRespVO;
import com.fhs.flow.service.definition.BpmProcessDefinitionService;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.fhs.flow.comon.pojo.CommonResult.success;


@Api(tags = "管理后台 - 流程定义")
@ApiGroup(group ="bpm")
@RestController
@RequestMapping("/ms/process-definition")
@Validated
public class BpmProcessDefinitionController {

    @Resource
    private BpmProcessDefinitionService bpmDefinitionService;

    @PostMapping("/page")
    @ApiOperation(value = "获得流程定义分页")
    @SaCheckRole("process-definition:see")
    public FhsPager<BpmProcessDefinitionPageItemRespVO> getProcessDefinitionPage(
            @RequestBody QueryFilter<?> queryFilter) {
        return bpmDefinitionService.getProcessDefinitionPage(ClassUtils.convert2Clz(queryFilter.queryFieldsMap(), BpmProcessDefinitionPageReqVO.class)).toFhsPager();
    }

    @GetMapping ("/list")
    @ApiOperation(value = "获得流程定义列表")
    @SaCheckRole("process-definition:see")
    public List<BpmProcessDefinitionRespVO> getProcessDefinitionList(
            BpmProcessDefinitionListReqVO listReqVO) {
        return bpmDefinitionService.getProcessDefinitionList(listReqVO);
    }

    @GetMapping ("/get-bpmn-xml")
    @ApiOperation(value = "获得流程定义的 BPMN XML")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024")
    @SaCheckRole("process-definition:see")
    public HttpResult<String> getProcessDefinitionBpmnXML(@RequestParam("id") String id) {
        String bpmnXML = bpmDefinitionService.getProcessDefinitionBpmnXML(id);
        return success(bpmnXML).toHttpResult();
    }
}
