package com.fhs.basics.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.fhs.basics.po.UcenterAppUserSetPO;
import com.fhs.basics.po.UcenterMsUserPO;
import com.fhs.basics.service.UcenterAppUserSetService;
import com.fhs.basics.service.UcenterMsRoleService;
import com.fhs.basics.service.UcenterMsUserService;
import com.fhs.basics.vo.*;
import com.fhs.basics.constant.LoggerConstant;
import com.fhs.basics.service.LogLoginService;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.*;
import com.fhs.core.base.controller.BaseController;
import com.fhs.core.cache.service.RedisCacheService;
import com.fhs.core.exception.ParamException;
import com.fhs.core.result.HttpResult;
import com.fhs.core.valid.checker.ParamChecker;
import com.fhs.core.logger.Logger;
import com.fhs.basics.context.UserContext;
import com.fhs.module.base.auth.StpInterfaceImpl;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 非分布式运行时候的登录登出
 *
 * @author user
 * @date 2020-05-18 16:49:49
 */
@RestController
@RequestMapping("/")
@Api(tags = "后台用户登录登出")
@ApiGroup(group = "group_default")
public class MsLoginController extends BaseController {

    private static Logger LOGGER = Logger.getLogger(MsLoginController.class);

    private static final String USER_KEY = "auth:user:";

    /**
     * 用户锁定key
     */
    private static final String USER_LOCK_KEY = "user:lock:";

    /**
     * 用户输入错误密码次数key
     */
    private static final String USER_ERROR_TIME_KEY = "user:errortime:";

    /**
     * 验证码
     */
    private static final String LOGIN_VCODE_KEY = "login:vcode:";

    /**
     * 短信验证码
     */
    private static final String SMS_CODE_KEY = "sms:code:";

    @Value("${sa-token.timeout:3600}")
    private Integer sesstionTimeout;

    @Value("${fhs.vue.is-verification:true}")
    private Boolean isVerification;

    @Value("${fhs.smscode.timeout:300}")
    private Integer smscodeTimeout;

    /**
     * redis 缓存服务
     */
    @Autowired
    private RedisCacheService redisCacheService;

    /**
     * 后台用户服务
     */
    @Autowired
    private UcenterMsUserService sysUserService;


    @Autowired
    private UcenterMsRoleService roleService;

    @Autowired
    private LogLoginService logLoginService;

    /**
     * 登录地址
     */
    @Value("${fhs.login.url:http://default.fhs-opensource.com}")
    private String shrioLoginUrl;

    /**
     * 输入多少次密码锁定
     */
    @Value("${fhs.login.error-pass-lock-times:5}")
    private int lockTimes;

    @Value("${fhs.login.user-lock-seconds:300}")
    private int userLockSeconds;

    @Autowired
    private StpInterfaceImpl stpInterface;

    @Autowired
    private UcenterAppUserSetService ucenterAppUserSetService;


    /**
     * 判断用户是否锁定
     *
     * @param userName 用户登录名
     */
    private void checkUserNameIsLock(String userName) {
        String key = USER_LOCK_KEY + userName;
        if (redisCacheService.exists(key)) {
            throw new ParamException("用户被锁定，请您" + redisCacheService.getExpire(key) + "秒后重试");
        }
    }

    /**
     * 添加用户输入密码错误次数
     *
     * @param userName 用户名
     */
    private void addErrorPassTimes(String userName) {
        String key = USER_ERROR_TIME_KEY + userName;
        if (redisCacheService.exists(key)) {
            int errorTimes = ConverterUtils.toInt(redisCacheService.get(key)) + 1;
            if (errorTimes >= lockTimes) {
                //锁定
                redisCacheService.put(USER_LOCK_KEY + userName, "");
                redisCacheService.expire(USER_LOCK_KEY + userName, userLockSeconds);
                throw new ParamException("密码输入次数过多，账号已被锁定");
            }
            //存在的话+1
            redisCacheService.put(key, "" + errorTimes);
        } else {
            redisCacheService.put(key, "1");
        }
        redisCacheService.expire(key, userLockSeconds);
    }

