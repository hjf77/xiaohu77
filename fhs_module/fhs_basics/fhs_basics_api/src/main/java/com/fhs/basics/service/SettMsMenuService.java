package com.fhs.basics.service;

import com.fhs.basics.po.SettMsMenuPO;
import com.fhs.basics.vo.SettMsMenuVO;
import com.fhs.basics.vo.TreeMenuPermissionVO;
import com.fhs.core.base.service.BaseService;

import java.util.List;

/**
 * 菜单业务接口，实现增删改查等业务
 *
 * @author Administrator
 * @date 2020-05-18 16:35:06
 */
public interface SettMsMenuService extends BaseService<SettMsMenuVO, SettMsMenuPO> {

    /**
     * 不需要显示的菜单
     */
    int NOT_SHOW = 0;

    /**
     * 租户findIdAndNameAndNamespaceList
     */
    int MENU_TYPE_TENANT = 1;


    /**
     * 获取菜单权限按钮
     *
     * @return
     */
    List<TreeMenuPermissionVO> getMenuPermissionTree();






}
