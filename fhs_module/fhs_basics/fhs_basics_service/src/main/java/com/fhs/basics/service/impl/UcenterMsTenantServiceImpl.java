package com.fhs.basics.service.impl;

import com.fhs.basics.dox.UcenterMsOrganizationDO;
import com.fhs.basics.dox.UcenterMsUserDO;
import com.fhs.basics.dox.UcenterMsTenantDO;
import com.fhs.basics.service.UcenterMsOrganizationService;
import com.fhs.basics.service.UcenterMsUserService;
import com.fhs.basics.service.UcenterMsTenantService;
import com.fhs.basics.vo.UcenterMsTenantVO;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.Md5Util;
import com.fhs.common.utils.StringUtil;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 租户管理(UcenterMsTenant)表服务实现类
 *
 * @author jackwong
 * @since 2019-05-15 14:21:04
 */
@Service("ucenterMsTenantService")
public class UcenterMsTenantServiceImpl extends BaseServiceImpl<UcenterMsTenantVO, UcenterMsTenantDO> implements UcenterMsTenantService {


    @Lazy
    @Autowired
    private UcenterMsUserService sysUserService;

    @Autowired
    private UcenterMsOrganizationService organizationService;

    @Override
    public int insertSelective(UcenterMsTenantDO tenant) {
        UcenterMsUserDO adminUser = new UcenterMsUserDO();
        adminUser.setGroupCode(tenant.getGroupCode());
        adminUser.setPassword(Md5Util.MD5(tenant.getGroupCode() + "123456").toLowerCase());
        adminUser.setUserId(StringUtil.getUUID());
        adminUser.setIsAdmin(Constant.INT_TRUE);
        adminUser.setUserName(tenant.getTenantName());
        adminUser.setUserLoginName(tenant.getGroupCode() + "_admin");
        adminUser.setMobile(tenant.getMobile());
        adminUser.setOrganizationId(tenant.getGroupCode() + "_001");
        adminUser.setIsEnable(Constant.INT_TRUE);
        adminUser.preInsert(null);
        sysUserService.insertSelective(adminUser);
        UcenterMsOrganizationDO organization = new UcenterMsOrganizationDO();
        organization.setId(tenant.getGroupCode() + "_001");
        organization.setName(tenant.getTenantName());
        organization.setRanking("1");
        organization.setIsEnable(Constant.INT_TRUE);
        organization.preInsert(tenant.getCreateUser());
        organization.setParentId("");
        organization.setGroupCode(tenant.getGroupCode());
        organizationService.insertSelective(organization);
        tenant.setId(StringUtil.getUUID());
        return super.insertSelective(tenant);
    }
}