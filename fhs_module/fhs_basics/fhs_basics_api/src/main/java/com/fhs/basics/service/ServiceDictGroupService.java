package com.fhs.basics.service;

import com.fhs.basics.po.ServiceDictGroupPO;
import com.fhs.basics.vo.ServiceDictGroupVO;
import com.fhs.core.base.service.BaseService;

/**
 * 字典类型
 *
 * @author nanshouxiao
 * @version [版本号, 2015/12/22 15:13:20]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface ServiceDictGroupService extends BaseService<ServiceDictGroupVO, ServiceDictGroupPO> {
    /**
     * 刷新redis缓存
     *
     * @return
     */
    boolean refreshRedisCache(String groupCode);
}
