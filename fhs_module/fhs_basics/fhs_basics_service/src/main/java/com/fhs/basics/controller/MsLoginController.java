package com.fhs.basics.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fhs.basics.po.UcenterMsUserPO;
import com.fhs.basics.service.UcenterMsRoleService;
import com.fhs.basics.service.UcenterMsUserService;
import com.fhs.basics.vo.LoginVO;
import com.fhs.basics.vo.UcenterMsRoleVO;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.basics.vo.VueRouterVO;
import com.fhs.basics.constant.LoggerConstant;
import com.fhs.basics.service.LogLoginService;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.*;
import com.fhs.core.base.controller.BaseController;
import com.fhs.core.base.vo.FhsPager;
import com.fhs.core.base.vo.QueryField;
import com.fhs.core.base.vo.QueryFilter;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
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

    @Value("${user.ssourl}")
    private String ssourl;

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
    @ApiOperation("登录")
    public HttpResult<Map<String, Object>> login(@RequestBody LoginVO loginVO, HttpServletRequest request) {
        ParamChecker.isNotNull(loginVO.getUserLoginName(),"用户名不能为空");
        ParamChecker.isNotNull(loginVO.getPassword(),"密码不能为空");
        checkUserNameIsLock(loginVO.getUserLoginName());
        if (isVerification) {
            String identifyCode = loginVO.getIdentifyCode();
            Object sessionIdentify = redisCacheService.get(LOGIN_VCODE_KEY + loginVO.getUuid());
//            if (null == sessionIdentify) {
//                logLoginService.addLoginUserInfo(request, loginVO.getUserLoginName(), true, LoggerConstant.LOG_LOGIN_ERROR_CODE_INVALID, null, false);
//                throw new ParamException("验证码失效，请刷新验证码后重新输入");
//            }
//            if (!sessionIdentify.toString().equals(identifyCode)) {
//                logLoginService.addLoginUserInfo(request, loginVO.getUserLoginName(), true, LoggerConstant.LOG_LOGIN_ERROR_CODE, null, false);
//                throw new ParamException("验证码错误，请重新输入");
//            }
        }
        String userName = loginVO.getUserLoginName();
        UcenterMsUserPO sysUser = sysUserService.login(loginVO);
        if (sysUser == null) {
            logLoginService.addLoginUserInfo(request, userName, true, LoggerConstant.LOG_LOGIN_ERROR_USER, null, false);
            addErrorPassTimes(userName);
            throw new ParamException("用户名或者密码错误");
        }
        clearLockKey(userName);
        StpUtil.login(sysUser.getUserId());
        String tokenStr = StpUtil.getTokenValue();
        stpInterface.clearCache(sysUser.getUserId());
        StpUtil.getTokenSession().set(Constant.SESSION_USER,sysUser);
        Map<String, Object> result = new HashMap<>();
        result.put("token", tokenStr);
        result.put("tokenName", StpUtil.getTokenName());
        result.put("userInfo", sysUser);
        logLoginService.addLoginUserInfo(request, sysUser.getUserLoginName(), false, null, sysUser.getUserId(), false);
        return HttpResult.success(result);
    }

    /**
     * 单点登录
     * @param loginVO
     * @param request
     * @return
     */
    @PostMapping("/autoLogin")
    @ApiOperation("登录")
    public HttpResult<Map<String, Object>> autoLogin(@RequestBody LoginVO loginVO, HttpServletRequest request) {
        String jwt = loginVO.getJwt();
        //远程调用接口返回hrCode查询
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization","Bearer " + jwt);
        HttpEntity<String> entity = new HttpEntity<>(null,headers);
        RestTemplate restClient = new RestTemplate();
        ResponseEntity<String> response = restClient.exchange(ssourl, HttpMethod.GET, entity, String.class);
        LOGGER.info("response" + response);
        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        LOGGER.info("jsonObject" + jsonObject);
        if(jsonObject.getString("code").equals("success")){
            JSONObject jsonData = JSONObject.parseObject(jsonObject.get("data").toString());
            loginVO.setHrCode(jsonData.getString("hrCode"));
            UcenterMsUserPO sysUser = sysUserService.login(loginVO);
            if (sysUser == null) {
                throw new ParamException("该用户不存在请在系统中新增！");
            }
            clearLockKey(sysUser.getUserLoginName());
            StpUtil.login(sysUser.getUserId());
            String tokenStr = StpUtil.getTokenValue();
            stpInterface.clearCache(sysUser.getUserId());
            StpUtil.getTokenSession().set(Constant.SESSION_USER,sysUser);
            Map<String, Object> result = new HashMap<>();
            result.put("token", tokenStr);
            result.put("tokenName", StpUtil.getTokenName());
            result.put("userInfo", sysUser);
            logLoginService.addLoginUserInfo(request, sysUser.getUserLoginName(), false, null, sysUser.getUserId(), false);
            return HttpResult.success(result);
        }
        throw new ParamException(jsonObject.getString("msg"));
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
     * 用户在线列表查询
     * @param filter
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/ms/getUserOnlineList")
    @ApiOperation("用户在线列表查询")
    public IPage<UcenterMsUserVO> getUserOnlineList(@RequestBody QueryFilter<UcenterMsUserVO> filter, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> parameterMap = new HashMap();
        if (!CollectionUtils.isEmpty(filter.getQuerys())) {
            for (QueryField queryField : filter.getQuerys()) {
                parameterMap.put(queryField.getProperty(), queryField.getValue() + "");
            }
        }
        //获取在线用户token
        List<String> userIdList = new ArrayList<>();
        List<String> tokenList = StpUtil.searchTokenValue("", -1, -1);
        for (String token : tokenList) {
            Object loginIdByToken = StpUtil.getLoginIdByToken(token.replaceAll("Authorization:login:token:",""));
            userIdList.add(loginIdByToken.toString());
        }
        if(CollectionUtils.isEmpty(userIdList)){
            return new FhsPager<>();
        }
        userIdList = userIdList.stream().distinct().collect(Collectors.toList());
        String userIds = StringUtils.join(userIdList, ",");
        parameterMap.put("userIds",userIds);
        return sysUserService.userOnlineList(filter.getPagerInfo(), parameterMap);
    }

    /**
     * 用户在线统计
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/ms/getUserOnlineTotal")
    @ApiOperation("用户在线统计")
    public HttpResult<Map<String,Object>> getUserOnlineTotal(HttpServletRequest request, HttpServletResponse response) {
        String userOrgId = UserContext.getSessionuser().getOrganizationId();
        Map<String, Object> parameterMap = new HashMap();
        Long userCount = sysUserService.selectCountMP(new LambdaQueryWrapper<UcenterMsUserPO>()
                .eq(UcenterMsUserPO::getIsEnable, Constant.INT_TRUE)
                .likeRight(UcenterMsUserPO::getOrganizationId,userOrgId));
        parameterMap.put("userCount",userCount);
        double count = userCount * 1.0;
        //获取在线用户的token
        List<String> userIdList = new ArrayList<>();
        List<String> tokenList = StpUtil.searchTokenValue("", -1, -1);
        for (String token : tokenList) {
            Object loginIdByToken = StpUtil.getLoginIdByToken(token.replaceAll("Authorization:login:token:",""));
            userIdList.add(loginIdByToken.toString());
        }
        userIdList = userIdList.stream().distinct().collect(Collectors.toList());
        DecimalFormat df1 = new DecimalFormat("0.00%");
        List<UcenterMsUserVO> ucenterMsUserList = sysUserService.selectListMP(new LambdaQueryWrapper<UcenterMsUserPO>()
                .eq(UcenterMsUserPO::getIsEnable, Constant.INT_TRUE)
                .in(UcenterMsUserPO::getUserId, userIdList)
                .likeRight(UcenterMsUserPO::getOrganizationId,userOrgId));
        parameterMap.put("onlineNum",ucenterMsUserList.size());
        //作业区在线人数
        long opeAreaCount = ucenterMsUserList.stream().filter(u -> u.getOrganizationId().length() >= 12).count();
        parameterMap.put("opeAreaCount",opeAreaCount);
        //计算作业区登录率
        double opeAreaNum = opeAreaCount * 1.0;
        parameterMap.put("opeAreaRate",df1.format(opeAreaNum / count));
        //二级单位在线人数
        long oilProCount = ucenterMsUserList.stream().filter(u -> u.getOrganizationId().length() == 9).count();
        parameterMap.put("oilProCount",oilProCount);
        //计算二级单位登录率
        double oilProNum = oilProCount * 1.0;
        parameterMap.put("oilProRate",df1.format(oilProNum / count));
        //总登录率
        double userLoginNum = ucenterMsUserList.size() * 1.0;
        parameterMap.put("userLoginRate",df1.format(userLoginNum / count));
        return HttpResult.success(parameterMap);
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

    @GetMapping("/getTokenList")
    public HttpResult<Map<String,Object>> getTokenList(){
        Map<String,Object> map = new HashMap<>();
        List<String> sessionList = StpUtil.searchSessionId("", -1, -1);
        List<String> tokenList = StpUtil.searchTokenValue("", -1, -1);
        List<String> tokenSessionList = StpUtil.searchTokenSessionId("", -1, -1);
        map.put("sessionList",sessionList);
        map.put("tokenList",tokenList);
        map.put("tokenSessionList",tokenSessionList);
        return HttpResult.success(map);
    }

    @GetMapping("/getTokenSession")
    public HttpResult<Map<String,Object>> getTokenSession(){
        Map<String,Object> map = new HashMap<>();
        List<SaSession> listMap = new ArrayList<>();
        List<Object> objList = new ArrayList<>();
        List<String> tokenList = StpUtil.searchTokenValue("", -1, -1);
        for (String token : tokenList) {
            SaSession tokenSessionByToken = StpUtil.getTokenSessionByToken(token.replaceAll("Authorization:login:token:",""));
            listMap.add(tokenSessionByToken);
        }
        for (String token : tokenList) {
            Object loginIdByToken = StpUtil.getLoginIdByToken(token.replaceAll("Authorization:login:token:",""));
            objList.add(loginIdByToken);
        }
        map.put("listMap",listMap);
        map.put("objList",objList);
        map.put("tokenList",tokenList);
        return HttpResult.success(map);
    }

    @GetMapping("/getToken")
    public void getToken(){
        //List<String> sessionList = StpUtil.searchSessionId("", -1, -1);
        List<String> tokenList = StpUtil.searchTokenValue("", -1, -1);
        //List<String> tokenSessionList = StpUtil.searchTokenSessionId("", -1, -1);
        for (String token : tokenList) {
            String t = token.replaceAll("Authorization:login:token:", "");
            try {
                redisCacheService.remove("auth:" + t);
                redisCacheService.remove(USER_KEY + t);
                StpUtil.logoutByTokenValue(t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
