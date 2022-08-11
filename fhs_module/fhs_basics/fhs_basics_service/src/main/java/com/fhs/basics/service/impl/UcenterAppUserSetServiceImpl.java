package com.fhs.basics.service.impl;

import com.fhs.basics.po.UcenterAppUserSetPO;
import com.fhs.basics.service.UcenterAppUserSetService;
import com.fhs.basics.vo.UcenterAppUserSetVO;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.cache.annotation.Namespace;
import org.springframework.stereotype.Service;

/**
 * (UcenterAppUserSet)表服务实现类
 *
 * @author miyaxin
 * @since 2022-08-11 09:29:44
 */
@Service
@Namespace("ucenter_app_user_set")
public class UcenterAppUserSetServiceImpl extends BaseServiceImpl<UcenterAppUserSetVO, UcenterAppUserSetPO> implements UcenterAppUserSetService {
    
}
