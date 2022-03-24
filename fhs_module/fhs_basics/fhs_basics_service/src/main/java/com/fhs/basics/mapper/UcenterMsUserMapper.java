package com.fhs.basics.mapper;

import com.fhs.basics.dox.SettMsMenuDO;
import com.fhs.basics.dox.SettMsMenuPermissionDO;
import com.fhs.basics.dox.UcenterMsUserDO;
import com.fhs.basics.vo.SysUserOrgVO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import com.mybatis.jpa.annotation.MapperDefinition;
import com.mybatis.jpa.annotation.MultiTenancyCheck;
import com.mybatis.jpa.annotation.NotMultiTenancyCheck;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 系统用户mapper
 *
 * @author jianbo.qin
 * @date 2020-05-18 16:23:28
 */
@Repository
@MultiTenancyCheck
@MapperDefinition(domainClass = UcenterMsUserDO.class)
public interface UcenterMsUserMapper extends FhsBaseMapper<UcenterMsUserDO> {
    /**
     * 用户登录
     */
    @NotMultiTenancyCheck
    UcenterMsUserDO login(UcenterMsUserDO adminUser);

    /**
     * 添加用户角色
     *
     * @param adminUser
     * @return
     */
    @NotMultiTenancyCheck
    int addUserRole(UcenterMsUserDO adminUser);

    /**
     * 查询用户角色
     *
     * @param adminUser
     * @return
     */
    @NotMultiTenancyCheck
    List<Map<String, Object>> searchUserRole(UcenterMsUserDO adminUser);

    /**
     * 删除当前用户的角色
     *
     * @param adminUser
     * @return
     */
    @NotMultiTenancyCheck
    int deleteUserRole(UcenterMsUserDO adminUser);

    /**
     * 根据用户查询菜单
     *
     * @param adminUser
     * @return
     */
    @NotMultiTenancyCheck
    List<SettMsMenuDO> selectMenuByUid(UcenterMsUserDO adminUser);

    /**
     * 根据父节点查询菜单
     *
     * @param map
     * @return
     */
    @NotMultiTenancyCheck
    SettMsMenuDO selectParentMenuById(Map<String, Object> map);

    /**
     * 根据Ids查询菜单
     *
     * @param map
     * @return
     */
    @NotMultiTenancyCheck
    List<SettMsMenuDO> readMenuByIds(Map<String, Object> map);

    /**
     * 监测原始密码是否正确
     *
     * @return
     * @paramsysUserGroupcode
     */
    @NotMultiTenancyCheck
    int validataPass(UcenterMsUserDO adminUser);

    /**
     * 根据登录名获取用户数
     *
     * @return
     * @paramsysUserGroupcode
     */
    @NotMultiTenancyCheck
    int getAdminUserCountByLoginName(UcenterMsUserDO adminUser);

    /**
     * 修改用户密码
     *
     * @return
     * @paramsysUserGroupcode
     */
    int updatePass(UcenterMsUserDO adminUser);

    /**
     * 根据用户查询权限
     *
     * @param adminUser
     * @return
     */
    @NotMultiTenancyCheck
    List<SettMsMenuPermissionDO> searchUserButton(UcenterMsUserDO adminUser);

    /**
     * 根据权限IDs获取权限对象
     *
     * @return
     * @paramadminUser
     */
    @NotMultiTenancyCheck
    List<SettMsMenuPermissionDO> searchUserButtonIds(Map<String, Object> map);

    /**
     * 获取权限对象
     *
     * @return
     * @paramadminUser
     */
    @NotMultiTenancyCheck
    List<SettMsMenuDO> searchUserButtonAll(Map<String, Object> map);

    /**
     * 根据权限Ids查询菜单
     *
     * @param map
     * @return
     */
    @NotMultiTenancyCheck
    List<SettMsMenuDO> readMenuByButtonIds(Map<String, Object> map);

    /**
     * 根据用户权限ids获取菜单
     *
     * @param adminUser
     * @return
     */
    @NotMultiTenancyCheck
    List<SettMsMenuDO> selectMenuByButtonIds(Map<String, Object> adminUser);

