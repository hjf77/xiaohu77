package com.fhs.basics.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fhs.basics.constant.BaseTransConstant;
import com.fhs.basics.po.UcenterMsOrganizationPO;
import com.fhs.basics.po.UcenterMsUserPO;
import com.fhs.basics.service.UcenterMsOrganizationService;
import com.fhs.basics.service.UcenterMsUserService;
import com.fhs.basics.vo.LeftMenuVO;
import com.fhs.basics.vo.UcenterMsOrganizationVO;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.bislogger.api.anno.LogMethod;
import com.fhs.bislogger.api.anno.LogNamespace;
import com.fhs.bislogger.constant.LoggerConstant;
import com.fhs.common.constant.Constant;
import com.fhs.common.tree.TreeNode;
import com.fhs.common.utils.*;
import com.fhs.core.base.pojo.pager.Pager;
import com.fhs.core.exception.ParamException;
import com.fhs.core.result.HttpResult;
import com.fhs.core.safe.repeat.anno.NotRepeat;
import com.fhs.core.valid.checker.ParamChecker;
import com.fhs.core.valid.group.Add;
import com.fhs.core.valid.group.Update;
import com.fhs.module.base.controller.ModelSuperController;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统用户管理controller
 *
 * @author jianbo.qin
 * @date 2020-05-18 16:55:05
 */
@RestController
@Api(tags = {"系统用户"})
@ApiGroup(group = "group_default")
@RequestMapping("ms/sysUser")
@LogNamespace(namespace = BaseTransConstant.USER_INFO, module = "用户管理")
public class UcenterMsUserController extends ModelSuperController<UcenterMsUserVO, UcenterMsUserPO> {


    @Autowired
    private UcenterMsUserService sysUserService;

    /**
     * 机构服务
     */
    @Autowired
    private UcenterMsOrganizationService sysOrganizationService;


    /**
     * 获取用户jsontree 用于easyui下拉tree数据源
     */
    @GetMapping("getUserTree")
    public void getUserTree() {
        super.outJsonp(JsonUtil.list2json(sysUserService.getUserOrgTreeList(super.getSessionuser().getGroupCode())));
    }

    /**
     * 获取用户List
     */
    @GetMapping("getUserList")
    public List<UcenterMsUserVO> getUserList() {
        UcenterMsUserVO ucenterMsUserVO = new UcenterMsUserVO();
        ucenterMsUserVO.setIsDelete(Constant.NO_DELETE);
        ucenterMsUserVO.setGroupCode(super.getSessionuser().getGroupCode());
        return sysUserService.findForList(ucenterMsUserVO);
    }

    /**
     * 根据用户id获取用户详情
     *
     * @param userId 用户id
     */
    @GetMapping("getUserById")
    @LogMethod
    public void getUserById(String userId) {
        super.outJsonp(JsonUtil.bean2json(sysUserService.selectById(userId)));
    }

    /**
     * 获取用户列表
     *
     * @param
     */
    @GetMapping("getUserByIdList")
    @LogMethod
    public void getUserByIdList(String userIds) {
        if (CheckUtils.isNullOrEmpty(userIds)) {
            super.outJsonp("[]");
            return;
        }
        List<UcenterMsUserVO> byIds = sysUserService.findByIds(Arrays.asList(userIds.split(",")));
        super.outJsonp(JsonUtil.list2json(byIds));
    }

