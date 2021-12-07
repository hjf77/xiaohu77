package com.fhs.basics.service;

import com.fhs.basics.po.UcenterMsRolePO;
import com.fhs.basics.vo.UcenterMsRoleVO;
import com.fhs.core.base.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 角色服务
 * @Version: 1.0
 * @Author: jackwong
 * @Date 2020-03-19
 */
public interface UcenterMsRoleService extends BaseService<UcenterMsRoleVO, UcenterMsRolePO> {
    /**
     * 资源类型
     */
    String RESOURCETYPES = "resourceTypes";

    /**
     * 部门
     */
    String DEPTS = "depts";

    /**
     * 小区
     */
    String PROJECTS = "projects";


    /**
     * 添加角色信息
     *
     * @param role
     * @return
     */
    boolean addRole(UcenterMsRolePO role);

    /**
     * 添加角色的按钮信息
     *
     * @param adminRole
     * @return
     */
    boolean addButtons(UcenterMsRolePO adminRole);

    /**
     * 删除角色的按钮信息
     *
     * @param adminRole
     * @return
     */
    boolean deleteButtons(UcenterMsRolePO adminRole);



    /**
     * 修改角色信息
     *
     * @param adminRole
     * @return
     */
    boolean updateRole(UcenterMsRolePO adminRole);

    /**
     * 查询角色的按钮信息列表
     *
     * @param adminRole
     * @return
     */
    List<Map<String, Object>> searchButtons(UcenterMsRolePO adminRole);

    /**
     * 查询角色的按钮信息列表
     *
     * @param roleId
     * @return
     */
    String[] getRolePermissionButtons(String roleId);

    /**
     * 根据角色查询按钮id
     *
     * @param map
     * @return
     */
    List<String> searchButtonId(Map<String, Object> map);



    /**
     * 获取一个用户的所有角色
     *
     * @param userId 用户id
     * @return 角色集合
     */
    List<UcenterMsRoleVO> findRolesByUserId(Long userId);

    /**
     * 根据roleid查询用户关联表用户数
     *
     * @param paramMap 查询条件
     * @return 关联用户数量
     */
    Integer findUserCountByRoleId(Map<String, Object> paramMap);

    /**
     * 修改角色权限
     * @param adminRole
     * @return
     */
    boolean updateRoleRermission(UcenterMsRolePO adminRole);
}
