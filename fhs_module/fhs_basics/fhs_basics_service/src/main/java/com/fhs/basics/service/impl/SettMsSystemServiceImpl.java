package com.fhs.basics.service.impl;

import com.fhs.basics.po.SettMsSystemPO;
import com.fhs.basics.po.UcenterMsUserPO;
import com.fhs.basics.mapper.SettMsSystemMapper;
import com.fhs.basics.service.SettMsSystemService;
import com.fhs.basics.vo.SettMsSystemVO;
import com.fhs.common.constant.Constant;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.db.ds.DataSource;
import com.fhs.core.trans.anno.AutoTrans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wanglei
 * @version [版本号, 2018-09-26]
 * @Description:
 * @versio 1.0 陕西小伙伴网络科技有限公司 Copyright (c) 2018 All Rights Reserved.
 */
@Service
@DataSource("base_business")
@AutoTrans(namespace = "sett_ms_system", fields = "name")
public class SettMsSystemServiceImpl extends BaseServiceImpl<SettMsSystemVO, SettMsSystemPO> implements SettMsSystemService {

    @Autowired
    private SettMsSystemMapper mapper;

    @Override
    public List<SettMsSystemVO> getSystemList(UcenterMsUserPO sysUser) {
        if (Constant.INT_TRUE == sysUser.getIsAdmin()) {
            return this.select();
        } else {
            return pos2vos(mapper.getSystemList(sysUser));
        }

    }

}
