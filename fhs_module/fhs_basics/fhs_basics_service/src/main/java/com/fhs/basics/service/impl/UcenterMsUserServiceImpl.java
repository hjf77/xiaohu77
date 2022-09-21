package com.fhs.basics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fhs.basics.constant.BaseTransConstant;
import com.fhs.basics.constant.BasicsMenuConstant;
import com.fhs.basics.mapper.SettMsMenuMapper;
import com.fhs.basics.mapper.UcenterMsUserMapper;
import com.fhs.basics.po.UcenterMsTenantPO;
import com.fhs.basics.po.UcenterMsUserPO;
import com.fhs.basics.service.*;
import com.fhs.basics.vo.*;
import com.fhs.common.constant.Constant;
import com.fhs.common.tree.TreeNode;
import com.fhs.common.utils.*;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.config.EConfig;
import com.fhs.core.db.ds.DataSource;
import com.fhs.core.exception.ParamException;
import com.fhs.core.trans.anno.AutoTrans;
import com.fhs.core.valid.checker.ParamChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户(ucenterMsUser)表服务实现类
 *
 * @author user
 * @date 2020-05-18 16:46:36
 */

@Primary
@Service
@DataSource("basic")
@AutoTrans(namespace = BaseTransConstant.USER_INFO, fields = "userName")
public class UcenterMsUserServiceImpl extends BaseServiceImpl<UcenterMsUserVO, UcenterMsUserPO> implements UcenterMsUserService {

    private final int ADMIN = 1;

    /**
     * 显示的菜单
     */
    private static final int SHOW = 0;


    @Value("${fhs.basics.passsalt:fhs}")
    private String passsalt;


    @Autowired
    private UcenterMsUserMapper sysUserMapper;

    @Autowired
    private SettMsMenuMapper sysMenuMapper;

    @Autowired
    private UcenterMsRoleService roleService;


    @Lazy
    @Autowired
    private UcenterMsTenantService tenantService;

    @Lazy
    @Autowired
    private UcenterMsOrganizationService organizationService;


    @Override
    public UcenterMsUserVO login(LoginVO loginVO) {
        UcenterMsUserPO adminUser = sysUserMapper.login(loginVO);
        if (adminUser == null) {
            return null;
        }
        UcenterMsUserVO result = this.selectById(adminUser.getUserId());
        if (result.getOrganizationId() != null) {
            UcenterMsOrganizationVO organization = organizationService.selectById(result.getOrganizationId());
            ParamChecker.isNotNull(organization, "用户所在部门被删除，禁止登陆");
            result.setCompanyId(organization.getCompanyId());
            result.setOrgName(organization.getName());
            if (organization.getCompanyId() != null) {
                organization = organizationService.selectById(organization.getCompanyId());
                ParamChecker.isNotNull(organization, "用户所在企业被删除，禁止登陆");
                result.setCompanyName(organization.getName());
            }
        }
        result.setPassword(null);
        return result;
    }

    @Override
    public void sendMail(UcenterMsUserPO adminUser, String pas) {
        //如果开通要发邮件的话可以写到这里

    }