    /**
     * 登录成功后清理掉无用的key
     *
     * @param userName
     */
    private void clearLockKey(String userName) {
        redisCacheService.remove(USER_ERROR_TIME_KEY + userName);
    }

    /**
     * APP 用户注册
     *
     * @param sysUser
     * @return
     */
    @PostMapping("/registerUser")
    @ApiOperation(value = "APP用户注册")
    public HttpResult<UcenterMsUserVO> registerUser(@RequestBody UcenterMsUserVO sysUser) {
        // 添加用户信息
        boolean notExist = sysUserService.validataLoginName(sysUser);
        if (notExist) {
            if (sysUser.getUserLoginName().contains("@")) {
                sysUser.setEmail(sysUser.getUserLoginName());
            } else {
                sysUser.setMobile(sysUser.getUserLoginName());
            }
            sysUser.setUpdateTime(new Date());
            sysUser.setCreateTime(new Date());
            sysUser.setGroupCode("fhs");
            sysUser.setIsEnable(Constant.INT_TRUE);
            sysUser.setIsAdmin(Constant.INT_FALSE);
            sysUser.setIsAppUser(Constant.INT_TRUE);
            sysUser.setRoleIds("1");
            sysUserService.addUser(sysUser);
            //添加用户默认设置
            UcenterAppUserSetPO ucenterAppUserSetPO = new UcenterAppUserSetPO();
            ucenterAppUserSetPO.setUserId(sysUser.getUserId());
            ucenterAppUserSetPO.setIsGesture(Constant.INT_FALSE);
            ucenterAppUserSetPO.setLanguage("1");
            ucenterAppUserSetPO.setTimeZone("+8");
            ucenterAppUserSetPO.setIsMessage(Constant.INT_FALSE);
            ucenterAppUserSetPO.setTemperatureType(1);
            ucenterAppUserSetService.insert(ucenterAppUserSetPO);
            return HttpResult.success(sysUser);
        } else {
            throw new ParamException("该账号已经注册了，请直接登录！");
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @ApiOperation("登录")
    public HttpResult<Map<String, Object>> login(@RequestBody LoginVO loginVO, HttpServletRequest request) {
        ParamChecker.isNotNull(loginVO.getUserLoginName(), "用户名不能为空");
        if (loginVO.getIsCode().equals(Constant.INT_FALSE)) {
            ParamChecker.isNotNull(loginVO.getPassword(), "密码不能为空");
        } else {
            //判断短信验证码
            ParamChecker.isNotNull(loginVO.getSmsCode(), "验证码不能为空");
            // 判断短信验证码是否正确过期
            String smsCode = redisCacheService.get(SMS_CODE_KEY + loginVO.getUuid() + loginVO.getUserLoginName()).toString();
            if (StringUtils.isEmpty(smsCode)) {
                throw new ParamException("验证码已过期，请重新发送");
            }
            if (!smsCode.equals(loginVO.getSmsCode())) {
                throw new ParamException("验证码不正确");
            }
        }
        checkUserNameIsLock(loginVO.getUserLoginName());
        if (isVerification) {
            String identifyCode = loginVO.getIdentifyCode();
            Object sessionIdentify = redisCacheService.get(LOGIN_VCODE_KEY + loginVO.getUuid());
            if (null == sessionIdentify) {
                logLoginService.addLoginUserInfo(request, loginVO.getUserLoginName(), true, LoggerConstant.LOG_LOGIN_ERROR_CODE_INVALID, null, false);
                throw new ParamException("验证码失效，请刷新验证码后重新输入");
            }
            if (!sessionIdentify.toString().equals(identifyCode)) {
                logLoginService.addLoginUserInfo(request, loginVO.getUserLoginName(), true, LoggerConstant.LOG_LOGIN_ERROR_CODE, null, false);
                throw new ParamException("验证码错误，请重新输入");
            }
        }
        String userName = loginVO.getUserLoginName();
        UcenterMsUserPO sysUser = sysUserService.login(loginVO);
        if (sysUser == null) {
            logLoginService.addLoginUserInfo(request, userName, true, LoggerConstant.LOG_LOGIN_ERROR_USER, null, false);
            addErrorPassTimes(userName);
            throw new ParamException("账号或者密码错误");
        }
        clearLockKey(userName);
        StpUtil.login(sysUser.getUserId());
        String tokenStr = StpUtil.getTokenValue();
        stpInterface.clearCache(sysUser.getUserId());
        StpUtil.getTokenSession().set(Constant.SESSION_USER, sysUser);
        Map<String, Object> result = new HashMap<>();
        result.put("token", tokenStr);
        result.put("tokenName", StpUtil.getTokenName());
        result.put("userInfo", sysUser);
        logLoginService.addLoginUserInfo(request, sysUser.getUserLoginName(), false, null, sysUser.getUserId(), false);
        return HttpResult.success(result);
    }

    /**
     * APP 忘记密码修改密码
     *
     * @param sysUser
     * @return
     */
    @PostMapping("/updatePassword")
    @ApiOperation(value = "忘记密码验证码修改密码")
    public HttpResult<String> updatePassword(@RequestBody UcenterMsUserVO sysUser) {
        //判断短信验证码
        ParamChecker.isNotNull(sysUser.getSmsCode(), "验证码不能为空");
        // 判断短信验证码是否正确过期
        String smsCode = redisCacheService.get(SMS_CODE_KEY + sysUser.getUuid() + sysUser.getUserLoginName()).toString();
        if (StringUtils.isEmpty(smsCode)) {
            throw new ParamException("验证码已过期，请重新发送");
        }
        if (!smsCode.equals(sysUser.getSmsCode())) {
            throw new ParamException("验证码不正确");
        }
        sysUser.setUpdateTime(new Date());
        // 添加用户信息
        LoginVO loginVO = new LoginVO();
        loginVO.setUserLoginName(sysUser.getUserLoginName());
        UcenterMsUserPO sysUserInfo = sysUserService.login(loginVO);
        if (sysUserInfo == null) {
            throw new ParamException("账号未注册，请先注册账号！");
        }
        int result = sysUserService.updateById(UcenterMsUserPO.builder().userId(sysUserInfo.getUserId()).password(sysUser.getPassword()).build());
        if (result > 0) {
            return HttpResult.success("密码修改成功！");
        }
        throw new ParamException("密码修改失败！");
    }

    /**
     * vue获取用户信息
     */
    @GetMapping("/ms/getUser")
    @ApiOperation("获取用户信息 for VUE")
    public HttpResult<Map<String, Object>> getUserInfo(HttpServletRequest request) {
        UcenterMsUserVO user = UserContext.getSessionuser();
        if (user == null) {
            throw new ParamException("token失效");
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("user", user);
        resultMap.put("permissions", sysUserService.findPermissionByUserId(user.getUserId()));
        if (user.getIsAdmin() == Constant.INT_TRUE) {
            resultMap.put("roles", Arrays.asList("admin"));
        } else {
            List<UcenterMsRoleVO> roleList = roleService.findRolesByUserId(user.getUserId());
            List<String> collect = roleList.stream().map(UcenterMsRoleVO::getRoleName).collect(Collectors.toList());
            String roles = StringUtils.join(roleList.stream().map(UcenterMsRoleVO::getRoleId).collect(Collectors.toList()), ",");
            resultMap.put("roles", collect);
        }
        return HttpResult.success(resultMap);
    }

    /**
     * 获取路由
     *
     * @return
     */
    @GetMapping("/ms/getRouters")
    @ApiOperation("获取路由FOR VUE")
    public HttpResult<List<VueRouterVO>> getRouters(String menuType) {
        UcenterMsUserVO user = UserContext.getSessionuser();
        return HttpResult.success(sysUserService.getRouters(user, menuType));
    }

    /**
     * 生成验证码
     */
    @GetMapping("/defaultKaptcha")
    @ApiOperation("验证码FOR VUE")
    public HttpResult<Map<String, String>> defaultKaptcha()
            throws Exception {
        SCaptcha sCaptcha = new SCaptcha(100, 38);
        String code = sCaptcha.getCode();
        String base64 = Base64Util.byte2Base64(FileUtils.imageToBytes(sCaptcha.getBuffImg()));
        String uuid = StringUtils.getUUID();
        redisCacheService.put(LOGIN_VCODE_KEY + uuid, code);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("img", base64);
        resultMap.put("uuid", uuid);
        return HttpResult.success(resultMap);
    }

    /**
     * 生成手机验证码
     */
    @PostMapping("/getSmsCode")
    @ApiOperation("生成手机验证码FOR VUE")
    public HttpResult<Map<String, String>> getSmsCode(@RequestBody LoginVO loginVO) throws Exception {
        UcenterMsUserPO userPO = new UcenterMsUserPO();
        BeanUtils.copyProperties(loginVO, userPO);
//        String code = String.valueOf((int)(Math.random() * 900000 + 100000));
        if (null != loginVO.getIsRegister() && loginVO.getIsRegister() == 1) {
            boolean notExist = sysUserService.validataLoginName(userPO);
            if (!notExist) {
                throw new ParamException("该账号已经注册了，请直接登录！");
            }
        }/*else {
            boolean notExist = sysUserService.validataLoginName(userPO);
            if (notExist){
                throw new ParamException("该账号还没有注册，请先注册！");
            }
        }*/
        String code = "888888";
        String uuid = StringUtils.getUUID();
        if (loginVO.getUserLoginName().contains("@")) {
            // 发送邮件
        } else {
            // 发送短信
        }
        redisCacheService.put(SMS_CODE_KEY + uuid + loginVO.getUserLoginName(), code);
        redisCacheService.expire(SMS_CODE_KEY + uuid + loginVO.getUserLoginName(), smscodeTimeout);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("code", code);
        resultMap.put("uuid", uuid);
        return HttpResult.success(resultMap);
    }

    /**
     * 验证验证码接口
     */
    @PostMapping("/ifSmsCode")
    @ApiOperation("验证验证码接口")
    public HttpResult<Boolean> ifSmsCode(@RequestBody LoginVO loginVO) throws Exception {
        //判断短信验证码
        ParamChecker.isNotNull(loginVO.getSmsCode(), "验证码不能为空");
        // 判断短信验证码是否正确过期
        String smsCode = redisCacheService.get(SMS_CODE_KEY + loginVO.getUuid() + loginVO.getUserLoginName()).toString();
        if (StringUtils.isEmpty(smsCode)) {
            throw new ParamException("验证码已过期，请重新发送");
        }
        if (!smsCode.equals(loginVO.getSmsCode())) {
            throw new ParamException("验证码不正确");
        }
        return HttpResult.success(true);
    }

    /**
     * 注销登出 for vue
     *
     * @param token
     * @return
     */
    @GetMapping("/logout")
    @ApiOperation("注销登出 for vue")
    public HttpResult<String> logout(String token) {
        redisCacheService.remove("auth:" + token);
        redisCacheService.remove(USER_KEY + token);
        StpUtil.logout();
        return HttpResult.success("登出成功");
    }

    /**
     * 验证当前用户是否存在
     *
     * @param sysUser
     * @return
     */
    @PostMapping("/checkUserIsexist")
    @ApiOperation(value = "验证当前用户是否存在")
    public HttpResult<Boolean> checkUserIsexist(@RequestBody UcenterMsUserVO sysUser) {
        boolean notExist = sysUserService.validataLoginName(sysUser);
        if (notExist) {
            return HttpResult.success(true);
        }
        return HttpResult.success(false);
    }
}