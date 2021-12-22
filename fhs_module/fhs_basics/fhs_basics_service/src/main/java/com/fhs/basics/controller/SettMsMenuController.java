package com.fhs.basics.controller;

import com.fhs.basics.po.SettMsMenuPO;
import com.fhs.basics.service.SettMsMenuService;
import com.fhs.basics.vo.SettMsMenuVO;
import com.fhs.basics.vo.TreeMenuPermissionVO;
import com.fhs.basics.api.anno.LogNamespace;
import com.fhs.core.logger.Logger;
import com.fhs.module.base.controller.ModelSuperController;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 系统菜单controller
 *
 * @author jianbo.qin
 * @date 2020-05-18 16:53:48
 */
@RestController
@RequestMapping("ms/sysMenu")
@Api(tags = {"菜单"})
@ApiGroup(group = "group_default")
@LogNamespace(namespace = "sysMenu", module = "菜单管理")
public class SettMsMenuController extends ModelSuperController<SettMsMenuVO, SettMsMenuPO> {

    private static final Logger LOG = Logger.getLogger(SettMsMenuController.class);

    @Autowired
    private SettMsMenuService sysMenuService;


    /**
     * vue - element 获取菜单权限按钮
     */
    @GetMapping("getMenuPermissionTree")
    public List<TreeMenuPermissionVO> getMenuPermissionTree(HttpServletRequest request, HttpServletResponse response) {
        List<TreeMenuPermissionVO> menuTrees = sysMenuService.getMenuPermissionTree();
        return menuTrees;
    }


}
