package com.fhs.module.base.auth;

import com.fhs.basics.vo.UcenterMsUserVO;

import java.util.Map;
import java.util.Set;

/**
 * 数据权限加载
 */
public interface DataPermissionLoader {
    /**
     * 加载数据权限
     * @param user 用户
     * @param tokenValue 用户登录token
     * @return
     */
    Map<String, Set<String>> loadDataPermissions(UcenterMsUserVO user,String tokenValue);
}
