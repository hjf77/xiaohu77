package com.fhs.basics.service.impl;

import com.fhs.basics.po.SettMsMenuPO;
import com.fhs.basics.po.SettMsMenuPermissionPO;
import com.fhs.basics.mapper.SettMsMenuMapper;
import com.fhs.basics.service.SettMsMenuPermissionService;
import com.fhs.basics.service.SettMsMenuService;
import com.fhs.basics.vo.SettMsMenuVO;
import com.fhs.basics.vo.TreeMenuPermissionVO;
import com.fhs.basics.vo.UcenterMsOrganizationVO;
import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.core.base.autodel.anno.AutoDel;
import com.fhs.core.base.autodel.anno.AutoDelSett;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.db.ds.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 菜单业务实现类
 *
 * @author Administrator
 * @date 2020-05-18 16:42:36
 */
@Service
@DataSource("base_business")
@AutoDel(mainServiceSetts = {
        @AutoDelSett(namespace = "sett_ms_system", isChecker = true, field = "systemId", desc = "菜单")
})
public class SettMsMenuServiceImpl extends BaseServiceImpl<SettMsMenuVO, SettMsMenuPO> implements SettMsMenuService  {

    @Autowired
    private SettMsMenuMapper mapper;

    @Autowired
    private SettMsMenuPermissionService msMenuPermissionService;



    /**
     * 根据父Id获取树集合
     *
     * @return 返回树节点对象集合
     * @paramparentId 父Id
     */
    @Override
    public List<TreeMenuPermissionVO> getMenuPermissionTree() {
        List<TreeMenuPermissionVO> treeMenuPermissionVOList = mapper.getMenuPermissionTree();
        //找不到爸爸的才会放到此里面
        List<TreeMenuPermissionVO> result = new ArrayList<>();
        Map<String, TreeMenuPermissionVO> map = treeMenuPermissionVOList.stream().collect(Collectors
                .toMap(TreeMenuPermissionVO::getId, Function.identity()));;
        for (TreeMenuPermissionVO tree : treeMenuPermissionVOList) {
            map.put(tree.getId(), tree);
            //找爸爸
            if (map.containsKey(tree.getParentId())) {
                //把树对象 放入到爸爸 List集合里
                map.get(tree.getParentId()).getChildren().add(tree);
            } else {
                result.add(tree);
            }
        }
        return result;
    }



    @Override
    public int deleteById(Serializable id) {
        msMenuPermissionService.deleteBean(SettMsMenuPermissionPO.builder().menuId(ConverterUtils.toString(id)).build());
        return super.deleteById(id);
    }



    @Override
    public int insertSelective(SettMsMenuPO adminMenu) {
        return super.insertSelective(parseUrl(adminMenu));
    }

    private SettMsMenuPO parseUrl(SettMsMenuPO adminMenu){
        String url = adminMenu.getMenuUrl();
        if (!CheckUtils.isNullOrEmpty(url)) {
            if (url.indexOf("\\") != -1) {
                url = url.replaceAll("\\\\", "/");
                adminMenu.setMenuUrl(url);
            }
        }
        return adminMenu;
    }

    @Override
    public int updateSelectiveById(SettMsMenuPO adminMenu) {
        return super.updateSelectiveById(parseUrl(adminMenu));
    }


}
