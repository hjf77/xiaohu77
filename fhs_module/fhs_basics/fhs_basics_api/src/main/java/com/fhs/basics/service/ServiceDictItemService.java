package com.fhs.basics.service;

import com.fhs.basics.po.ServiceDictItemPO;
import com.fhs.basics.vo.ServiceDictItemVO;
import com.fhs.core.base.service.BaseService;
import com.fhs.easycloud.anno.CloudApi;
import com.fhs.easycloud.anno.CloudMethod;

import java.util.List;


/**
 * 字典服务类
 *
 * @author wanglei
 * @version [版本号, 2015年8月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@CloudApi(serviceName = "basic",primary = false)
public interface ServiceDictItemService extends BaseService<ServiceDictItemVO, ServiceDictItemPO> {

    /**
     * 查询数据 参数为object
     * @param bean bean
     * @return 查询出来的数据集合
     */
    @CloudMethod
    List<ServiceDictItemVO> findItemForList(ServiceDictItemPO bean);
}
