package com.fhs.basics.mapper;

import com.fhs.basics.po.SettMsMenuPO;
import com.fhs.basics.vo.TreeMenuPermissionVO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import com.mybatis.jpa.annotation.MapperDefinition;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 菜单DAO
 *
 * @author jianbo.qin
 * @date 2020-05-18 15:52:33
 */
@Repository
@MapperDefinition(domainClass = SettMsMenuPO.class)
public interface SettMsMenuMapper extends FhsBaseMapper<SettMsMenuPO> {




    /**
     * 找到所有的菜单id不包括root
     *
     * @param paramMap
     * @return
     */
    List<SettMsMenuPO> findAllIdsExcept(Map<String, Object> paramMap);


    /**
     * 查询id,name,namespace列表
     *
     * @return
     */
    List<SettMsMenuPO> findIdAndNameAndNamespaceList();

    /**
     * 获取菜单权限按钮
     *
     * @return
     */
    List<TreeMenuPermissionVO> getMenuPermissionTree();

}