    /**
     * 根据用户名称获取菜单
     *
     * @return
     * @paramadminUser
     */
    @NotMultiTenancyCheck
    List<SettMsMenuDO> selectMenuByRoleId(@Param("roleId") Integer roleId);

    /**
     * 通过登录名获取用户
     *
     * @param adminUser
     * @return
     */
    @NotMultiTenancyCheck
    UcenterMsUserDO selectUserByULname(UcenterMsUserDO adminUser);

    /**
     * 通过权限ids获取菜单
     *
     * @param map
     * @return
     */
    @NotMultiTenancyCheck
    List<SettMsMenuDO> selectMenuByButtons(Map<String, Object> map);

    /**
     * 获取所有菜单
     *
     * @return
     */
    @NotMultiTenancyCheck
    List<SettMsMenuDO> selectMenuAll(Map<String, Object> map);

    /**
     * 通过权限ids获取特定集合，main项目调用
     *
     * @param map
     * @return
     */
    @NotMultiTenancyCheck
    List<Map<String, Object>> searchButtons(Map<String, Object> map);

    /**
     * 根据传来的fatherIds查出所有的father信息
     *
     * @param fatherMenuIds
     * @return List<SettMsMenuDO>
     */
    @NotMultiTenancyCheck
    List<SettMsMenuDO> getFatherMenu(@Param("fatherMenuIds") String fatherMenuIds);

    /**
     * 获取所有不是平台账号登陆而且已经根据添加应用过滤之后的菜单
     *
     * @return
     */
    @NotMultiTenancyCheck
    List<SettMsMenuDO> selectNotPlanfromMenuAll(Map<String, Object> map);

    /**
     * 通过有权限的buttonid 查询所有的菜单id集合
     *
     * @param paramMap
     * @return
     */
    @NotMultiTenancyCheck
    List<Integer> findAllHasPermissionId(Map<String, Object> paramMap);

    /**
     * 运营商登录验证
     *
     * @param paramMap
     * @return
     */
    @NotMultiTenancyCheck
    Map<String, Object> checkOperatorLogin(Map<String, Object> paramMap);

    /**
     * 根据admin有权限的菜单
     *
     * @param user SysUser用户
     * @return 用户有权限的 菜单id
     */
    @NotMultiTenancyCheck
    Set<Integer> selectMenuIdByAdmin(UcenterMsUserDO user);

    /**
     * 根据userid获取user有权限的菜单
     *
     * @param roleId 角色id
     * @return 用户有权限的 菜单id
     */
    @NotMultiTenancyCheck
    Set<Integer> selectMenuIdByRoleId(@Param("roleId") Integer roleId);

    /**
     * 根据条件查询用户数
     *
     * @param paramMap 查询条件
     * @return 用户数
     */
    @NotMultiTenancyCheck
    Integer findUserByOrgId(Map<String, Object> paramMap);

    /**
     * 根据用户ID获取用户权限URL
     *
     * @param userId 用户ID
     * @return 用户权限URL列表
     */
    @NotMultiTenancyCheck
    List<String> getPermissionUrlByUserId(@Param("userId") String userId);

    /**
     * 查询所有权限URL
     *
     * @return 用户权限URL列表
     */
    @NotMultiTenancyCheck
    List<String> getPermissionUrlAll();

    /**
     * 根据集团编码获取集团下所有的用户
     *
     * @param groupCode 集团编码
     * @return 集团下所有的用户
     */
    List<SysUserOrgVO> getUserOrgTreeList(@Param("groupCode") String groupCode);


    /**
     * 根据用户组织id
     *
     * @param companyId           公司id
     * @param namespace           命名空间
     * @param permissonMethodCode 权限编码
     * @return
     */
    @NotMultiTenancyCheck
    List<UcenterMsUserDO> getUserByOrgAndPermission(@Param("companyId") String companyId, @Param("namespace") String namespace, @Param("permissonMethodCode") String permissonMethodCode);
}
