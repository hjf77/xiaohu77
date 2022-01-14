package com.fhs.basics.context;

import cn.dev33.satoken.stp.StpUtil;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.common.constant.Constant;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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
        return (UcenterMsUserVO) StpUtil.getTokenSession().get(Constant.SESSION_USER);
    }


    /**
     * 获取数据权限
     * @return
     */
    public static Map<String, Set<String>> getDataPermission(){
        if(StpUtil.isLogin()){
            StpUtil.getTokenSession().get(Constant.SESSION_USER_DATA_PERMISSION);
        }
        return new HashMap<>();
    }
}
