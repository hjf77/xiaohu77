package com.fhs.basics.controller;

import com.fhs.basics.dox.UcenterMsUserDO;
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
import com.fhs.core.config.EConfig;
import com.fhs.core.exception.ParamException;
import com.fhs.core.result.HttpResult;
import com.fhs.logger.Logger;
import com.fhs.module.base.shiro.StatelessSubject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 */
@RestController
@RequestMapping("/")
public class MsLoginController extends BaseController {

    private static Logger LOGGER = Logger.getLogger(MsLoginController.class);

    private static final String USER_KEY = "shiro:user:";

    @Value("${server.session.timeout:3600}")
    private Integer sesstionTimeout;

    /**
     * 验证码
     */
    private static final String LOGIN_VCODE_KEY = "login:vcode:";

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


    @Autowired
    private RedisCacheService redisCacheService;


    /**
     * 用户登录
     */
    @RequestMapping("/securityLogin")
    public HttpResult<Boolean> securityLogin(UcenterMsUserDO sysUser, HttpServletRequest request, HttpServletResponse response) {
        String identifyCode = request.getParameter("identifyCode");
        Object sessionIdentify = request.getSession().getAttribute("identifyCode");
        if (null == sessionIdentify)//session 失效
        {
            logLoginService.addLoginUserInfo(request, sysUser.getUserLoginName(),true, LoggerConstant.LOG_LOGIN_ERROR_CODE_INVALID,sysUser.getUserId() );
            throw new ParamException("验证码失效，请刷新验证码后重新输入");
        }
        if (!sessionIdentify.toString().equals(identifyCode)) {
            logLoginService.addLoginUserInfo(request, sysUser.getUserLoginName(), true, LoggerConstant.LOG_LOGIN_ERROR_CODE,sysUser.getUserId() );
            throw new ParamException("验证码错误，请重新输入");
        }
        request.getSession().setAttribute("identifyCode", null);
        String userName = sysUser.getUserLoginName();
        sysUser = sysUserService.login(sysUser);
        if (sysUser == null) {
            logLoginService.addLoginUserInfo(request, userName, true, LoggerConstant.LOG_LOGIN_ERROR_USER,sysUser.getUserId() );
            throw new ParamException("用户名或者密码错误");
        }
        //如果不是admin就去加载全部的数据
        if (sysUser.getIsAdmin() == Constant.INT_TRUE) {
            request.getSession().setAttribute(Constant.SESSION_USER_DATA_PERMISSION, new HashMap<>());
        } else {
            request.getSession().setAttribute(Constant.SESSION_USER_DATA_PERMISSION, sysUserService.findUserDataPermissions(sysUser.getUserId()));
        }
        UsernamePasswordToken token = new UsernamePasswordToken(sysUser.getUserLoginName(), sysUser.getPassword());
        SecurityUtils.getSubject().login(token);
        Subject subjects = SecurityUtils.getSubject();
        // 显示调用，让程序重新去加载授权数据
        subjects.isPermitted("init");
        request.getSession().setAttribute(Constant.SESSION_USER, sysUser);
        logLoginService.addLoginUserInfo(request, sysUser.getUserLoginName(), false, null,sysUser.getUserId() );
        return HttpResult.success(true);
    }

