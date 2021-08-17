package com.fhs.module.base.shiro;

import com.fhs.basics.dox.UcenterMsUserDO;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.JsonUtils;
import com.fhs.core.cache.service.RedisCacheService;
import com.fhs.core.result.PubResult;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthFilter  extends AuthenticatingFilter {

    private RedisCacheService<UcenterMsUserDO> redisCacheService;

    public AuthFilter(RedisCacheService<UcenterMsUserDO> redisCacheService) {
        this.redisCacheService = redisCacheService;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        UcenterMsUserDO sysUser = (UcenterMsUserDO)request.getSession().getAttribute(Constant.SESSION_USER);
        if(sysUser==null){
            JsonUtils.outJson((HttpServletResponse) servletResponse, PubResult.NOT_LOGIN.asResult().asJson());
            return null;
        }


        // 吧 accessToken 设置进 Shiro
        UsernamePasswordToken customUsernamePasswordToken = new UsernamePasswordToken(sysUser.getUserLoginName(), sysUser.getPassword());
        return customUsernamePasswordToken;
    }

    //所有请求全部拒绝访问
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //允许option请求通过
        if (((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }
        return false;
    }

    //拒绝访问的请求，onAccessDenied方法先获取 token，再调用executeLogin方法
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest= (HttpServletRequest) request;
        HttpServletResponse httpServletResponse= (HttpServletResponse) response;
        String token = httpServletRequest.getHeader(Constant.VUE_HEADER_TOKEN_KEY);
        if(CheckUtils.isNullOrEmpty(token)){
            token  = httpServletRequest.getParameter("token");
        }
        if(CheckUtils.isNullOrEmpty(token)){
            JsonUtils.outJson(httpServletResponse, PubResult.NOT_TOKEN.asResult().asJson());
            return false;
        }
        UcenterMsUserDO sysUser = redisCacheService.get("shiro:user:" + token);
        if(sysUser==null){
            JsonUtils.outJson(httpServletResponse, PubResult.NOT_LOGIN.asResult().asJson());
            return false;
        }
        httpServletRequest.getSession().setAttribute(Constant.SESSION_USER, sysUser);
        TokenContext.setToken(token);
        return executeLogin(request,response);
    }

    //token失效时调用
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        JsonUtils.outJson((HttpServletResponse)response , PubResult.NOT_LOGIN.asResult().asJson());
        return false;
    }

}
