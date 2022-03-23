package com.fhs.basics.service.impl;

import com.fhs.basics.mapper.UcenterMsRoleMapper;
import com.fhs.basics.po.UcenterMsRolePO;
import com.fhs.basics.service.UcenterMsRoleService;
import com.fhs.basics.service.UcenterMsUserService;
import com.fhs.basics.vo.UcenterMsRoleVO;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.common.utils.ListUtils;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.db.ds.DataSource;
import com.fhs.core.exception.ParamException;
import com.fhs.core.logger.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统角色实现
 * 老代码待优化
 *
 * @author user
 * @date 2020-05-18 16:43:36
 */
@Primary
@Service
@DataSource("basic")
public class UcenterMsRoleServiceImpl extends BaseServiceImpl<UcenterMsRoleVO, UcenterMsRolePO> implements UcenterMsRoleService {

    private static final Logger LOG = Logger.getLogger(UcenterMsRoleServiceImpl.class);

    @Autowired
    private UcenterMsRoleMapper mapper;

    /**
     * 后台用户服务
     */
    private UcenterMsUserService userService;

    /**
     * 添加角色信息
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean addRole(UcenterMsRolePO adminRole) {
        long countMP = this.findCount(UcenterMsRolePO.builder().roleName(adminRole.getRoleName()).build());
        if (countMP != 0) {
            throw new ParamException("角色名称不能重复");
        }
        // 插入角色信息
        int count = super.insertSelective(adminRole);
        if (count > 0) {
            return saveButtons(adminRole);
        }
        return false;
    }

    /**
     * 添加角色的权限信息
     *
     * @param adminRole
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean saveButtons(UcenterMsRolePO adminRole) {
        if (adminRole.getMethods() != null && adminRole.getMethods().length > 0) {
            // 构建按钮列表
            adminRole.setMethods(buildButtonArray(adminRole.getMethods()));

            // 添加角色按钮信息
            return addButtons(adminRole);
        }
        return true;
    }

    /**
     * 构建按钮列表集合
     *
     * @param buttons
     * @return
     */
    private String[] buildButtonArray(String[] buttons) {
        List<String> list = new ArrayList<String>();
        for (String item : buttons) {
            String[] itemarr = item.split("_");
            //p前缀 直接跟的就是权限id
            if ("p".equalsIgnoreCase(itemarr[0])) {
                list.add(itemarr[1]);
            } else if ("t".equalsIgnoreCase(itemarr[0])) {
                String buttonId = itemarr[1].split(":")[1];
                list.add(buttonId);
            } else {
                if (itemarr.length > 1) {
                    String buttonType = itemarr[1].split(":")[1];
                    String menuId = itemarr[1].split(":")[0];
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("buttonType", buttonType);
                    map.put("menuId", menuId);
                    List<String> itemlist = searchButtonId(map);
                    list.addAll(itemlist);
                }
            }
        }
        return list.toArray(new String[]{});
    }

    /***
     * 添加角色对应的操作信息
     */
    @Override
    public boolean addButtons(UcenterMsRolePO adminRole) {
        int count = mapper.addButtons(adminRole);
        return (count > 0);
    }

    /**
     * 删除角色信息
     */
    @Override
    public boolean deleteButtons(UcenterMsRolePO adminRole) {
        try {
            mapper.deleteButtons(adminRole);
            return true;
        } catch (Exception e) {
            LOG.error("删除关联按钮数据错误", e);
            return false;
        }
    }

    /**
     * 删除角色信息
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int deleteById(Serializable primaryValue) {
        UcenterMsRoleVO role = new UcenterMsRoleVO();
        role.setRoleId(ConverterUtils.toInt(primaryValue));
        // 删除按钮信息
        boolean count = deleteButtons(role);
        if (count) {
            // 删除角色用户关联
            mapper.deleteUserRela(role);
            // 删除角色信息
            return super.deleteById(primaryValue);
        }
        return 0;
    }

    /**
     * 更新角色信息
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean updateRole(UcenterMsRolePO adminRole) {
        // 删除当前角色的按钮信息
        boolean count = deleteButtons(adminRole);
        if (count) {
            // 修改当前角色信息
            int result = super.updateSelectiveById(adminRole);
            if (result > 0) {
                if (adminRole.getMethods() != null && adminRole.getMethods().length > 0) {
                    // 构建按钮列表
                    adminRole.setMethods(buildButtonArray(adminRole.getMethods()));
                    // 插入按钮信息
                    return addButtons(adminRole);
                }
                return true;
            }
        }
        return false;
    }


    /**
     * 查询角色的按钮信息
     */
    @Override
    public List<Map<String, Object>> searchButtons(UcenterMsRolePO adminRole) {
        return mapper.searchButtons(adminRole);
    }

    /**
     * 查询角色的按钮信息
     */
    @Override
    public String[] getRolePermissionButtons(Integer id) {
        List<String> rolePermissionButtons = mapper.getRolePermissionButtons(id);
        return rolePermissionButtons.toArray(new String[rolePermissionButtons.size()]);
    }

    /**
     * 查询按钮id列表
     */
    @Override
    public List<String> searchButtonId(Map<String, Object> map) {
        return mapper.searchButtonId(map);
    }

    /**
     * 根据用户获取角色
     */
    @Override
    public List<UcenterMsRoleVO> findRolesByUserId(Long userId) {
        return ListUtils.copyListToList(mapper.findRolesByUserId(userId), UcenterMsRoleVO.class);
    }

    @Override
    public Integer findUserCountByRoleId(Map<String, Object> paramMap) {
        return mapper.findUserCountByRoleId(paramMap);
    }


    /**
     * 更新角色授权
     *
     * @param adminRole
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean updateRoleRermission(UcenterMsRolePO adminRole) {
        // 删除当前角色的按钮信息
        boolean count = deleteButtons(adminRole);
        if (count) {
            if (adminRole.getMethods() != null && adminRole.getMethods().length > 0) {
                // 构建按钮列表
                adminRole.setMethods(buildButtonArray(adminRole.getMethods()));
                // 插入按钮信息
                return addButtons(adminRole);
            }
            return true;
        }
        return false;
    }
}
