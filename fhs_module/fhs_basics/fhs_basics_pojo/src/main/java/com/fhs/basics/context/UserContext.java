package com.fhs.basics.context;

import cn.dev33.satoken.stp.StpUtil;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.common.constant.Constant;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户上线文
 */
public class UserContext {
    /**
     * 获取session里面的user
     *
     * @return session里面的user
     */
    public static UcenterMsUserVO getSessionuser() {
        return (UcenterMsUserVO) StpUtil.getTokenSession().get(Constant.SESSION_USER);
    }

    /**
     * 获取request
     *
     * @return
     */
    private static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
