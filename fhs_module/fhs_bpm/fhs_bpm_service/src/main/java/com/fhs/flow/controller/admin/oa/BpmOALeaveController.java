package com.fhs.flow.controller.admin.oa;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.fhs.basics.context.UserContext;
import com.fhs.common.utils.ClassUtils;
import com.fhs.core.base.vo.FhsPager;
import com.fhs.core.base.vo.QueryFilter;
import com.fhs.core.result.HttpResult;
import com.fhs.flow.comon.pojo.PageResult;
import com.fhs.flow.controller.admin.oa.vo.BpmOALeaveCreateReqVO;
import com.fhs.flow.controller.admin.oa.vo.BpmOALeavePageReqVO;
import com.fhs.flow.controller.admin.oa.vo.BpmOALeaveRespVO;
import com.fhs.flow.convert.oa.BpmOALeaveConvert;
import com.fhs.flow.dal.dataobject.oa.BpmOALeavePO;
import com.fhs.flow.service.oa.BpmOALeaveService;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static com.fhs.flow.comon.pojo.CommonResult.success;

/**
 * OA 请假申请 Controller，用于演示自己存储数据，接入工作流的例子
 *
 * @author jason
 * @author 芋道源码
 */
@Api(tags = "管理后台 - OA 请假申请")
@ApiGroup(group ="bpm")
@RestController
@RequestMapping("/ms/oa-leave")
@Validated
public class BpmOALeaveController {

    @Resource
    private BpmOALeaveService leaveService;

    @PostMapping("/create")
    @SaCheckRole("oa-leave:add")
    @ApiOperation("创建请求申请")
    public HttpResult<Long> createLeave(@Valid @RequestBody BpmOALeaveCreateReqVO createReqVO) {
        return success(leaveService.createLeave(UserContext.getSessionuser().getUserId(), createReqVO)).toHttpResult();
    }

    @GetMapping("/get")
    @SaCheckRole("oa-leave:see")
    @ApiOperation("获得请假申请")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024")
    public HttpResult<BpmOALeaveRespVO> getLeave(@RequestParam("id") Long id) {
        BpmOALeavePO leave = leaveService.getLeave(id);
        return success(BpmOALeaveConvert.INSTANCE.convert(leave)).toHttpResult();
    }

    @PostMapping("/page")
    @SaCheckRole("oa-leave:see")
    @ApiOperation("获得请假申请分页")
    public FhsPager<BpmOALeaveRespVO> getLeavePage(@RequestBody QueryFilter<?> queryFilter) {
        PageResult<BpmOALeavePO> pageResult = leaveService.getLeavePage(UserContext.getSessionuser().getUserId(), ClassUtils.convert2Clz(queryFilter.queryFieldsMap(), BpmOALeavePageReqVO.class));
        FhsPager<BpmOALeaveRespVO> result = BpmOALeaveConvert.INSTANCE.convertPage(pageResult).toFhsPager();
        result.getRecords().forEach(x->{
            x.setCreateTime(LocalDateTime.ofInstant(x.getCreateTime().toInstant(ZoneOffset.UTC), ZoneId.systemDefault()));
        });
        return result;
    }

}
