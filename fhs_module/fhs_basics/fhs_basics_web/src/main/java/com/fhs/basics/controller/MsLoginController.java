package com.fhs.basics.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.fhs.basics.po.UcenterMsUserPO;
import com.fhs.basics.service.SettMsMenuPermissionService;
import com.fhs.basics.service.SettMsSystemService;
import com.fhs.basics.service.UcenterMsRoleService;
import com.fhs.basics.service.UcenterMsUserService;
import com.fhs.basics.vo.UcenterMsRoleVO;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.basics.vo.VueRouterVO;
import com.fhs.bislogger.constant.LoggerConstant;
import com.fhs.bislogger.service.LogLoginService;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.*;
import com.fhs.core.base.controller.BaseController;
import com.fhs.core.cache.service.RedisCacheService;
import com.fhs.core.exception.ParamException;
import com.fhs.core.result.HttpResult;
import com.fhs.logger.Logger;
import com.fhs.basics.context.UserContext;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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

    @Value("${sa-token.timeout:3600}")
    private Integer sesstionTimeout;

    @Value("${fhs.vue.is-verification:true}")
    private Boolean isVerification;

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

    /**
     * 子系统服务
     */
    @Autowired
    private SettMsSystemService sysSystemService;

    @Autowired
    private SettMsMenuPermissionService settMsMenuPermissionService;

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
     * 用户登录
     */
    @PostMapping("/login")
    @ApiOperation("登录 for VUE")
    public HttpResult<Map<String, Object>> login(UcenterMsUserPO sysUser, String uuid, HttpServletRequest request, HttpServletResponse response) {
        checkUserNameIsLock(sysUser.getUserLoginName());
        if (isVerification) {
            String identifyCode = request.getParameter("identifyCode");
            Object sessionIdentify = redisCacheService.get(LOGIN_VCODE_KEY + uuid);
            if (null == sessionIdentify) {
                logLoginService.addLoginUserInfo(request, sysUser.getUserLoginName(), true, LoggerConstant.LOG_LOGIN_ERROR_CODE_INVALID, null, false);
                throw new ParamException("验证码失效，请刷新验证码后重新输入");
            }
            if (!sessionIdentify.toString().equals(identifyCode)) {
                logLoginService.addLoginUserInfo(request, sysUser.getUserLoginName(), true, LoggerConstant.LOG_LOGIN_ERROR_CODE, null, false);
                throw new ParamException("验证码错误，请重新输入");
            }
        }
        sysUser.setPassword(Md5Util.MD5(sysUser.getPassword()));
        String userName = sysUser.getUserLoginName();
        sysUser = sysUserService.login(sysUser);
        if (sysUser == null) {
            logLoginService.addLoginUserInfo(request, userName, true, LoggerConstant.LOG_LOGIN_ERROR_USER, null, false);
            addErrorPassTimes(userName);
            throw new ParamException("用户名或者密码错误");
        }
        clearLockKey(userName);
        StpUtil.login(sysUser.getUserId());
        String tokenStr = StpUtil.getTokenValue();
        //如果不是admin就去加载全部的数据
        if (sysUser.getIsAdmin() == Constant.INT_TRUE) {
            redisCacheService.put("auth:dp:" + tokenStr, new HashMap<>());
        } else {
            redisCacheService.put("auth:dp:" + tokenStr, sysUserService.findUserDataPermissions(sysUser.getUserId()));
        }
        redisCacheService.expire("auth:dp:" + tokenStr, sesstionTimeout);
        StpUtil.getTokenSession().set(Constant.SESSION_USER,sysUser);
        Map<String, Object> result = new HashMap<>();
        result.put("token", tokenStr);
        result.put("tokenName", StpUtil.getTokenName());
        result.put("userInfo", sysUser);
        logLoginService.addLoginUserInfo(request, sysUser.getUserLoginName(), false, null, sysUser.getUserId(), false);
        return HttpResult.success(result);
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



    @GetMapping("/logout")
    @ApiOperation("注销登出 for vue")
    public HttpResult<String> logout(String token) {
        redisCacheService.remove("auth:" + token);
        redisCacheService.remove(USER_KEY + token);
        StpUtil.logout();
        return HttpResult.success("登出成功");
    }

}
