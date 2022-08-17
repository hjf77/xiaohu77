package com.fhs.basics.service.impl;

import com.fhs.basics.po.ServiceCountryPO;
import com.fhs.basics.service.ServiceCountryService;
import com.fhs.basics.vo.ServiceCountryVO;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.cache.annotation.Namespace;
import org.springframework.stereotype.Service;

/**
 * (ServiceCountry)表服务实现类
 *
 * @author miyaxin
 * @since 2022-08-17 17:18:34
 */
@Service
@Namespace("service_country")
public class ServiceCountryServiceImpl extends BaseServiceImpl<ServiceCountryVO, ServiceCountryPO> implements ServiceCountryService {
    
}
