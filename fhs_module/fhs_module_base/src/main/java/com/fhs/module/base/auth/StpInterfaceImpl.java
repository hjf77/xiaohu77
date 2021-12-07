package com.fhs.module.base.auth;

import cn.dev33.satoken.stp.StpInterface;
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

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 返回权限
        return new ArrayList<>(ucenterMsUserService.findPermissionByUserId(ConverterUtils.toLong(loginId)));
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return new ArrayList<>();
    }

}
