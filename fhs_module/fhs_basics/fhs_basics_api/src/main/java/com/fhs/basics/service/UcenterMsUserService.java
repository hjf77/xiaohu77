package com.fhs.basics.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fhs.basics.po.UcenterMsUserPO;
import com.fhs.basics.vo.*;
import com.fhs.common.tree.TreeNode;
import com.fhs.common.tree.Treeable;
import com.fhs.core.base.service.BaseService;
import com.fhs.core.base.vo.FhsPager;
import com.fhs.easycloud.anno.CloudApi;
import com.fhs.easycloud.anno.CloudMethod;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户service
 *
 * @author jianbo.qin
 * @date 2020-05-18 16:38:36
 */
@CloudApi(serviceName = "basic")
public interface UcenterMsUserService extends BaseService<UcenterMsUserVO, UcenterMsUserPO> {

    /**
     * 超管用户标识
     */
    Integer SYS_USER_IS_ADMIN = 1;

    /**
     * 登陆
     *
     * @param adminUser
     * @return
     */
    UcenterMsUserVO login(LoginVO loginVO);

    /**
     * 发送邮件
     *
     * @param adminUser
     */
    void sendMail(UcenterMsUserPO adminUser, String pas);

    /***
     * 获取密码
     *
     * @param userName
     * @return
     */
    String readPass(String userName);

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
    boolean deleteUserRole(UcenterMsUserPO adminUser);

    /**
     * 添加用户信息
     *
     * @param adminUser
     * @return
     */
    Map<String, Object> addUser(UcenterMsUserPO adminUser);

    /**
     * 修改用户信息
     *
     * @param adminUser
     * @return
     */
    boolean updateUser(UcenterMsUserPO adminUser);



    /**
     * 监测原始密码是否正确
     *
     * @return
     * @paramsysUserGroupcode
     */
    boolean validataPass(UcenterMsUserPO adminUser);

    /**
     * 修改用户密码
     *
     * @return
     * @paramsysUserGroupcode
     */
    boolean updatePass(UcenterMsUserPO adminUser);

    /**
     * 根据用户名称获取权限
     *
     * @param adminUser
     * @return
     */
    List<String> selectMenuByUname(UcenterMsUserPO adminUser);

    /**
     * 根据用户名称获取权限
     *
     * @param adminUser,menuState(0:物业的,1:社区的)
     * @return
     */
    List<String> selectMenuByUname(UcenterMsUserPO adminUser, int menuState);

    /**
     * 根据用户id获取权限
     * @param userId
     * @return
     */
    @CloudMethod
    Set<String> findPermissionByUserId(Long userId);

    /**
     * 通过登录名获取用户
     *
     * @param adminUser
     * @return
     */
    UcenterMsUserVO selectUserByULname(UcenterMsUserPO adminUser);

    /**
     * 校验登录名是否存在
     *
     * @return
     * @paramsysUserGroupcode
     */
    boolean validataLoginName(UcenterMsUserPO adminUser);




    /**
     * 获取当前用户的 左侧菜单
     *
     * @param user     当前登录用户
     * @param menuType 菜单类型
     * @return 他有权限的左侧菜单
     */
    List<LeftMenuVO> getMenu(UcenterMsUserPO user, String menuType);

    /**
     * 获取vue用的菜单
     *
     * @param user     用户
     * @param menuType 菜单类型
     * @return vue路由菜单
     */
    List<VueRouterVO> getRouters(UcenterMsUserPO user, String menuType);


    /**
     * 删除用户
     */
    Boolean deleteSysUserById(Long userId);




    /**
     * 根据集团编码获取集团下所有的用户tree
     *
     * @param groupCode 集团编码
     * @return 集团下所有的用户tree
     */
    List<SysUserOrgVO> getUserOrgTreeList(String groupCode);


    /**
     * 根据用户组织id
     *
     * @param companyId           公司id
     * @param namespace           命名空间
     * @param permissonMethodCode 权限编码
     * @return
     */
    List<UcenterMsUserPO> getUserByOrgAndPermission(String companyId, String namespace, String permissonMethodCode);

    /**
     * 获取用户和单位tree
     *
     * @param wrapper 用来过滤用户的
     * @return
     */
    List<TreeNode> getUserCompanyTree(QueryWrapper<UcenterMsUserPO> wrapper);


    List<TreeNode<Treeable>> getOrgTreeByUser(String organizationId, List<UcenterMsOrganizationVO>ucenterMsOrganizationVOS);

    /**
     * 查询组织机构某个角色下的所有用户信息
     * @param paramMap roleId 角色id
     * @param paramMap organizationId 组织机构id
     * @return
     */
    @CloudMethod
    List<UcenterMsUserPO> findOrgRoleList(Map<String, Object> paramMap);


    /**
     * 可以根据单位，角色查询用户列表信息
     * @param page 分页参数
     * @param wrapper 接口参数
     * @return
     */
    @CloudMethod
    IPage<UcenterMsUserVO> advancedPaging(FhsPager<UcenterMsUserVO> page, QueryWrapper<UcenterMsUserVO> wrapper);

    /**
     * 用户在线列表查询
     * @param page
     * @param map
     * @return
     */
    IPage<UcenterMsUserVO> userOnlineList(FhsPager<UcenterMsUserVO> page, Map<String,Object> map);

    /**
     * 查询角色下用户数量
     * @param roleId
     * @return
     */
    int findUserRoleCount(@Param("roleId") int roleId);
}
