package com.fhs.ucenter.api;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.beust.jcommander.ParameterException;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.common.utils.DateUtils;
import com.fhs.common.utils.StringUtil;
import com.fhs.core.db.DataSource;
import com.fhs.core.exception.ParamChecker;
import com.fhs.core.page.Pager;
import com.fhs.core.result.HttpResult;
import com.fhs.redis.service.RedisCacheService;
import com.fhs.ucenter.api.form.SysUserForm;
import com.fhs.ucenter.api.service.FeignSysUserApiService;
import com.fhs.ucenter.api.vo.SysUserVo;
import com.fhs.ucenter.bean.SysUser;
import com.fhs.ucenter.service.SysUserService;
import feign.RequestLine;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Filename: SysUserApiServiceCloud.java
 * @Description: 后台用户api接口
 * @Version: 1.0
 * @Author: qixiaobo
 * @Email: qxb@sxpartner.com
 * @History:<br>
 * 陕西小伙伴网络科技有限公司
 * Copyright (c) 2017 All Rights Reserved.
 *
 */
@RestController
@RequestMapping("api/sysUser")
@DataSource("pagex/ucenter")
public class SysUserApiServiceCloud implements FeignSysUserApiService {

    /**
     * 后台用户服务
     */
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisCacheService<String> redisCacheService;

    /**
     * 根据用户登录名查询用户
     * @param userLoginName 用户登录名m
     * @return 用户信息
     */
    @GetMapping("/getSysUserByLoginName")
    @Override
    public HttpResult<SysUserVo> getSysUserByName(@RequestParam("userLoginName") String userLoginName) {
        SysUser sysUser = sysUserService.selectBean(new SysUser().mk("userLoginName", userLoginName));
        if (CheckUtils.isNotEmpty(sysUser)){
            SysUserVo result = new SysUserVo();
            BeanUtils.copyProperties(sysUser,result);
            return HttpResult.success(result);
        }
        throw new ParameterException("用户名不存在:" + userLoginName);
    }


    /**
     * 根据用户登录名查询用户权限列表
     * @param userLoginName 用户登录名
     * @return 权限列表
     */
    @GetMapping("/selectMenuByUname")
    @Override
    public HttpResult<List<String>> selectMenuByUname(@RequestParam("userLoginName") String userLoginName) {
        List<String> list = new ArrayList<>();
        list = sysUserService.selectMenuByUname( sysUserService.selectBean(new SysUser().mk("userLoginName",userLoginName)));
        return HttpResult.success(list);
    }

