package com.fhs.flow.controller.admin.definition;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.fhs.common.utils.ClassUtils;
import com.fhs.core.base.vo.FhsPager;
import com.fhs.core.base.vo.QueryFilter;
import com.fhs.core.result.HttpResult;
import com.fhs.flow.comon.pojo.PageResult;
import com.fhs.flow.controller.admin.definition.vo.group.BpmUserGroupCreateReqVO;
import com.fhs.flow.controller.admin.definition.vo.group.BpmUserGroupPageReqVO;
import com.fhs.flow.controller.admin.definition.vo.group.BpmUserGroupRespVO;
import com.fhs.flow.controller.admin.definition.vo.group.BpmUserGroupUpdateReqVO;
import com.fhs.flow.convert.definition.BpmUserGroupConvert;
import com.fhs.flow.dal.dataobject.definition.BpmUserGroupPO;
import com.fhs.flow.enums.CommonStatusEnum;
import com.fhs.flow.service.definition.BpmUserGroupService;
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


@Api(tags = "管理后台 - 用户组")
@ApiGroup(group ="bpm")
@RestController
@RequestMapping("/ms/user-group")
@Validated
public class BpmUserGroupController {

    @Resource
    private BpmUserGroupService userGroupService;

    @PostMapping("/create")
    @ApiOperation("创建用户组")
    @SaCheckRole("user-group:add")
    public HttpResult<Long> createUserGroup(@Valid @RequestBody BpmUserGroupCreateReqVO createReqVO) {
        return success(userGroupService.createUserGroup(createReqVO)).toHttpResult();
    }

    @PutMapping("/update")
    @ApiOperation("更新用户组")
    @SaCheckRole("user-group:udpate")
    public HttpResult<Boolean> updateUserGroup(@Valid @RequestBody BpmUserGroupUpdateReqVO updateReqVO) {
        userGroupService.updateUserGroup(updateReqVO);
        return success(true).toHttpResult();
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除用户组")
    @ApiImplicitParam(name = "id", value = "编号", required = true)
    @SaCheckRole("user-group:delete")
    public HttpResult<Boolean> deleteUserGroup(@RequestParam("id") Long id) {
        userGroupService.deleteUserGroup(id);
        return success(true).toHttpResult();
    }

    @GetMapping("/get")
    @ApiOperation("获得用户组")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024")
    @SaCheckRole("user-group:see")
    public HttpResult<BpmUserGroupRespVO> getUserGroup(@RequestParam("id") Long id) {
        BpmUserGroupPO userGroup = userGroupService.getUserGroup(id);
        return success(BpmUserGroupConvert.INSTANCE.convert(userGroup)).toHttpResult();
    }

    @GetMapping("/page")
    @ApiOperation("获得用户组分页")
    @SaCheckRole("user-group:see")
    public FhsPager<BpmUserGroupRespVO> getUserGroupPage(@RequestBody QueryFilter<?> queryFilter) {
        PageResult<BpmUserGroupPO> pageResult = userGroupService.getUserGroupPage(ClassUtils.convert2Clz(queryFilter.queryFieldsMap(),BpmUserGroupPageReqVO.class));
        return BpmUserGroupConvert.INSTANCE.convertPage(pageResult).toFhsPager();
    }

    @GetMapping("/list-all-simple")
    @ApiOperation(value = "获取用户组精简信息列表", notes = "只包含被开启的用户组，主要用于前端的下拉选项")
    public List<BpmUserGroupRespVO> getSimpleUserGroups() {
        // 获用户门列表，只要开启状态的
        List<BpmUserGroupPO> list = userGroupService.getUserGroupListByStatus(CommonStatusEnum.ENABLE.getStatus());
        // 排序后，返回给前端
        return BpmUserGroupConvert.INSTANCE.convertList2(list);
    }

}
