package com.fhs.basics.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fhs.basics.api.anno.LogMethod;
import com.fhs.basics.api.anno.LogNamespace;
import com.fhs.basics.constant.LoggerConstant;
import com.fhs.basics.po.UcenterAppUserSetPO;
import com.fhs.basics.po.UcenterMsOrganizationPO;
import com.fhs.basics.po.UcenterMsUserPO;
import com.fhs.basics.service.UcenterAppUserSetService;
import com.fhs.basics.service.UcenterMsOrganizationService;
import com.fhs.basics.service.UcenterMsUserService;
import com.fhs.basics.vo.SysUserOrgVO;
import com.fhs.basics.vo.UcenterAppUserSetVO;
import com.fhs.basics.vo.UcenterMsOrganizationVO;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.common.constant.Constant;
import com.fhs.common.tree.TreeNode;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.common.utils.StringUtils;
import com.fhs.core.base.valid.group.Add;
import com.fhs.core.base.valid.group.Update;
import com.fhs.core.cache.service.RedisCacheService;
import com.fhs.core.exception.ParamException;
import com.fhs.core.result.HttpResult;
import com.fhs.core.safe.repeat.anno.NotRepeat;
import com.fhs.core.valid.checker.ParamChecker;
import com.fhs.module.base.controller.ModelSuperController;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
@LogNamespace(namespace = "sysUser", module = "用户管理")
public class UcenterMsUserController extends ModelSuperController<UcenterMsUserVO, UcenterMsUserPO, Long> {

    @Autowired
    private UcenterMsUserService sysUserService;
    @Autowired
    private UcenterMsOrganizationService sysOrganizationService;
    @Autowired
    private UcenterAppUserSetService ucenterAppUserSetService;
    @Autowired
    private RedisCacheService redisCacheService;

    /**
     * 短信验证码
     */
    private static final String SMS_CODE_KEY = "sms:code:";

    /**
     * 获取用户jsontree 用于easyui下拉tree数据源
     */
    @GetMapping("getUserTree")
    @ApiOperation("获取用户组织tree")
    public List<SysUserOrgVO> getUserTree() {
        return sysUserService.getUserOrgTreeList(super.getSessionuser().getGroupCode());
    }

