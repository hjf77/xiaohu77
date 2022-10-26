package com.fhs.basics.context;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.common.constant.Constant;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        try {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if(servletRequestAttributes == null){
                return null;
            }
            return (UcenterMsUserVO) StpUtil.getTokenSession().get(Constant.SESSION_USER);
        } catch (NotLoginException e) {
            return null;
        }
    }


    /**
     * 获取数据权限
     *
     * @return
     */
    public static Map<String, Set<String>> getDataPermission() {
        if (StpUtil.isLogin()) {
            return (Map) StpUtil.getTokenSession().get(Constant.SESSION_USER_DATA_PERMISSION);
        }
        return new HashMap<>();
    }
}
