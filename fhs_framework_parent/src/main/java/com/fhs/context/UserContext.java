package com.fhs.context;

import com.fhs.common.constant.Constant;
import com.fhs.ucenter.api.vo.SysUserVo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class UserContext {
    public static SysUserVo getSessionuser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return (SysUserVo) request.getSession().getAttribute(Constant.SESSION_USER);
    }
}
