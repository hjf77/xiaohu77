package com.fhs.basics.service;

import com.fhs.basics.po.SettMsMenuPermissionPO;
import com.fhs.basics.vo.SettMsMenuPermissionVO;
import com.fhs.core.base.service.BaseService;

import java.util.Map;

/**
 * 系统菜单和权限
 *
 * @author user
 * @date 2020-05-18 16:27:00
 */
public interface SettMsMenuPermissionService extends BaseService<SettMsMenuPermissionVO, SettMsMenuPermissionPO> {


    /**
     * 一键添加增删改查菜单
     *
     * @param map
     * @return
     */
    boolean addBaseMenuBatch(Map<String, String> map);


}
