package com.fhs.fee.mapper;

import com.fhs.fee.po.FeePlanPO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 费用方案(FeePlan)表数据库访问层
 *
 * @author wanglei
 * @since 2022-06-01 15:42:19
 */
@Repository
public interface FeePlanMapper extends FhsBaseMapper<FeePlanPO> {

}
