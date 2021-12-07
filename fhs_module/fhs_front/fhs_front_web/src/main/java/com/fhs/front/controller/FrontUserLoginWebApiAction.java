package com.fhs.front.controller;

import com.fhs.common.utils.*;
import com.fhs.core.exception.ParamException;
import com.fhs.core.result.HttpResult;
import com.fhs.core.valid.checker.ParamChecker;
import com.fhs.front.service.LoginService;
import com.fhs.front.service.UcenterFrontUserService;
import com.fhs.front.vo.UcenterFrontUserVO;
import com.fhs.logger.Logger;
import com.github.liangbaika.validate.annations.AbcValidate;
import com.github.liangbaika.validate.annations.ValidateParam;
import com.github.liangbaika.validate.enums.Check;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 前段用户登录注册 by wanglei
 *
 * @author wanglei
 * @since 2019-05-18 11:51:21
 */
@RestController
@RequestMapping("/webApi/front")
public class FrontUserLoginWebApiAction {
    /**
     * 日志
     */
    private static final Logger LOGGER = Logger.getLogger(FrontUserLoginWebApiAction.class);


    /**
     * 登录服务
     */
    @Autowired
    private LoginService loginService;

    @Autowired
    private UcenterFrontUserService frontUserService;


    /**
     * 获取用户信息
     *
     * @param accessToken 根据授权token获取用户信息
     */
    @GetMapping(value = "/getUserInfo")
    @ApiOperation(value = "获取用户信息")
    public HttpResult<UcenterFrontUserVO> getUserInfo(String accessToken) {
        String userId = loginService.getUserIdByAccessToken(accessToken);
        ParamChecker.isNotNull(userId, "accessToken无效或者超时");
        UcenterFrontUserVO user = frontUserService.selectById(userId);
        user.setPasswd(null);
        return HttpResult.success(user);
    }

    @PostMapping(value = "/loginByUsernameAndPassword")
    @ValidateParam(value = Check.NotEmpty, argName = "userVO.userName")
    @ValidateParam(value = Check.NotEmpty, argName = "userVO.passwd")
    @ApiOperation(value = "使用用户名密码登录")
    public HttpResult<String> loginByUsernameAndPassword(@RequestBody UcenterFrontUserVO userVO) {
        userVO.setPasswd(Md5Util.MD5(userVO.getPasswd()));
        userVO = frontUserService.selectBean(userVO);
        ParamChecker.isNotNull(userVO, "用户名密码无效");
        return HttpResult.success(loginService.login(userVO.getUserId()));
    }


    /**
     * 用户登录
     * 自动识别登录方式
     *
     * @param request  请求
     * @param response 响应
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ApiOperation(value = "用户登录")
    public void login(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        String callBack = request.getParameter("callBack");
        if (CheckUtils.isNullOrEmpty(callBack)) {
            throw new ParamException("callBack 不可以为空");
        }
        session.setAttribute("callBack", callBack);
        String accessToken = ConverterUtils.toString(session.getAttribute("accessToken"));
        //通过accessToken获取用户id
        String userId = loginService.getUserIdByAccessToken(accessToken);
        if (CheckUtils.isNullOrEmpty(userId)) {
            session.removeAttribute("accessToken");
        } else {
            response.sendRedirect(loginService.checkUrl(callBack, accessToken));
            return;
        }
        //截下来处理各种 SSO请求
        String userAgent = ConverterUtils.toString(request.getHeader("user-agent"));
        String url = loginService.getOauth302Impl(userAgent).getLoginUrl(request, response);
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            LOGGER.error("302错误:", e);
        }
    }

    /**
     * 完成登录
     *
     * @param request
     * @param response
     */
    @RequestMapping("complateLogin")
    public void complateLogin(HttpServletRequest request, HttpServletResponse response) {
        //截下来处理各种 SSO请求
        String userAgent = ConverterUtils.toString(request.getHeader("user-agent"));
        loginService.getOauth302Impl(userAgent).complateLogin(request, response);
    }

}

