package com.fhs.ucenter.dao;

import com.fhs.core.base.dao.BaseDao;
import com.fhs.ucenter.bean.SysRole;
import com.mybatis.jpa.annotation.MapperDefinition;
import com.mybatis.jpa.annotation.MultiTenancyCheck;
import com.mybatis.jpa.annotation.NotMultiTenancyCheck;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 角色DAO
 *
 * @author jianbo.qin
 */
@MultiTenancyCheck
@MapperDefinition(domainClass = SysRole.class)
public interface SysRoleDAO extends BaseDao<SysRole> {
    /**
     * 添加角色的按钮信息
     *
     * @param adminRole
     * @return
     */
    @NotMultiTenancyCheck
    public int addButtons(SysRole adminRole);

    /**
     * 删除角色的按钮信息
     *
     * @param adminRole
     * @return
     */
    @NotMultiTenancyCheck
    public int deleteButtons(SysRole adminRole);

    /**
     * 查询角色的按钮信息
     *
     * @param adminRole
     * @return
     */
    @NotMultiTenancyCheck
    public List<Map<String, Object>> searchButtons(SysRole adminRole);

    @NotMultiTenancyCheck
    public List<String> searchButtonId(Map<String, Object> map);

    /**
     * 根据角色获取角色对象
     *
     * @param map
     * @return
     */
    @NotMultiTenancyCheck
    public List<SysRole> findRoleByGroupCode(Map<String, Object> map);

    /**
     * 通过用户ID获取角色
     *
     * @param userId 用户id
     * @return 用户角色集合
     */
    @NotMultiTenancyCheck
    public List<SysRole> findRolesByUserId(@Param("userId") String userId);

    /**
     * 获取所有角色
     *
     * @return
     * @parammap
     */
    public List<SysRole> findForListAll();

    /**
     * 根据roleid查询用户关联表用户数
     *
     * @param paramMap 查询条件
     * @return 关联用户数量
     */
    @NotMultiTenancyCheck
    Integer findUserCountByRoleId(Map<String, Object> paramMap);

    /**
     * 删除角色用户关联
     *
     * @param adminRole
     */
    @NotMultiTenancyCheck
    void deleteUserRela(SysRole adminRole);
}
