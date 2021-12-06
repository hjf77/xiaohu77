package com.fhs.front.api.filter;

import com.fhs.common.constant.Constant;
import com.fhs.common.spring.FhsSpringContextUtil;
import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.CookieUtil;
import com.fhs.common.utils.JsonUtil;
import com.fhs.core.config.EConfig;
import com.fhs.core.exception.ParamException;
import com.fhs.core.result.HttpResult;
import com.fhs.front.api.rpc.FeignFrontUserApiService;
import com.fhs.front.form.GetSingleFrontUserForm;
import com.fhs.front.vo.UcenterFrontUserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * c端用户登录过滤器
 *
 * @author user
 * @since 2019-05-18 11:40:05
 */
@WebFilter(urlPatterns = {"/front/*", "/page/h5/*", "/page/pc/*", "/b/*"}, filterName = "userFilter", asyncSupported = true)
public class FrontUserFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(FrontUserFilter.class);
    private static final String JSESSIONID_CODE = "JSESSIONID";
    private static Map jsessionidMapCache = new HashMap();


    /* 前台用户service */
    private FeignFrontUserApiService frontUserService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (frontUserService == null) {
            frontUserService = FhsSpringContextUtil.getBeanByClassForApi(FeignFrontUserApiService.class);
        }

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String uri = request.getRequestURI();

        //如果url 是beetl的，并且不包含h5和pc直接放行
        if (uri.contains("/b/") && !uri.contains("/b/page-h5") && !uri.contains("/b/page-pc")) {
            chain.doFilter(req, res);
            return;
        }
        String token = request.getHeader("token");
        //有token代表是ajax请求，前后端分离的请求
        if (CheckUtils.isNotEmpty(token)) {
            //使用token登录
            if (!loginByToken(request, response, token)) {
                return;
            }
            chain.doFilter(req, res);
            return;
        } else if (CheckUtils.isNotEmpty(request.getParameter("accessToken"))) {
            // 如果验证通过则判断用户是否是vip如果不是vip但是又要vip 那么会 302 如果accessToken验证不通过 会302
            if (login(request, response)) {
                chain.doFilter(req, res);
            }
            return;
        }

        //普通会员
        boolean isUser = uri.contains("_u");
        UcenterFrontUserVO user = (UcenterFrontUserVO) request.getSession().getAttribute("frontUser");
        if (isUser && CheckUtils.isNullOrEmpty(user)) {
            send2Login(response, request);
            return;
        }
        chain.doFilter(req, res);
    }


    @Override
    public void destroy() {

    }

    private boolean loginByToken(HttpServletRequest request, HttpServletResponse response, String token) {

        HttpResult<UcenterFrontUserVO> resultFrontUser = null;
        try {
            resultFrontUser = frontUserService.getSingleFrontUser(GetSingleFrontUserForm.builder().accessToken(token).build());
        } catch (ParamException e) {
            LOGGER.error("获取前端用户信息错误,accessToken为{}", token);
            JsonUtil.outJson(response, HttpResult.otherCodeMsgResult(HttpResult.AUTHORITY_ERROR, "token失效").asJson());
            return false;
        }
        HttpSession session = request.getSession();
        session.setAttribute("frontUser", resultFrontUser.getData());
        return true;
    }


    /**
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    private boolean login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String accessToken = request.getParameter("accessToken");
        HttpResult<UcenterFrontUserVO> resultFrontUser = frontUserService.getSingleFrontUser(GetSingleFrontUserForm.builder().accessToken(accessToken).build());
        if (resultFrontUser.getCode() != Constant.SUCCESS_CODE) {
            LOGGER.error("获取前端用户信息错误,accessToken为{}", accessToken);
            LOGGER.error("获取前端用户信息错误,返回结果为{}", resultFrontUser);
            send2Login(response, request);
            return false;
        }
        UcenterFrontUserVO frontUser = resultFrontUser.getData();//前端用户信息
        if (frontUser == null) {
            send2Login(response, request);
            return false;
        } else {
            CookieUtil.writeCookie("isUser", "true", response);
            session.setAttribute("frontUser", frontUser);
            session.setAttribute("accessToken", accessToken);
            return true;
        }
    }

    /**
     * 跳转登陆页面
     *
     * @param response
     * @param request
     * @throws IOException
     */
    private void send2Login(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String extendsParam = "";
        if (!CheckUtils.isNullOrEmpty(request.getQueryString())) {
            extendsParam = request.getQueryString();
            String[] extendsParams = extendsParam.split("&");
            StringBuilder needExtendsParam = new StringBuilder();
            for (int i = 0; i < extendsParams.length; i++) {
                if (!extendsParams[i].contains("accessToken")) {
                    needExtendsParam.append(extendsParams[i]);
                    if (i != (extendsParams.length - 1)) {
                        needExtendsParam.append("&");
                    }
                }
            }
            extendsParam = needExtendsParam.toString().length() == 0 ? "" : "?" + needExtendsParam.toString();
        }
        String callBack = "&callBack=" + URLEncoder.encode(request.getRequestURL() + extendsParam, "utf-8");
        LOGGER.info("redirect_login_url:" + EConfig.getPathPropertiesValue("redirect_login_url"));
        String loginBaseUrl = EConfig.getPathPropertiesValue("redirect_login_url");
        if (!loginBaseUrl.contains("?")) {
            loginBaseUrl = loginBaseUrl + "?sso=true";
        }
        response.sendRedirect(loginBaseUrl + callBack);
    }

}