    /**
     * 用户登录
     */
    @RequestMapping("/vueLogin")
    public HttpResult<Map<String, String>> vueLogin(UcenterMsUserDO sysUser, String uuid, HttpServletRequest request, HttpServletResponse response) {
        String identifyCode = request.getParameter("identifyCode");
        Object sessionIdentify = redisCacheService.get(LOGIN_VCODE_KEY + uuid);
        if (null == sessionIdentify) {
            logLoginService.addLoginUserInfo(request, sysUser.getUserLoginName(), true, LoggerConstant.LOG_LOGIN_ERROR_CODE_INVALID,sysUser.getUserId() );
            throw new ParamException("验证码失效，请刷新验证码后重新输入");
        }
        if (!sessionIdentify.toString().equals(identifyCode)) {
            logLoginService.addLoginUserInfo(request, sysUser.getUserLoginName(), true, LoggerConstant.LOG_LOGIN_ERROR_CODE,sysUser.getUserId() );
            throw new ParamException("验证码错误，请重新输入");
        }
        sysUser.setPassword(Md5Util.MD5(sysUser.getPassword()));
        String userName = sysUser.getUserLoginName();
        sysUser = sysUserService.login(sysUser);
        if (sysUser == null) {
            logLoginService.addLoginUserInfo(request, userName, true, LoggerConstant.LOG_LOGIN_ERROR_USER,sysUser.getUserId() );
            throw new ParamException("用户名或者密码错误");
        }
        String tokenStr = StringUtil.getUUID();
        //如果不是admin就去加载全部的数据
        if (sysUser.getIsAdmin() == Constant.INT_TRUE) {
            redisCacheService.put("shiro:dp:" + tokenStr, new HashMap<>());
        } else {
            redisCacheService.put("shiro:dp:" + tokenStr, sysUserService.findUserDataPermissions(sysUser.getUserId()));
        }
        redisCacheService.expire("shiro:dp:" + tokenStr, sesstionTimeout);

        UsernamePasswordToken token = new UsernamePasswordToken(sysUser.getUserLoginName(), sysUser.getPassword());
        SecurityUtils.getSubject().login(token);
        Subject subject = SecurityUtils.getSubject();
        // 显示调用，让程序重新去加载授权数据
        subject.isPermitted("init");
        redisCacheService.put("shiro:" + tokenStr, new StatelessSubject((WebDelegatingSubject) subject));
        redisCacheService.expire("shiro:" + tokenStr, sesstionTimeout);
        redisCacheService.put(USER_KEY + tokenStr, sysUser);
        redisCacheService.expire(USER_KEY + tokenStr, sesstionTimeout);
        Map<String, String> result = new HashMap<>();
        result.put("token", tokenStr);
        logLoginService.addLoginUserInfo(request, sysUser.getUserLoginName(), false, null,sysUser.getUserId());
        return HttpResult.success(result);
    }

    /**
     * vue获取用户信息
     */
    @RequestMapping("/getUserForVue")
    public HttpResult<Map<String, Object>> getUserInfo(HttpServletRequest request) {
        UcenterMsUserVO user = getSessionuser();
        if (user == null) {
            throw new ParamException("token失效");
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("user", user);
        if (user.getIsAdmin() == Constant.INT_TRUE) {
            resultMap.put("roles", Arrays.asList("admin"));
            resultMap.put("permissions", settMsMenuPermissionService.getRolePermisssionByRoleId(null));
        } else {
            List<UcenterMsRoleVO> roleList = roleService.findRolesByUserId(user.getUserId());
            List<String> collect = roleList.stream().map(UcenterMsRoleVO::getRoleName).collect(Collectors.toList());
            String roles = roleList.stream().map(UcenterMsRoleVO::getRoleId).map(Objects::toString).collect(Collectors.joining(",", "'", "'"));
            resultMap.put("roles", collect);
            resultMap.put("permissions", settMsMenuPermissionService.getRolePermisssionByRoleId(roles));
        }
        return HttpResult.success(resultMap);
    }

    /**
     * 获取路由
     *
     * @return
     */
    @RequestMapping("/getRouters")
    public HttpResult<List<VueRouterVO>> getRouters() {
        UcenterMsUserVO user = getSessionuser();
        return HttpResult.success(sysUserService.getRouters(user, Constant.MENU_TYPE_VUE));
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    protected UcenterMsUserVO getSessionuser() {
        HttpServletRequest request = getRequest();
        return (UcenterMsUserVO) request.getSession().getAttribute(Constant.SESSION_USER);
    }

    /**
     * 生成验证码
     */
    @RequestMapping("/defaultKaptchaForVue")
    public HttpResult<Map<String, String>> defaultKaptchaForVue()
            throws Exception {
        SCaptcha sCaptcha = new SCaptcha(100, 38);
        String code = sCaptcha.getCode();
        String base64 = Base64Util.byte2Base64(FileUtils.imageToBytes(sCaptcha.getBuffImg()));
        String uuid = StringUtil.getUUID();
        redisCacheService.put(LOGIN_VCODE_KEY + uuid, code);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("img", base64);
        resultMap.put("uuid", uuid);
        return HttpResult.success(resultMap);
    }

    /**
     * 生成验证码
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/defaultKaptcha")
    public void defaultKaptcha(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        SCaptcha sCaptcha = new SCaptcha();
        String code = sCaptcha.getCode();
        HttpSession session = request.getSession();
        session.setAttribute("identifyCode", code);
        try {
            sCaptcha.write(response.getOutputStream());
        } catch (IOException e) {
            LOGGER.error("二维码生成错误", e);
        }
    }

    /**
     * 退出
     */
    @RequestMapping("ms/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().removeAttribute(Constant.SESSION_USER);
        SecurityUtils.getSubject().logout();
        response.sendRedirect(EConfig.getPathPropertiesValue("basePath") + shrioLoginUrl);
    }

}
