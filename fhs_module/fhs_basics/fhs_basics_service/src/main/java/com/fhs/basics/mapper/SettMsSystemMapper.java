package com.fhs.basics.mapper;

import com.fhs.basics.po.SettMsSystemPO;
import com.fhs.basics.po.UcenterMsUserPO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wanglei
 * @version [版本号, 2018-09-26]
 * @Description: 子系统Mapper 接口
 * @versio 1.0 陕西小伙伴网络科技有限公司  Copyright (c) 2018 All Rights Reserved.
 */
@Repository
public interface SettMsSystemMapper extends FhsBaseMapper<SettMsSystemPO> {

    /**
     * 查询当前用户拥有权限的子系统列表
     *
     * @param sysUser 用户
     * @return 子系统列表
     */
    List<SettMsSystemPO> getSystemList(UcenterMsUserPO sysUser);
}
