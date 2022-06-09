package com.fhs.basics.controller;

import com.fhs.basics.po.UcenterMsTenantPO;
import com.fhs.basics.service.UcenterMsUserService;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.basics.vo.UcenterMsTenantVO;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.Md5Util;
import com.fhs.common.utils.StringUtils;
import com.fhs.core.result.HttpResult;
import com.fhs.core.valid.checker.ParamChecker;
import com.fhs.module.base.controller.ModelSuperController;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 租户管理(UcenterMsTenant)表控制层
 *
 * @author wanglei
 * @since 2019-05-15 14:21:04
 */
@RestController
@Api(tags = {"租户"})
@RequestMapping("/ms/tenant")
@ApiGroup(group = "group_default")
public class UcenterMsTenantController extends ModelSuperController<UcenterMsTenantVO, UcenterMsTenantPO, String> {

    @Autowired
    private UcenterMsUserService userService;

    /**
     * 重置某个租户密码
     *
     * @param groupCode 租户集团编码
     * @return 重置后的密码
     */
    @RequestMapping("resetAdminPass")
    public HttpResult<String> resetAdminPass(String groupCode) {
        ParamChecker.isNotNull(groupCode, "groupCode不能为空");
        String newPass = StringUtils.getUUID();
        UcenterMsUserVO user = new UcenterMsUserVO();
        user.setUserLoginName(groupCode + "_admin");
        user = userService.selectBean(user);
        ParamChecker.isNotNull(user, "用户信息为空，请联系运维");
        user.setIsEnable(Constant.INT_TRUE);
        user.setPassword(Md5Util.MD5(newPass).toLowerCase());
        userService.updateSelectiveById(user);
        return HttpResult.success(newPass);
    }
}
