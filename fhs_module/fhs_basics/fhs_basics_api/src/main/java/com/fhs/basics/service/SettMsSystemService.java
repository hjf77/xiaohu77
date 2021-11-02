package com.fhs.basics.service;

import com.fhs.basics.po.SettMsSystemPO;
import com.fhs.basics.po.UcenterMsUserPO;
import com.fhs.basics.vo.ComboboxNodeVO;
import com.fhs.basics.vo.SettMsSystemVO;
import com.fhs.core.base.service.BaseService;

import java.util.List;

/**
 * @author wanglei
 * @version [版本号, 2018-09-26]
 * @Description: 子系统服务
 * @versio 1.0 陕西小伙伴网络科技有限公司  Copyright (c) 2018 All Rights Reserved.
 */
public interface SettMsSystemService extends BaseService<SettMsSystemVO, SettMsSystemPO> {

    /**
     * 超管用户
     */
    public static final int ADMIN = 1;

    /**
     * 获取全部商户的combbox
     *
     * @return
     */
    List<ComboboxNodeVO> getSystemComBoxData();

    /**
     * 查询当前登录用户拥有权限的子系统列表
     *
     * @param sysUser 登录用户
     * @return 子系统列表
     */
    List<SettMsSystemVO> getSystemList(UcenterMsUserPO sysUser);
}
