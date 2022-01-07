package com.fhs.module.base.auth;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.fhs.basics.service.UcenterMsUserService;
import com.fhs.common.utils.ConverterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * sa-token 权限认证
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private UcenterMsUserService ucenterMsUserService;

    @CreateCache(expire = 1800, name = "user:permission", cacheType = CacheType.BOTH)
    private Cache<Object, List<String>> permissionCache;

    /**
     * 清理缓存
     * @param userId
     */
    public void clearCache(Long userId){
        permissionCache.remove(userId);
    }

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> permissions =  permissionCache.get(StpUtil.getTokenInfo().tokenValue);
        if(permissions==null){
            permissions = new ArrayList<>(ucenterMsUserService.findPermissionByUserId(ConverterUtils.toLong(loginId)));
            permissionCache.put(StpUtil.getTokenInfo().tokenValue,permissions);
            return permissions;
        }
        // 返回权限
        return permissions;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return new ArrayList<>();
    }

}
