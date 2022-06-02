package com.fhs.fee.service.impl;


import com.fhs.fee.vo.FeePlanVO;
import com.fhs.fee.po.FeePlanPO;
import com.fhs.fee.service.FeePlanService;
import org.springframework.stereotype.Service;
import com.fhs.core.db.ds.DataSource;
import com.fhs.core.cache.annotation.Namespace;
import com.fhs.core.base.service.impl.BaseServiceImpl;

/**
 * 费用方案(FeePlan)表服务实现类
 *
 * @author wanglei
 * @since 2022-06-01 15:42:18
 */
@Service
@Namespace("fee_plan")
public class FeePlanServiceImpl extends BaseServiceImpl<FeePlanVO,FeePlanPO> implements FeePlanService {
    
}
