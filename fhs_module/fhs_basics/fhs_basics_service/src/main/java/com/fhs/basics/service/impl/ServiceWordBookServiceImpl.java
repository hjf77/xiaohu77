package com.fhs.basics.service.impl;

import com.fhs.basics.po.ServiceDictItemPO;
import com.fhs.basics.service.ServiceDictItemService;
import com.fhs.basics.vo.ServiceDictItemVO;
import com.fhs.common.constant.Constant;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.db.ds.DataSource;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 字典表服务服务/带翻译
 *
 * @author wanglei
 * @version [版本号, 2015年8月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Primary
@Service
@DataSource("basic")
public class ServiceWordBookServiceImpl extends BaseServiceImpl<ServiceDictItemVO, ServiceDictItemPO> implements ServiceDictItemService {


    @Override
    public List<ServiceDictItemVO> findItemForList(ServiceDictItemPO bean) {
        bean.setIsDelete(Constant.INT_FALSE);
        List<ServiceDictItemPO> dos = baseMapper.selectList(bean.asWrapper());
        return pos2vos(dos);
    }
}
