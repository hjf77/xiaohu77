package com.fhs.flow.controller.admin.definition;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.fhs.common.utils.ClassUtils;
import com.fhs.core.base.vo.FhsPager;
import com.fhs.core.base.vo.QueryFilter;
import com.fhs.core.result.HttpResult;
import com.fhs.flow.comon.pojo.PageResult;
import com.fhs.flow.controller.admin.definition.vo.form.*;
import com.fhs.flow.convert.definition.BpmFormConvert;
import com.fhs.flow.dal.dataobject.definition.BpmFormPO;
import com.fhs.flow.service.definition.BpmFormService;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.fhs.flow.comon.pojo.CommonResult.success;


@Api(tags = "管理后台 - 动态表单")
@ApiGroup(group ="bpm")
@RestController
@RequestMapping("/ms/form")
@Validated
public class BpmFormController {

    @Resource
    private BpmFormService formService;

    @PostMapping("/create")
    @ApiOperation("创建动态表单")
    @SaCheckRole("form:add")
    public HttpResult<Long> createForm(@Valid @RequestBody BpmFormCreateReqVO createReqVO) {
        return success(formService.createForm(createReqVO)).toHttpResult();
    }

    @PutMapping("/update")
    @ApiOperation("更新动态表单")
    @SaCheckRole("form:update")
    public HttpResult<Boolean> updateForm(@Valid @RequestBody BpmFormUpdateReqVO updateReqVO) {
        formService.updateForm(updateReqVO);
        return success(true).toHttpResult();
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除动态表单")
    @ApiImplicitParam(name = "id", value = "编号", required = true)
    @SaCheckRole("form:del")
    public HttpResult<Boolean> deleteForm(@RequestParam("id") Long id) {
        formService.deleteForm(id);
        return success(true).toHttpResult();
    }

    @GetMapping("/get")
    @ApiOperation("获得动态表单")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024")
    @SaCheckRole("form:see")
    public HttpResult<BpmFormRespVO> getForm(@RequestParam("id") Long id) {
        BpmFormPO form = formService.getForm(id);
        return success(BpmFormConvert.INSTANCE.convert(form)).toHttpResult();
    }

    @GetMapping("/list-all-simple")
    @ApiOperation(value = "获得动态表单的精简列表", notes = "用于表单下拉框")
    public List<BpmFormSimpleRespVO> getSimpleForms() {
        List<BpmFormPO> list = formService.getFormList();
        return BpmFormConvert.INSTANCE.convertList2(list);
    }

    @PostMapping("/page")
    @ApiOperation("获得动态表单分页")
    @SaCheckRole("form:see")
    public FhsPager<BpmFormRespVO> getFormPage(@RequestBody QueryFilter<?> queryFilter) {
        PageResult<BpmFormPO> pageResult = formService.getFormPage(ClassUtils.convert2Clz(queryFilter.queryFieldsMap(),BpmFormPageReqVO.class));
        return BpmFormConvert.INSTANCE.convertPage(pageResult).toFhsPager();
    }

}