    @NotRepeat
    @ResponseBody
    @PostMapping("/")
    @ApiOperation(value = "新增-vue专用")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_ADD, voParamIndex = 0)
    public HttpResult<Boolean> save(@RequestBody @Validated(Add.class) UcenterMsUserVO sysUser, HttpServletRequest request,
                                    HttpServletResponse response) {
        return addUser(sysUser);
    }

    /**
     * 添加平台用户
     * @param sysUser
     */
    @NotRepeat
    @SaCheckRole("sysUser:add")
    @PostMapping("addUser")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_ADD, voParamIndex = 0)
    public HttpResult addUser( UcenterMsUserVO sysUser) {
        // 添加用户信息
        boolean notExist = sysUserService.validataLoginName(sysUser);
        if (notExist) {
            UcenterMsUserVO loginSysUser = super.getSessionuser();
            sysUser.setUpdateTime(new Date());
            sysUser.setUpdateUser(loginSysUser.getUserId());
            if (StringUtils.isEmpty(sysUser.getUserId())) { //新增
                sysUser.setCreateTime(new Date());
                sysUser.setCreateUser(loginSysUser.getUserId());
                sysUser.setGroupCode(loginSysUser.getGroupCode());
                sysUser.setIsAdmin(Constant.INT_FALSE);
            }
            Map<String, Object> resultMap = sysUserService.addUser(sysUser);
            boolean retult = ConverterUtils.toBoolean(resultMap.get("retult"));
            if (retult) {
                String passWord = ConverterUtils.toString(resultMap.get("passWord"));
                UcenterMsUserVO mailUser = (UcenterMsUserVO) resultMap.get("sysUser");
                // 发送邮件
                sysUserService.sendMail(mailUser, passWord);
            }
            return HttpResult.success(retult);
        } else {
            throw new ParamException("用户名重复");
        }
    }

    /**
     * 获取密码
     *
     * @param request
     * @param response
     * @param sysUser
     */
    @PostMapping("readPass")
    public void readPass(HttpServletRequest request, HttpServletResponse response, UcenterMsUserVO sysUser) {
        String isSuccess = sysUserService.readPass(sysUser.getUserName());
        super.outWrite(isSuccess);
    }

    /**
     * jsonp接口.用于其他系统用户列表
     *
     * @param request
     */
    @GetMapping("findUsers")
    @LogMethod
    public void findUsersJsonp(HttpServletRequest request) {
        PageSizeInfo pageSizeInfo = super.getPageSizeInfo();
        UcenterMsUserPO queryParam = UcenterMsUserPO.builder().userName(request.getParameter("userName")).organizationId(request.getParameter("orgId")).build();
        List<UcenterMsUserVO> users = sysUserService.selectPage(queryParam,
                pageSizeInfo.getPageStart(), pageSizeInfo.getPageSize());
        super.outJsonp(new Pager<UcenterMsUserVO>(sysUserService.selectCount(queryParam), users).asJson());
    }

    @GetMapping("getUserByCompanyId")
    @ApiOperation("根据单位id获取单位下的用户集合")
    public List<UcenterMsUserVO> getUserByCompanyId(String companyId) {
        List<UcenterMsOrganizationVO> orgs = sysOrganizationService.selectListMP(new LambdaQueryWrapper<UcenterMsOrganizationPO>()
                .eq(UcenterMsOrganizationPO::getCompanyId, companyId));
        if (orgs.isEmpty()) {
            return new ArrayList<>();
        }
        List<UcenterMsUserVO> users = sysUserService.selectListMP(new LambdaQueryWrapper<UcenterMsUserPO>().in(UcenterMsUserPO::getOrganizationId, orgs.stream()
                .map(UcenterMsOrganizationVO::getId).collect(Collectors.toList())));
        return users;
    }


    /**
     * @param id
     * @param request
     * @return
     * @desc 根据id删除对象
     */
    @SaCheckRole("sysUser:del")
    @RequestMapping("/delSysUser")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_DEL, pkeyParamIndex = 0)
    public HttpResult<Boolean> delSysUser(@RequestParam("id") String id, HttpServletRequest request) {
        UcenterMsUserVO sysUser = sysUserService.selectById(id);
        if (sysUser.getIsAdmin() == sysUserService.SYS_USER_IS_ADMIN) {
            throw new ParamException("超级用户不可删除");
        }
        sysUserService.deleteSysUserById(id);
        return HttpResult.success(true);
    }

    @ResponseBody
    @PutMapping("/")
    @ApiOperation(value = "修改-vue专用")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE, voParamIndex = 0)
    public HttpResult<Boolean> updateForVue(@RequestBody @Validated(Update.class)UcenterMsUserVO sysUser, HttpServletRequest request,
                                            HttpServletResponse response) {
        return update(sysUser);
    }
    /**
     * 更新用户信息
     * @param sysUser
     */
    @SaCheckRole("sysUser:update")
    @RequestMapping("updateUser")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE, voParamIndex = 0)
    public HttpResult<Boolean> update(UcenterMsUserVO sysUser) {
        if ("defaultPass".equals(sysUser.getPassword())) {
            sysUser.setPassword(null);
        }
        sysUserService.updateUser(sysUser);
        return HttpResult.success(Boolean.TRUE);
    }


    /**
     * 根据用户查询菜单
     *
     * @param request
     * @param response
     */
    @RequestMapping("seachMenuByUser")
    public List<LeftMenuVO> seachMenuByUser(@RequestParam("menuType") String menuType, HttpServletRequest request, HttpServletResponse response) {
        UcenterMsUserVO user = super.getSessionuser();
        ParamChecker.isNotNull(user, "用户没有登录");
        return sysUserService.getMenu(user, menuType);
    }

    /**
     * 修改密码
     *
     * @param sysUser 前端用户信息
     */
    @PostMapping("updatePass")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE, voParamIndex = 2, desc = "修改个人密码")
    public void updatePass(HttpServletRequest request, HttpServletResponse response, UcenterMsUserVO sysUser) {
        UcenterMsUserVO user = super.getSessionuser();
        sysUser.setUserId(user.getUserId());
        boolean isSuccess = sysUserService.updatePass(sysUser);
        super.outToClient(isSuccess);
    }

    /**
     * 修改自己的个人信息
     *
     * @param formSysUser 前端参数
     */
    @PostMapping("updateOwnUserInfo")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE, voParamIndex = 2, desc = "修改个人信息")
    public void updateOwnUserInfo(HttpServletRequest request, HttpServletResponse response, UcenterMsUserVO formSysUser) {
        UcenterMsUserVO user = super.getSessionuser();
        formSysUser.setUserName(formSysUser.getUserName());
        //把密码设置为空不修改密码
        formSysUser.setPassword(null);
        formSysUser.setEmail(formSysUser.getEmail());
        formSysUser.setMobile(formSysUser.getMobile());
        formSysUser.setUserId(user.getUserId());
        super.outToClient(sysUserService.updateSelectiveById(formSysUser) > 0);

    }

    /**
     * 校验密码
     *
     * @param request
     * @param response
     */
    @PostMapping("validataPass")
    public void validataPass(HttpServletRequest request, HttpServletResponse response) {
        String param = request.getParameter("param");
        UcenterMsUserVO user = super.getSessionuser();
        UcenterMsUserVO sysUser = new UcenterMsUserVO();
        sysUser.setUserId(user.getUserId());
        sysUser.setOldPassword(param);
        boolean isSuccess = sysUserService.validataPass(sysUser);
        if (isSuccess) {
            super.outWrite("y");
        } else {
            super.outWrite("原密码错误");
        }

    }


    /**
     * @param request http请求
     * @param
     * @return 前端分页请求
     * @desc 后台用户分页
     */
    @SaCheckRole("sysUser:see")
    @GetMapping("/findPage/{organizationId}")
    @ResponseBody
    @LogMethod
    public Pager<UcenterMsUserVO> findPage(@PathVariable(value = "organizationId") String organizationId, HttpServletRequest request, UcenterMsUserVO sysUser) {
        if (!CheckUtils.isNullOrEmpty(organizationId)) sysUser.setOrganizationId(organizationId);
        PageSizeInfo pgeSizeInfo = getPageSizeInfo();
        List<UcenterMsUserVO> dataList = sysUserService.findForList(sysUser, pgeSizeInfo.getPageStart(), pgeSizeInfo.getPageSize());
        int count = sysUserService.findCountJpa(sysUser);
        return new Pager<UcenterMsUserVO>(count, dataList);
    }


    /**
     * 获取自己的个人信息
     *
     * @param request
     * @return
     */
    @GetMapping("getOwnUserInfo")
    @LogMethod
    public UcenterMsUserVO getOwnUserInfo(HttpServletRequest request) {
        return sysUserService.selectById(super.getSessionuser().getUserId());
    }

    @GetMapping("getUserByOrgAndPermission")
    @ApiOperation("根据单位id，namespace，和方法编码获取符合条件的人")
    public List<UcenterMsUserPO> getUserByOrgAndPermission(String companyId, String namespace, String permissonMethodCode) {
        return sysUserService.getUserByOrgAndPermission(companyId, namespace, permissonMethodCode);
    }

    @GetMapping("getUserCompanyTree")
    @ApiOperation("获取公司tree(带用户)")
    public List<TreeNode> getUserCompanyTree(QueryWrapper<UcenterMsUserPO> wrapper) {
        return sysUserService.getUserCompanyTree(new QueryWrapper<>());
    }
}