    @Override
    public String readPass(String userName) {
        return ENCodeUtils.encodeByMD5(userName + passsalt).toLowerCase();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int addUserRole(UcenterMsUserPO adminUser) {
        return sysUserMapper.addUserRole(adminUser);
    }

    @Override
    public List<Map<String, Object>> searchUserRole(UcenterMsUserPO adminUser) {
        return sysUserMapper.searchUserRole(adminUser);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteUserRole(UcenterMsUserPO adminUser) {
        try {
            sysUserMapper.deleteUserRole(adminUser);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String, Object> addUser(UcenterMsUserPO adminUser) {
        int count = 0;
        if (null == adminUser.getUserId()) { //新增
            adminUser.setUserId(idHelper.nextId());
            count = this.insert(adminUser);
        } else {//修改
            count = super.updateById(adminUser);
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if (count > 0) {
            // 添加用户成功时插入当前用户角色
            if (adminUser.getRoleList() != null && adminUser.getRoleList().length > 0) {
                //删除用户角色中间表数据
                deleteUserRole(adminUser);
                //新增用户角色中间表
                addUserRole(adminUser);
            }
            paramMap.put("adminUser", adminUser);
            paramMap.put("retult", true);
            return paramMap;
        }
        paramMap.put("retult", false);
        return paramMap;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean updateUser(UcenterMsUserPO adminUser) {
        if (adminUser.getPassword() == null) {
            adminUser.setPassword(selectById(adminUser.getUserId()).getPassword());
        }
        // 删除原有的角色
        boolean count = deleteUserRole(adminUser);
        if (count) {
            // 修改用户信息
            super.updateById(adminUser);
            if (adminUser.getRoleList().length > 0) {
                // 插入新的用户角色
                int count1 = addUserRole(adminUser);
                return count1 > 0;
            } else {
                return true;
            }
        }
        return false;
    }


    /**
     * 校验密码
     */
    @Override
    public boolean validataPass(UcenterMsUserPO adminUser) {
        if (adminUser.getOldPassword() == null) {
            return false;
        }
        adminUser.setPassword(ENCodeUtils.encodeByMD5(adminUser.getOldPassword()).toLowerCase());
        int count = sysUserMapper.validataPass(adminUser);
        return count > 0;
    }

    /**
     * 验证登录名是否存在
     */
    @Override
    public boolean validataLoginName(UcenterMsUserPO adminUser) {
        int count = sysUserMapper.getAdminUserCountByLoginName(adminUser);
        return count <= 0;
    }

    /**
     * 更新密码
     */
    @Override
    public boolean updatePass(UcenterMsUserPO adminUser) {
        if (adminUser.getOldPassword() == null) {
            return false;
        }
        adminUser.setPassword(adminUser.getOldPassword());
        int count = sysUserMapper.validataPass(adminUser);
        if (count > 0) {
            if (adminUser.getNewPassword() == null) {
                throw new ParamException("新密码不可为空");
            }
            adminUser.setPassword(adminUser.getNewPassword());
            count = sysUserMapper.updatePass(adminUser);
            if (count > 0) {
                // 发送邮件
                sendMail(adminUser, adminUser.getNewPassword());
            }
            return count > 0;
        } else {
            throw new ParamException("密码不正确");
        }
    }


    /**
     * 根据用户获取菜单，授权使用
     */
    @Override
    public List<String> selectMenuByUname(UcenterMsUserPO adminUser) {
        return selectMenuByUname(adminUser, SHOW);
    }

    @Override
    public List<String> selectMenuByUname(UcenterMsUserPO adminUser, int menuState) {
        UcenterMsUserVO tempUser = selectUserByULname(adminUser);
        return new ArrayList<>(findPermissionByUserId(tempUser.getUserId()));
    }

    @Override
    public Set<String> findPermissionByUserId(Long userId) {
        List<SettMsMenuVO> adminMenus = null;
        UcenterMsUserVO tempUser = super.selectById(userId);
        if (tempUser == null) {
            return null;
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        // 管理员时，全查
        if (tempUser.getIsAdmin() == ADMIN) {
            adminMenus = ListUtils.copyListToList(sysMenuMapper.findForAllList(paramMap), SettMsMenuVO.class);
        } else {
            adminMenus = ListUtils.copyListToList(sysUserMapper.selectMenuByUname(paramMap), SettMsMenuVO.class);
        }
        return readPermissionSet(adminMenus);
    }

    private Set<String> readPermissionSet(List<SettMsMenuVO> adminMenus) {
        Set<String> resulstList = new HashSet<>();
        for (SettMsMenuVO item : adminMenus) {
            resulstList.add(item.getNamespace());
        }
        return resulstList;
    }


    @Override
    public UcenterMsUserVO selectUserByULname(UcenterMsUserPO adminUser) {
        return p2v(sysUserMapper.selectUserByULname(adminUser));
    }


    @Override
    public List<LeftMenuVO> getMenu(UcenterMsUserPO user, String menuType) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        //如果是saas模式需要判断菜单类型
        if (ConverterUtils.toBoolean(EConfig.getOtherConfigPropertiesValue("isSaasModel"))) {
            //如果不是运营者的集团编码，只能是租户，如果是运营者的编码可以按照参数请求的来
            if (EConfig.getOtherConfigPropertiesValue("operator_group_code").equals(user.getGroupCode())) {
                paramMap.put("menuType", menuType);
            } else {
                paramMap.put("menuType", SettMsMenuService.MENU_TYPE_TENANT);
                if (ConverterUtils.toBoolean(EConfig.getOtherConfigPropertiesValue("isSaasModel"))) {
                    String systemIds = tenantService.selectBean(UcenterMsTenantPO.builder().groupCode(user.getGroupCode()).build()).getSystemIds();
                    if (systemIds != null) {
                        paramMap.put("systemIds", StringUtils.getStrToIn(systemIds.split(",")));
                    }
                }
            }
        }
        List<SettMsMenuVO> menuList = ListUtils.copyListToList(sysUserMapper.selectMenuAll(paramMap), SettMsMenuVO.class);
        menuList = menuFilter(user, menuList);
        Map<String, LeftMenuVO> leftMenuMap = new HashMap<>();
        // 遍历AdminMenu转换为LeftMenu
        menuList.forEach(adminMenu -> {
            LeftMenuVO leftMenu = LeftMenuVO.builder().id(adminMenu.getMenuId()).name(adminMenu.getMenuName())
                    .icon(adminMenu.getIcon())
                    .namespace(adminMenu.getNamespace()).url(adminMenu.getMenuUrl()).sonMenu(new ArrayList<>()).build();
            leftMenuMap.put(leftMenu.getId(), leftMenu);
        });
        List<LeftMenuVO> result = new ArrayList<>();
        menuList.forEach(adminMenu -> {
            if (ConverterUtils.toInt(adminMenu.getIsShow()) != SettMsMenuService.NOT_SHOW) {
                // 如果不是null 也不是root则找爸爸吧自己添加到爸爸的儿子里面去
                if (adminMenu.getFatherMenuId() != null && (!BasicsMenuConstant.MENU_ROOT_STR.equals(adminMenu.getFatherMenuId()))) {
                    if (leftMenuMap.containsKey(adminMenu.getFatherMenuId())) {
                        leftMenuMap.get(adminMenu.getFatherMenuId()).getSonMenu().add(
                                leftMenuMap.get(adminMenu.getMenuId()));
                    }
                }
                // 如果是一级菜单则挂写到result去
                else if (adminMenu.getFatherMenuId() != null && BasicsMenuConstant.MENU_ROOT_STR.equals(adminMenu.getFatherMenuId())) {
                    result.add(leftMenuMap.get(adminMenu.getMenuId()));
                }
            }
        });
        return result;
    }

    @Override
    public List<VueRouterVO> getRouters(UcenterMsUserPO user, String menuType) {
        List<LeftMenuVO> menus = getMenu(user, menuType);
        List<VueRouterVO> result = new ArrayList<>();
        VueRouterVO tempRouter = null;
        for (LeftMenuVO menu : menus) {
            tempRouter = new VueRouterVO();
            converterMenu2Router(menu, tempRouter, true);
            result.add(tempRouter);
        }
        return result.stream().sorted(Comparator.comparing(VueRouterVO::getOrderIndex)).collect(Collectors.toList());
    }

    /**
     * 转换leftMenu到vueRouter
     *
     * @param menu        菜单
     * @param vueRouterVO vue 路由
     * @param isFirst     是否是一级菜单
     */
    private void converterMenu2Router(LeftMenuVO menu, VueRouterVO vueRouterVO, boolean isFirst) {
        vueRouterVO.setName(menu.getNamespace());
        vueRouterVO.setAlwaysShow(isFirst);
        vueRouterVO.setPath(isFirst ? "/" + menu.getNamespace() : menu.getNamespace());
        String component = null;
        component = menu.getUrl();
        vueRouterVO.setComponent(isFirst ? "Layout" : component);
        vueRouterVO.setOrderIndex(menu.getOrderIndex());
        vueRouterVO.setRedirect(isFirst ? "noRedirect" : null);
        vueRouterVO.getMeta().put("title", menu.getName());
        vueRouterVO.getMeta().put("icon", menu.getIcon());
        if (menu.getSonMenu() != null && !menu.getSonMenu().isEmpty()) {
            for (LeftMenuVO sonMenu : menu.getSonMenu()) {
                VueRouterVO sunRouter = new VueRouterVO();
                converterMenu2Router(sonMenu, sunRouter, false);
                vueRouterVO.getChildren().add(sunRouter);
            }
        }
    }

    /**
     * 从所有的菜单找到用户拥有权限的
     *
     * @param user
     * @param menuList
     * @return
     */
    private List<SettMsMenuVO> menuFilter(UcenterMsUserPO user, List<SettMsMenuVO> menuList) {
        List<String> userMenuIds = null;
        if (user.getIsAdmin() == ADMIN) {
            userMenuIds = sysUserMapper.selectMenuIdByAdmin(user);
        } else {
            userMenuIds = sysUserMapper.selectMenuIdByUserId(user);
        }

        Map<String, SettMsMenuVO> menuMap = new HashMap<>();
        menuList.forEach(menu -> {
            menuMap.put(menu.getMenuId(), menu);
        });
        // 已经添加进结果的菜单
        Set<SettMsMenuVO> hasAddMenu = new LinkedHashSet<>();
        for (String userMenuId : userMenuIds) {
            if (menuMap.containsKey(userMenuId)) {
                hasAddMenu.add(menuMap.get(userMenuId));
                // 能看到儿子就能看到爸爸，找儿子的爸爸
                initFather(hasAddMenu, menuMap, menuMap.get(userMenuId));
            }
        }
        List<SettMsMenuVO> result = new ArrayList<>();
        result.addAll(hasAddMenu);
        return result;
    }

    /**
     * 找一个菜单的爸爸，保证拥有子菜单 可以显示出父菜单
     *
     * @param hasAddMenu
     * @param menuMap
     * @param sysMenu
     */
    private void initFather(Set<SettMsMenuVO> hasAddMenu, Map<String, SettMsMenuVO> menuMap, SettMsMenuVO sysMenu) {
        if (menuMap.containsKey(sysMenu.getFatherMenuId())) {
            SettMsMenuVO father = menuMap.get(sysMenu.getFatherMenuId());
            if (!hasAddMenu.contains(father)) {
                hasAddMenu.add(father);
                // 很愉快的找打爸爸后接着网上找 找father的爸爸
                initFather(hasAddMenu, menuMap, father);
            }
        }
    }

    /**
     * @param userId 用户id
     * @return 用户信息
     * @desc 根据用户id获取用户信息
     */
    @Override
    public UcenterMsUserVO selectById(Serializable userId) {
        //根据id获取用户信息
        UcenterMsUserVO sysUser = super.selectById(userId);
        //根据用户id获取当前用户的角色
        List<Map<String, Object>> sysUserRoleList = this.searchUserRole(sysUser);
        if (sysUserRoleList.size() > 0) {
            Vector<String> roleVectorList = new Vector<>();
            for (int i = 0; i < sysUserRoleList.size(); i++) {
                Map<String, Object> map = sysUserRoleList.get(i);
                roleVectorList.add(map.get("roleId").toString());
            }
            String[] roleList = new String[roleVectorList.size()];
            roleVectorList.toArray(roleList);
            sysUser.setRoleList(roleList);
            sysUser.setRoleIds(StringUtils.getStrForIn(roleVectorList, false));
        } else {
            sysUser.setRoleList(new String[0]);
        }
        return sysUser;
    }

    /**
     * @param userId 要删除的用户de id
     * @return 删除是否成功
     * @desc 根据id删除用户
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Boolean deleteSysUserById(Long userId) {
        this.deleteById(userId);
        UcenterMsUserPO sysUser = new UcenterMsUserPO();
        sysUser.setUserId(userId);
        return this.deleteUserRole(sysUser);
    }


    @Override
    public List<SysUserOrgVO> getUserOrgTreeList(String groupCode) {
        List<SysUserOrgVO> dbRecord = sysUserMapper.getUserOrgTreeList(groupCode);

        //找不到爸爸的才会放到此里面
        List<SysUserOrgVO> result = new ArrayList<>();

        Map<String, SysUserOrgVO> fatherDTO = new HashMap<>();
        for (SysUserOrgVO user : dbRecord) {
            fatherDTO.put(user.getId(), user);
            if (fatherDTO.containsKey(user.getParentId())) {
                fatherDTO.get(user.getParentId()).getChildren().add(user);
            } else {
                result.add(user);
            }
        }
        return result;
    }


    @Override
    public List<UcenterMsUserPO> getUserByOrgAndPermission(String companyId, String namespace, String permissonMethodCode) {
        List<UcenterMsUserPO> result = sysUserMapper.getUserByOrgAndPermission(companyId, namespace, permissonMethodCode);
        List<UcenterMsUserVO> adminUsers = super.selectListMP(new LambdaQueryWrapper<UcenterMsUserPO>().eq(UcenterMsUserPO::getIsAdmin, Constant.INT_TRUE));
        //把admin也加入进来
        if (!adminUsers.isEmpty()) {
            result.addAll(adminUsers);
        }
        // 按userId删除重复用户
        List<UcenterMsUserPO> userList = result.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(UcenterMsUserPO::getUserId))), ArrayList::new));
        return userList;
    }


    private List<String> getPermissionUrlByUserId(Long userId) {
        return sysUserMapper.getPermissionUrlByUserId(userId);
    }


    public List<TreeNode> getUserCompanyTree(QueryWrapper<UcenterMsUserPO> wrapper) {
        List<UcenterMsUserVO> users = super.selectListMP(wrapper);
        List<UcenterMsOrganizationVO> orgs = this.organizationService.selectListMP(new LambdaQueryWrapper<>());
        Map<String, UcenterMsOrganizationVO> orgMap = orgs.stream().collect(Collectors
                .toMap(UcenterMsOrganizationVO::getId, Function.identity()));
        Map<String, TreeNode> nodeMap = new HashMap<>();
        List<TreeNode> result = new ArrayList<>();
        for (UcenterMsOrganizationVO org : orgs) {
            nodeMap.put(org.getId(), TreeNode.builder().name(org.getName()).id(org.getId()).parentId(org.getParentId()).data(org).children(new ArrayList<>()).build());
        }
        Map<String, List<UcenterMsUserVO>> userOrgMap = users.stream().collect(Collectors.groupingBy(UcenterMsUserVO::getOrganizationId));
        String companyId = null;
        for (UcenterMsOrganizationVO org : orgs) {
            if (CheckUtils.isNullOrEmpty(org.getParentId())) {
                result.add(nodeMap.get(org.getId()));
                companyId = org.getId();
                //如果是个组织则找我爸爸的公司id
            } else if (org.getIsCompany() != null && Constant.INT_TRUE == org.getIsCompany() && orgMap.containsKey(org.getParentId())) {
                nodeMap.get(orgMap.get(org.getParentId()).getCompanyId()).getChildren().add(nodeMap.get(org.getId()));
                companyId = org.getId();
            } else {
                //如果只是个普通的部门则取部门id
                companyId = org.getCompanyId();
            }
            //找到当前用户
            if (userOrgMap.containsKey(org.getId())) {
                List<UcenterMsUserVO> orgUser = userOrgMap.get(org.getId());
                for (UcenterMsUserVO ucenterMsUserVO : orgUser) {
                    nodeMap.get(companyId).getChildren().add(TreeNode.builder().name(ucenterMsUserVO.getUserName() + "(用户)(" + ucenterMsUserVO.getTransMap().get("orgName") + ")")
                            .id(ucenterMsUserVO.getUserId().toString()).parentId(companyId).data(ucenterMsUserVO).children(new ArrayList<>()).build());
                }
            }

        }
        return result;
    }


    private List<String> getPermissionUrlAll() {
        return sysUserMapper.getPermissionUrlAll();
    }


}
