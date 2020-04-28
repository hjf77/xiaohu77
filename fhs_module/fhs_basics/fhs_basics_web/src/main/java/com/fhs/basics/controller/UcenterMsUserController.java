package com.fhs.basics.controller;

import com.fhs.basics.constant.BaseTransConstant;
import com.fhs.basics.dox.UcenterMsUserDO;
import com.fhs.basics.service.UcenterMsUserService;
import com.fhs.basics.vo.LeftMenuVO;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.bislogger.api.anno.LogMethod;
import com.fhs.bislogger.api.anno.LogNamespace;
import com.fhs.bislogger.constant.LoggerConstant;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.*;
import com.fhs.core.base.pojo.pager.Pager;
import com.fhs.core.exception.ParamException;
import com.fhs.core.result.HttpResult;
import com.fhs.core.safe.repeat.anno.NotRepeat;
import com.fhs.core.valid.checker.ParamChecker;
import com.fhs.logger.anno.LogDesc;
import com.fhs.module.base.controller.ModelSuperController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统用户管理Action
 *
 * @author jianbo.qin
 */
@RestController
@LogNamespace(namespace = BaseTransConstant.USER_INFO,module = "用户管理")
@RequestMapping("ms/sysUser")
public class UcenterMsUserController extends ModelSuperController<UcenterMsUserVO, UcenterMsUserDO> {


    @Autowired
    private UcenterMsUserService sysUserService;

    /**
     * 获取用户jsontree 用于easyui下拉tree数据源
     */
    @RequestMapping("getUserTree")
    public void  getUserTree() {
        super.outJsonp(JsonUtils.list2json(sysUserService.getUserOrgTreeList(super.getSessionuser().getGroupCode())));
    }

    /**
     * 根据用户id获取用户详情
     * @param userId 用户id
     */
    @RequestMapping("getUserById")
    @LogMethod
    public void getUserById(String userId){
        super.outJsonp(JsonUtils.bean2json(sysUserService.selectById(userId)));
    }

    /**
     * 获取用户列表
     * @param
     */
    @RequestMapping("getUserByIdList")
    @LogMethod
    public void getUserByIdList(String userIds){
        if(CheckUtils.isNullOrEmpty(userIds)){
            super.outJsonp("[]");
            return;
        }
        List<UcenterMsUserVO> byIds = sysUserService.findByIds(Arrays.asList(userIds.split(",")));
        super.outJsonp(JsonUtils.list2json(byIds));
    }

    /**
     * 添加平台用户
     *
     * @param request
     * @param response
     * @param sysUser
     * @param attr
     */
    @NotRepeat
    @RequiresPermissions("sysUser:add")
    @RequestMapping("addUser")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_ADD,voParamIndex = 2)
    public HttpResult addUser(HttpServletRequest request, HttpServletResponse response, UcenterMsUserVO sysUser,
                              RedirectAttributes attr) {
        // 添加用户信息
        boolean notExist = sysUserService.validataLoginName(sysUser);
        if (notExist) {
            UcenterMsUserVO loginSysUser = super.getSessionuser();
            sysUser.setUpdateTime(new Date());
            sysUser.setUpdateUser(loginSysUser.getUserId());
            if (StringUtil.isEmpty(sysUser.getUserId())) { //新增
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
    @RequestMapping("readPass")
    public void readPass(HttpServletRequest request, HttpServletResponse response, UcenterMsUserVO sysUser) {
        String isSuccess = sysUserService.readPass(sysUser.getUserName());
        super.outWrite(isSuccess);
    }

    /**
     * jsonp接口.用于其他系统用户列表
     * @param request
     */
    @RequestMapping("findUsers")
    @LogMethod
    public void findUsersJsonp(HttpServletRequest request){
        PageSizeInfo pageSizeInfo = super.getPageSizeInfo();
        UcenterMsUserDO queryParam = UcenterMsUserDO.builder().userName(request.getParameter("userName")).organizationId(request.getParameter("orgId")).build();
        List<UcenterMsUserVO> users = sysUserService.selectPage(queryParam,
                pageSizeInfo.getPageStart(),pageSizeInfo.getPageSize());
        super.outJsonp(new Pager<UcenterMsUserVO>(sysUserService.selectCount(queryParam),users).asJson());
    }


    /**
     * @param id
     * @param request
     * @return
     * @desc 根据id删除对象
     */
    @RequiresPermissions("sysUser:del")
    @RequestMapping("/delSysUser")
    @LogMethod(type=LoggerConstant.METHOD_TYPE_DEL,pkeyParamIndex = 0)
    public HttpResult<Boolean> delSysUser(@RequestParam("id") String id, HttpServletRequest request) {
        UcenterMsUserVO sysUser = sysUserService.selectById(id);
        if (sysUser.getIsAdmin() == sysUserService.SYS_USER_IS_ADMIN) {
            throw new ParamException("超级用户不可删除");
        }
        sysUserService.deleteSysUserById(id);
        return HttpResult.success(true);
    }


    /**
     * 更新用户信息
     *
     * @param request
     * @param response
     * @param sysUser
     */
    @RequiresPermissions("sysUser:update")
    @RequestMapping("updateUser")
    @LogMethod(type=LoggerConstant.METHOD_TYPE_UPATE,voParamIndex = 2)
    public HttpResult<Boolean> update(HttpServletRequest request, HttpServletResponse response, UcenterMsUserVO sysUser) {
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
    @RequestMapping("updatePass")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE,voParamIndex = 2,desc = "修改个人密码")
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
    @RequestMapping("updateOwnUserInfo")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE,voParamIndex = 2,desc = "修改个人信息")
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
    @RequestMapping("validataPass")
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
    @RequiresPermissions("sysUser:see")
    @RequestMapping("/findPage/{organizationId}")
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
    @RequestMapping("getOwnUserInfo")
    @ResponseBody
    public UcenterMsUserVO getOwnUserInfo(HttpServletRequest request) {
        return sysUserService.selectById(super.getSessionuser().getUserId());
    }

}
