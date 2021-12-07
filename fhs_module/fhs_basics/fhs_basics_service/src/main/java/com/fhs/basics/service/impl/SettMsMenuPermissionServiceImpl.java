package com.fhs.basics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fhs.basics.po.SettMsMenuPermissionPO;
import com.fhs.basics.mapper.SettMsMenuPermissionMapper;
import com.fhs.basics.service.SettMsMenuPermissionService;
import com.fhs.basics.vo.SettMsMenuPermissionVO;
import com.fhs.core.base.autodel.anno.AutoDel;
import com.fhs.core.base.autodel.anno.AutoDelSett;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.db.ds.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单权限(SettMsMenuPermission)表服务实现类
 *
 * @author user
 * @date 2020-05-18 16:40:36
 */
@Service
@DataSource("base_business")
@AutoDel(mainServiceSetts = {
        @AutoDelSett(namespace = "sett_ms_menu", isChecker = true, field = "menuId", desc = "菜单")
})
public class SettMsMenuPermissionServiceImpl extends BaseServiceImpl<SettMsMenuPermissionVO, SettMsMenuPermissionPO>
        implements SettMsMenuPermissionService {

    @Autowired
    private SettMsMenuPermissionMapper mapper;

    @Override
    public boolean addBaseMenuBatch(Map<String, Object> map) {
        if (map.containsKey("menuId")) {
            List<SettMsMenuPermissionPO> existsButtonList = mapper.selectList(new LambdaQueryWrapper<SettMsMenuPermissionPO>()
                    .eq(SettMsMenuPermissionPO::getMenuId,map.get("menuId")));
            Map<String, Map<String, Object>> baseButtonMap = this.getBaseButton();
            String buttonMethod;
            for (SettMsMenuPermissionPO button : existsButtonList) {
                buttonMethod = button.getPermissionCode();
                if (baseButtonMap.containsKey(buttonMethod)) {
                    baseButtonMap.remove(buttonMethod);
                }
            }
            if (baseButtonMap.size() > 0) {
                map.put("baseButtonList", new ArrayList<Map<String, Object>>(baseButtonMap.values()));
                // 一键添加增删改查菜单
                mapper.addBaseMenuBatch(map);
            }
        }
        return true;
    }

    /**
     * 获取基础权限的数据，组装为map，key为权限，value为权限的信息
     *
     * @return
     */
    private Map<String, Map<String, Object>> getBaseButton() {
        String[][] buttonArray = {
                {"see", "查询", "1"},
                {"add", "添加", "2"},
                {"update", "修改", "3"},
                {"del", "删除", "4"}
        };
        Map<String, Map<String, Object>> resultMap = new HashMap<>();
        Map<String, Object> tempMap;
        for (int i = 0; i < buttonArray.length; i++) {
            tempMap = new HashMap<>();
            tempMap.put("permissionCode", buttonArray[i][0]);
            tempMap.put("permissionName", buttonArray[i][1]);
            tempMap.put("permissionType", buttonArray[i][2]);
            resultMap.put(buttonArray[i][0], tempMap);
        }
        return resultMap;
    }

}
