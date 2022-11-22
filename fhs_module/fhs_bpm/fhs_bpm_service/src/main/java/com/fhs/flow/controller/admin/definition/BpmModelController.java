package com.fhs.flow.controller.admin.definition;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.fhs.flow.comon.pojo.CommonResult;
import com.fhs.flow.comon.pojo.PageResult;
import com.fhs.flow.comon.utils.IoUtils;
import com.fhs.flow.controller.admin.definition.vo.model.*;
import com.fhs.flow.convert.definition.BpmModelConvert;
import com.fhs.flow.service.definition.BpmModelService;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;

import static com.fhs.flow.comon.pojo.CommonResult.success;


@Api(tags = "管理后台 - 流程模型")
@ApiGroup(group ="bpm")
@RestController
@RequestMapping("/ms/model")
@Validated
public class BpmModelController {

    @Resource
    private BpmModelService modelService;

    @GetMapping("/page")
    @ApiOperation(value = "获得模型分页")
    public CommonResult<PageResult<BpmModelPageItemRespVO>> getModelPage(BpmModelPageReqVO pageVO) {
        return success(modelService.getModelPage(pageVO));
    }

    @GetMapping("/get")
    @ApiOperation("获得模型")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024")
    @SaCheckRole("model:see")
    public CommonResult<BpmModelRespVO> getModel(@RequestParam("id") String id) {
        BpmModelRespVO model = modelService.getModel(id);
        return success(model);
    }

    @PostMapping("/create")
    @ApiOperation(value = "新建模型")
    @SaCheckRole("model:add")
    public CommonResult<String> createModel(@Valid @RequestBody BpmModelCreateReqVO createRetVO) {
        return success(modelService.createModel(createRetVO, null));
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改模型")
    @SaCheckRole("model:update")
    public CommonResult<Boolean> updateModel(@Valid @RequestBody BpmModelUpdateReqVO modelVO) {
        modelService.updateModel(modelVO);
        return success(true);
    }

    @PostMapping("/import")
    @ApiOperation(value = "导入模型")
    @SaCheckRole("model:add")
    public CommonResult<String> importModel(@Valid BpmModeImportReqVO importReqVO) throws IOException {
        BpmModelCreateReqVO createReqVO = BpmModelConvert.INSTANCE.convert(importReqVO);
        // 读取文件
        String bpmnXml = IoUtils.readUtf8(importReqVO.getBpmnFile().getInputStream(), false);
        return success(modelService.createModel(createReqVO, bpmnXml));
    }

    @PostMapping("/deploy")
    @ApiOperation(value = "部署模型")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024")
    @SaCheckRole("model:deploy")
    public CommonResult<Boolean> deployModel(@RequestParam("id") String id) {
        modelService.deployModel(id);
        return success(true);
    }

    @PutMapping("/update-state")
    @ApiOperation(value = "修改模型的状态", notes = "实际更新的部署的流程定义的状态")
    @SaCheckRole("model:update")
    public CommonResult<Boolean> updateModelState(@Valid @RequestBody BpmModelUpdateStateReqVO reqVO) {
        modelService.updateModelState(reqVO.getId(), reqVO.getState());
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除模型")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024")
    @SaCheckRole("model:del")
    public CommonResult<Boolean> deleteModel(@RequestParam("id") String id) {
        modelService.deleteModel(id);
        return success(true);
    }
}
