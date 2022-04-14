package com.fhs.context;

import com.fhs.common.constant.Constant;
import com.fhs.ucenter.api.vo.SysUserVo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户上下文
 */
public class UserContext {
    /**
     * 获取当前登录用户
     * @return
     */
    public static SysUserVo getSessionuser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return (SysUserVo) request.getSession().getAttribute(Constant.SESSION_USER);
    }

    /**
     * 获取当前登录用户 group code
     * @return
     */
    public static String getUserGroupCode() {
        SysUserVo user = getSessionuser();
        if (user != null) {
            return user.getGroupCode();
        }
        return null;
    }
}
