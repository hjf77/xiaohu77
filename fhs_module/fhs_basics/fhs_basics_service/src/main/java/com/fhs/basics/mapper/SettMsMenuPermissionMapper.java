package com.fhs.basics.mapper;

import com.fhs.basics.po.SettMsMenuPermissionPO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 系统权限mapper
 *
 * @author jianbo.qin
 * @date 2020-05-18 16:16:03
 */
@Repository
public interface SettMsMenuPermissionMapper extends FhsBaseMapper<SettMsMenuPermissionPO> {


    /**
     * 一键添加增删改查菜单
     *
     * @param map
     * @return
     */
    int addBaseMenuBatch(Map<String, Object> map);



}
