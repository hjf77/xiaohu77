package com.fhs.basics.mapper;

import com.fhs.basics.po.UcenterMsRolePO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 角色mapper 接口
 *
 * @author jianbo.qin
 * @date 2020-05-18 16:21:03
 */
@Repository
public interface UcenterMsRoleMapper extends FhsBaseMapper<UcenterMsRolePO> {
    /**
     * 添加角色的按钮信息
     *
     * @param adminRole
     * @return
     */
    public int addButtons(UcenterMsRolePO adminRole);

    /**
     * 删除角色的按钮信息
     *
     * @param adminRole
     * @return
     */
    public int deleteButtons(UcenterMsRolePO adminRole);

    /**
     * 查询角色的按钮信息
     *
     * @param adminRole
     * @return
     */
    public List<Map<String, Object>> searchButtons(UcenterMsRolePO adminRole);

    /**
     * 查询角色的按钮信息 id
     *
     * @param roleId
     * @return
     */
    public List<String> getRolePermissionButtons(@Param("roleId") Long roleId);

    public List<String> searchButtonId(Map<String, Object> map);

    /**
     * 根据角色获取角色对象
     *
     * @param map
     * @return
     */
    public List<UcenterMsRolePO> findRoleByGroupCode(Map<String, Object> map);

    /**
     * 通过用户ID获取角色
     *
     * @param userId 用户id
     * @return 用户角色集合
     */
    public List<UcenterMsRolePO> findRolesByUserId(@Param("userId") Long userId);

    /**
     * 获取所有角色
     *
     * @return
     * @parammap
     */
    public List<UcenterMsRolePO> findForListAll();

    /**
     * 根据roleid查询用户关联表用户数
     *
     * @param paramMap 查询条件
     * @return 关联用户数量
     */
    Integer findUserCountByRoleId(Map<String, Object> paramMap);

    /**
     * 删除角色用户关联
     *
     * @param adminRole
     */
    void deleteUserRela(UcenterMsRolePO adminRole);
}
