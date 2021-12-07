package com.fhs.basics.mapper;

import com.fhs.basics.po.SettMsMenuPO;
import com.fhs.basics.po.SettMsMenuPermissionPO;
import com.fhs.basics.po.UcenterMsUserPO;
import com.fhs.basics.vo.SysUserOrgVO;
import com.fhs.core.base.mapper.FhsBaseMapper;
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
public interface UcenterMsUserMapper extends FhsBaseMapper<UcenterMsUserPO> {
    /**
     * 用户登录
     */
    UcenterMsUserPO login(UcenterMsUserPO adminUser);

    /**
     * 添加用户角色
     *
     * @param adminUser
     * @return
     */
    int addUserRole(UcenterMsUserPO adminUser);

    /**
     * 查询用户角色
     *
     * @param adminUser
     * @return
     */
    List<Map<String, Object>> searchUserRole(UcenterMsUserPO adminUser);

    /**
     * 删除当前用户的角色
     *
     * @param adminUser
     * @return
     */
    int deleteUserRole(UcenterMsUserPO adminUser);

    /**
     * 根据用户查询菜单
     *
     * @param adminUser
     * @return
     */
    List<SettMsMenuPO> selectMenuByUid(UcenterMsUserPO adminUser);

    /**
     * 根据父节点查询菜单
     *
     * @param map
     * @return
     */
    SettMsMenuPO selectParentMenuById(Map<String, Object> map);

    /**
     * 根据Ids查询菜单
     *
     * @param map
     * @return
     */
    List<SettMsMenuPO> readMenuByIds(Map<String, Object> map);

    /**
     * 监测原始密码是否正确
     *
     * @return
     * @paramsysUserGroupcode
     */
    int validataPass(UcenterMsUserPO adminUser);

    /**
     * 根据登录名获取用户数
     *
     * @return
     * @paramsysUserGroupcode
     */
    int getAdminUserCountByLoginName(UcenterMsUserPO adminUser);

    /**
     * 修改用户密码
     *
     * @return
     * @paramsysUserGroupcode
     */
    int updatePass(UcenterMsUserPO adminUser);

    /**
     * 根据用户查询权限
     *
     * @param adminUser
     * @return
     */
    List<SettMsMenuPermissionPO> searchUserButton(UcenterMsUserPO adminUser);

    /**
     * 根据权限IDs获取权限对象
     *
     * @return
     * @paramadminUser
     */
    List<SettMsMenuPermissionPO> searchUserButtonIds(Map<String, Object> map);

    /**
     * 获取权限对象
     *
     * @return
     * @paramadminUser
     */
    List<SettMsMenuPO> searchUserButtonAll(Map<String, Object> map);

    /**
     * 根据权限Ids查询菜单
     *
     * @param map
     * @return
     */
    List<SettMsMenuPO> readMenuByButtonIds(Map<String, Object> map);

    /**
     * 根据用户权限ids获取菜单
     *
     * @param adminUser
     * @return
     */
    List<SettMsMenuPO> selectMenuByButtonIds(Map<String, Object> adminUser);

    /**
     * 根据用户名称获取菜单
     *
     * @return
     * @paramadminUser
     */
    List<SettMsMenuPO> selectMenuByUname(Map<String, Object> paramMap);

    /**
     * 通过登录名获取用户
     *
     * @param adminUser
     * @return
     */
    UcenterMsUserPO selectUserByULname(UcenterMsUserPO adminUser);

    /**
     * 通过权限ids获取菜单
     *
     * @param map
     * @return
     */
    List<SettMsMenuPO> selectMenuByButtons(Map<String, Object> map);

    /**
     * 获取所有菜单
     *
     * @return
     */
    List<SettMsMenuPO> selectMenuAll(Map<String, Object> map);

    /**
     * 通过权限ids获取特定集合，main项目调用
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> searchButtons(Map<String, Object> map);

    /**
     * 根据传来的fatherIds查出所有的father信息
     *
     * @param fatherMenuIds
     * @return List<SettMsMenuDO>
     */
    List<SettMsMenuPO> getFatherMenu(@Param("fatherMenuIds") String fatherMenuIds);

    /**
     * 获取所有不是平台账号登陆而且已经根据添加应用过滤之后的菜单
     *
     * @return
     */
    List<SettMsMenuPO> selectNotPlanfromMenuAll(Map<String, Object> map);

    /**
     * 通过有权限的buttonid 查询所有的菜单id集合
     *
     * @param paramMap
     * @return
     */
    List<Integer> findAllHasPermissionId(Map<String, Object> paramMap);

    /**
     * 运营商登录验证
     *
     * @param paramMap
     * @return
     */
    Map<String, Object> checkOperatorLogin(Map<String, Object> paramMap);

    /**
     * 根据admin有权限的菜单
     *
     * @param user SysUser用户
     * @return 用户有权限的 菜单id
     */
    List<String> selectMenuIdByAdmin(UcenterMsUserPO user);

    /**
     * 根据userid获取user有权限的菜单
     *
     * @param user 普通用户
     * @return 用户有权限的 菜单id
     */
    List<String> selectMenuIdByUserId(UcenterMsUserPO user);

    /**
     * 根据条件查询用户数
     *
     * @param paramMap 查询条件
     * @return 用户数
     */
    Integer findUserByOrgId(Map<String, Object> paramMap);

    /**
     * 根据用户ID获取用户权限URL
     *
     * @param userId 用户ID
     * @return 用户权限URL列表
     */
    List<String> getPermissionUrlByUserId(@Param("userId") Long userId);

    /**
     * 查询所有权限URL
     *
     * @return 用户权限URL列表
     */
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
    List<UcenterMsUserPO> getUserByOrgAndPermission(@Param("companyId") String companyId, @Param("namespace") String namespace, @Param("permissonMethodCode") String permissonMethodCode);
}