    @Override
    @NotRepeat
    @PostMapping("/")
    @ApiOperation(value = "新增-vue专用")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_ADD, voParamIndex = 0)
    public HttpResult<Boolean> add(@RequestBody @Validated(Add.class) UcenterMsUserVO sysUser) {

        // 添加用户信息
        boolean notExist = sysUserService.validataLoginName(sysUser);
        if (notExist) {
            UcenterMsUserVO loginSysUser = super.getSessionuser();
            sysUser.setUpdateTime(new Date());
            sysUser.setUpdateUser(loginSysUser.getUserId());
            //新增
            if (sysUser.getUserId() == null) {
                sysUser.setCreateTime(new Date());
                sysUser.setCreateUser(loginSysUser.getUserId());
                sysUser.setGroupCode(loginSysUser.getGroupCode());
                sysUser.setIsAdmin(Constant.INT_FALSE);
                sysUser.setIsAppUser(Constant.INT_FALSE);
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
     * @param sysUser
     */
    @PostMapping("readPass")
    public void readPass(UcenterMsUserVO sysUser) {
        String isSuccess = sysUserService.readPass(sysUser.getUserName());
        super.outWrite(isSuccess);
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


    @Override
    @PutMapping("/")
    @ApiOperation(value = "修改-vue专用")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE, voParamIndex = 0)
    public HttpResult<Boolean> update(@RequestBody @Validated(Update.class) UcenterMsUserVO sysUser) {
        if ("defaultPass".equals(sysUser.getPassword())) {
            sysUser.setPassword(null);
        }
        sysUserService.updateUser(sysUser);
        return HttpResult.success(Boolean.TRUE);
    }


    /**
     * 修改密码
     *
     * @param sysUser 前端用户信息
     */
    @PutMapping("updatePass")
    @ApiOperation("修改登录人密码")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE, voParamIndex = 2, desc = "修改个人密码")
    public HttpResult updatePass(@RequestBody UcenterMsUserVO sysUser) {
        UcenterMsUserVO user = super.getSessionuser();
        sysUser.setUserId(user.getUserId());
        sysUserService.updatePass(sysUser);
        return HttpResult.success();
    }

    /**
     * 修改自己的个人信息
     *
     * @param formSysUser 前端参数
     */
    @PostMapping("updateOwnUserInfo")
    @ApiOperation("修改登录人个人信息")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE, voParamIndex = 2, desc = "修改个人信息")
    public HttpResult updateOwnUserInfo(@RequestBody UcenterMsUserVO formSysUser) {
        UcenterMsUserVO user = super.getSessionuser();
        formSysUser.setUserName(formSysUser.getUserName());
        //把密码设置为空不修改密码
        formSysUser.setPassword(null);
        formSysUser.setEmail(formSysUser.getEmail());
        formSysUser.setMobile(formSysUser.getMobile());
        formSysUser.setUserId(user.getUserId());
        sysUserService.updateSelectiveById(formSysUser);
        return HttpResult.success();
    }

    /**
     * 校验密码
     *
     * @param request
     */
    @PostMapping("validataPass")
    public HttpResult<Boolean> validataPass(HttpServletRequest request) {
        String param = request.getParameter("param");
        UcenterMsUserVO user = super.getSessionuser();
        UcenterMsUserVO sysUser = new UcenterMsUserVO();
        sysUser.setUserId(user.getUserId());
        sysUser.setOldPassword(param);
        boolean isSuccess = sysUserService.validataPass(sysUser);
        return isSuccess ? HttpResult.success(true) : HttpResult.error(false);
    }

    /**
     * 校验密码
     *
     * @param ucenterMsUserPO
     */
    @PostMapping("appValidataPass")
    public HttpResult<Boolean> appValidataPass(@RequestBody UcenterMsUserPO ucenterMsUserPO) {
        ucenterMsUserPO.setUserId(this.getSessionuser().getUserId());
        boolean isSuccess = sysUserService.appValidataPass(ucenterMsUserPO);
        if (isSuccess) {
            return HttpResult.success(true);
        }
        throw new ParamException("密码输入错误");
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

    /**
     * 获取自己的个人信息
     *
     * @param request
     * @return
     */
    @GetMapping("getAppUserInfo")
    @LogMethod
    public HttpResult<Map<String, Object>> getAppUserInfo(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        UcenterMsUserVO sysUser = sysUserService.selectById(super.getSessionuser().getUserId());
        result.put("userInfo", sysUser);
        //获取APP用户设置信息
        if (sysUser.getIsAppUser().equals(Constant.INT_TRUE)) {
            UcenterAppUserSetVO ucenterAppUserSetVO = ucenterAppUserSetService.selectBean(UcenterAppUserSetPO.builder().userId(sysUser.getUserId()).build());
            result.put("userSetInfo", ucenterAppUserSetVO);
        }
        return HttpResult.success(result);
    }


    /**
     * 用户信息设置
     *
     * @param ucenterAppUserSetVO
     * @return
     */
    @PostMapping("setAppUser")
    @LogMethod
    public HttpResult<Boolean> setAppUser(@RequestBody UcenterAppUserSetVO ucenterAppUserSetVO) {
        Long userId = super.getSessionuser().getUserId();
        UcenterMsUserPO ucenterMsUserPO = new UcenterMsUserPO();
        ucenterMsUserPO.setUserId(userId);
        if (!StringUtils.isEmpty(ucenterAppUserSetVO.getHeader())) {
            ucenterMsUserPO.setHeader(ucenterAppUserSetVO.getHeader());
        }
        if (!StringUtils.isEmpty(ucenterAppUserSetVO.getUserName())) {
            ucenterMsUserPO.setUserName(ucenterAppUserSetVO.getUserName());
        }
        ucenterMsUserPO.setUpdateTime(new Date());
        sysUserService.updateById(ucenterMsUserPO);
        //用户信息设置
        UcenterAppUserSetPO ucenterAppUserSetPO = (UcenterAppUserSetPO) ucenterAppUserSetVO;
        ucenterAppUserSetPO.setUserId(userId);
        UcenterAppUserSetVO userSetVO = ucenterAppUserSetService.selectBean(UcenterAppUserSetPO.builder().userId(userId).build());
        if (userSetVO != null) {
            ucenterAppUserSetPO.setId(userSetVO.getId());
            ucenterAppUserSetService.updateById(ucenterAppUserSetPO);
        } else {
            ucenterAppUserSetService.insert(ucenterAppUserSetPO);
        }
        return HttpResult.success(true);
    }

    /**
     * 修改手机号邮箱
     *
     * @param ucenterMsUserVO
     * @return
     */
    @PostMapping("setUserMobileOrEmail")
    @LogMethod
    public HttpResult<Boolean> setUserMobileOrEmail(@RequestBody UcenterMsUserVO ucenterMsUserVO) {
        //判断短信验证码
        ParamChecker.isNotNull(ucenterMsUserVO.getSmsCode(), "验证码不能为空");

        Long userId = super.getSessionuser().getUserId();
        UcenterMsUserPO ucenterMsUserPO = new UcenterMsUserPO();
        ucenterMsUserPO.setUserId(userId);
        if (!StringUtils.isEmpty(ucenterMsUserVO.getMobile())) {
            // 判断短信验证码是否正确过期
            String smsCode = redisCacheService.get(SMS_CODE_KEY + ucenterMsUserVO.getUuid() + ucenterMsUserVO.getMobile()).toString();
            if (StringUtils.isEmpty(smsCode)) {
                throw new ParamException("验证码已过期，请重新发送");
            }
            if (!smsCode.equals(ucenterMsUserVO.getSmsCode())) {
                throw new ParamException("验证码不正确");
            }
            Long mobileCount = sysUserService.selectCountMP(new LambdaQueryWrapper<UcenterMsUserPO>()
                    .eq(UcenterMsUserPO::getMobile, ucenterMsUserVO.getMobile())
                    .ne(UcenterMsUserPO::getUserId, userId));
            if (mobileCount > 0L) {
                throw new ParamException("该手机号已被使用！");
            }
            ucenterMsUserPO.setMobile(ucenterMsUserVO.getMobile());
        }
        if (!StringUtils.isEmpty(ucenterMsUserVO.getEmail())) {
            // 判断短信验证码是否正确过期
            String smsCode = redisCacheService.get(SMS_CODE_KEY + ucenterMsUserVO.getUuid() + ucenterMsUserVO.getEmail()).toString();
            if (StringUtils.isEmpty(smsCode)) {
                throw new ParamException("验证码已过期，请重新发送");
            }
            if (!smsCode.equals(ucenterMsUserVO.getSmsCode())) {
                throw new ParamException("验证码不正确");
            }
            Long emailCount = sysUserService.selectCountMP(new LambdaQueryWrapper<UcenterMsUserPO>()
                    .eq(UcenterMsUserPO::getEmail, ucenterMsUserVO.getEmail())
                    .ne(UcenterMsUserPO::getUserId, userId));
            if (emailCount > 0L) {
                throw new ParamException("该邮箱已被使用！");
            }
            ucenterMsUserPO.setEmail(ucenterMsUserVO.getEmail());
        }
        ucenterMsUserPO.setUpdateTime(new Date());
        sysUserService.updateById(ucenterMsUserPO);
        return HttpResult.success(true);
    }


    /**
     * 根据单位id，namespace，和方法编码获取符合条件的人
     *
     * @param companyId
     * @param namespace
     * @param permissonMethodCode
     * @return
     */
    @GetMapping("getUserByOrgAndPermission")
    @ApiOperation("根据单位id，namespace，和方法编码获取符合条件的人")
    public List<UcenterMsUserPO> getUserByOrgAndPermission(String companyId, String namespace, String permissonMethodCode) {
        return sysUserService.getUserByOrgAndPermission(companyId, namespace, permissonMethodCode);
    }

    /**
     * 获取公司tree(带用户)
     *
     * @param wrapper
     * @return
     */
    @GetMapping("getUserCompanyTree")
    @ApiOperation("获取公司tree(带用户)")
    public List<TreeNode> getUserCompanyTree(QueryWrapper<UcenterMsUserPO> wrapper) {
        return sysUserService.getUserCompanyTree(new QueryWrapper<>());
    }
}