    /**
     * @desc 获取后端用户信息
     * @param sysUserForm 后端用户的form对象
     * @return 处理结果
     */
    @PostMapping("/getSysUserList")
    @Override
    public HttpResult<Pager<SysUserVo>> getSysUserList(@RequestBody SysUserForm sysUserForm) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserForm, sysUser);
        List<SysUser> sysUsersList = this.sysUserService.findForList(sysUser, sysUserForm.getPageStart()-1, sysUserForm.getPageSize());
        if(sysUsersList.size() > 0) {
            List<SysUserVo> sysUserVoList = new ArrayList<>();
            sysUsersList.forEach(sysUserForEach -> {
                SysUserVo sysUserVo = new SysUserVo();
                //正则表达式，替换手机号中间4位
                sysUserForEach.setMobile(sysUserForEach.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
                BeanUtils.copyProperties(sysUserForEach, sysUserVo);
                sysUserVoList.add(sysUserVo);
            });
            int count = this.sysUserService.findCount(sysUser);
            return count > 0 ? HttpResult.success(new Pager<>(count,sysUserVoList)) : HttpResult.success(new Pager<SysUserVo>(0, null));
        }else {
            return HttpResult.success(new Pager<SysUserVo>(0, null));
        }
    }

    /**
     * 根据用户ID获取用户权限URL
     * @param userId 用户ID
     * @return 用户权限URL列表
     */
    @Override
    @GetMapping("/getPermissionUrlByUserId")
    public HttpResult<List<String>> getPermissionUrlByUserId(@RequestParam("userId") String userId) {
        SysUser sysUser = sysUserService.findBeanById(userId);
        if(sysUser == null)
        {
            return HttpResult.error(null, "没有此用户");
        } else {
            return HttpResult.success(sysUserService.getPermissionUrl(sysUser));
        }
    }

    /**
     * 获取用户的数据权限
     * @param userId  用户id
     * @return 数据权限配置
     */
    @Override
    @GetMapping("/getDataUserPermisstion")
    public HttpResult<Map<String,String>> getDataUserPermisstion(@RequestParam("userId")String userId)
    {
        ParamChecker.isNotNullOrEmpty(userId,"userId不能为空");
        return HttpResult.success(sysUserService.findUserDataPermissions(userId));
    }


    /**
     * @desc 根据userId获取用户信息
     * @param sysUserForm 后端用户form
     * @return 后端用户信息
     */
    @PostMapping("/getSysUserByUserId")
    @Override
    public HttpResult<SysUserVo> getSysUserByUserId(@RequestBody SysUserForm sysUserForm) {
        SysUserVo vo = new SysUserVo();
        if(!CheckUtils.isNullOrEmpty(sysUserForm) && !CheckUtils.isNullOrEmpty(sysUserForm.getUserId())) {
            SysUser sysUser = sysUserService.findSysUserById(sysUserForm.getUserId());
            BeanUtils.copyProperties(sysUser, vo);
        }
        return HttpResult.success(vo);
    }

    /**
     * 获取用户的数据权限
     * @param organizationId  组织id
     * @return 用户列表
     */
    @GetMapping("/getSysUserByOrganizationId")
    @Override
    public HttpResult<List<SysUserVo>> getSysUserByOrganizationId(@RequestParam("organizationId")String organizationId)
    {
        ParamChecker.isNotNullOrEmpty(organizationId,"userId不能为空");
        List<SysUserVo> voList = new ArrayList<>();
        List<SysUser> sysUserList=sysUserService.findForList(SysUser.builder().organizationId(organizationId).build());
        BeanUtils.copyProperties(sysUserList, voList);
        return HttpResult.success(voList);
    }

    /**
     * 添加后管用户
     */
    @PostMapping("/addUser")
    @Override
    public HttpResult addUser(@RequestBody SysUserVo sysUserVo) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserVo,sysUser);
        //是修改但不存在该用户
        if(CheckUtils.isNotEmpty(sysUser.getUserId())){
            if(sysUserService.validataLoginName(SysUser.builder().userLoginName(sysUserVo.getUserLoginName()).build())){
                sysUser.setUserId(null);
            }else{
                sysUser.setUserId(sysUserService.selectBean(SysUser.builder().userLoginName(sysUserVo.getUserLoginName()).build()).getUserId());
            }
        }
        // 需要添加进行设置
        if(StringUtil.isEmpty(sysUser.getUserId())){
            sysUser.setCreateTime(DateUtils.formartDate(new Date(), DateUtils.DATETIME_PATTERN));
            sysUser.setCreateUser(sysUserVo.getLoginUserId());
            sysUser.setGroupCode(sysUserVo.getLoginUserGroupCode());
        }
        sysUser.setUpdateTime(DateUtils.formartDate(new Date(), DateUtils.DATETIME_PATTERN));
        sysUser.setUpdateUser(sysUserVo.getLoginUserId());
        Map<String, Object> resultMap = sysUserService.addUser(sysUser);
        HttpResult<Boolean> retult = HttpResult.success(ConverterUtils.toBoolean(resultMap.get("retult")));
        return retult;
    }


    /**
     * 删除后管用户
     */
    @PostMapping("/delUser")
    @Override
    public HttpResult delUser(@RequestParam("loginName") String loginName) {
        ParamChecker.isNotNullOrEmpty(loginName,"登录用户名不能为空");
        SysUser sysUser = sysUserService.findBean(SysUser.builder().userLoginName(loginName).build());
        if(CheckUtils.isNotEmpty(sysUser)){
            // 删除用户同时删除用户角色配置
            sysUserService.deleteSysUserById(sysUser.getUserId());
        }
        return HttpResult.success();
    }

    /**
     *@Description:  根据用户名修改密码
     * @Param: [loginName, password]
     * @Return: com.fhs.core.result.HttpResult
     */

    @GetMapping("updatePassWord")
    @Override
    public HttpResult updatePassWord(@RequestParam("loginName") String loginName,@RequestParam("password") String password) {
        ParamChecker.isNotNullOrEmpty(loginName,"用户名不能为空");
        ParamChecker.isNotNullOrEmpty(password,"密码不能为空");
        SysUser sysUser = sysUserService.selectBean(new SysUser().mk("userLoginName", loginName));
        if(CheckUtils.isNotEmpty(sysUser)){
            sysUserService.updateSelectiveById(SysUser.builder().userId(sysUser.getUserId()).password(password).build());
            return HttpResult.success("");
        }
        return HttpResult.error("","用户名不存在:" + loginName);
    }
}
