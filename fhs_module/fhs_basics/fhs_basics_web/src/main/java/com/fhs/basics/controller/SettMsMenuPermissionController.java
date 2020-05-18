package com.fhs.basics.controller;

import com.fhs.basics.constant.BaseTransConstant;
import com.fhs.basics.dox.SettMsMenuPermissionDO;
import com.fhs.basics.service.SettMsMenuPermissionService;
import com.fhs.basics.vo.SettMsMenuPermissionVO;
import com.fhs.bislogger.api.anno.LogMethod;
import com.fhs.bislogger.api.anno.LogNamespace;
import com.fhs.bislogger.constant.LoggerConstant;
import com.fhs.module.base.controller.ModelSuperController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * 按钮控制器
 * @author Administrator
 * @date 2020-05-18 16:54:08
 */
@RestController
@RequestMapping("ms/sysMenuPermission")
@LogNamespace(namespace = BaseTransConstant.MENU_INFO_PERMISSION,module = "菜单权限管理")
public class SettMsMenuPermissionController extends ModelSuperController<SettMsMenuPermissionVO, SettMsMenuPermissionDO> {


    @Autowired
    private SettMsMenuPermissionService sysMenuPermissionService;

    /**
     * 一键添加增删改查菜单
     *
     * @param request
     * @paramreponse
     */
    @RequestMapping("addBaseMenuBatch")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_ADD)
    public void addBaseMenuBatch(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = super.getParameterMap();
        boolean result = sysMenuPermissionService.addBaseMenuBatch(map);
        super.outToClient(result);
    }

}
