package com.fhs.basics.mapper;

import com.fhs.basics.po.SettAlipaySettPO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import com.mybatis.jpa.annotation.MapperDefinition;

/**
 * 支付宝设置(settAlipaySett)表数据库访问层
 *
 * @author jackwong
 * @since 2019-03-19 16:10:29
 */
@MapperDefinition(domainClass = SettAlipaySettPO.class, orderBy = " update_time DESC")
public interface SettAlipaySettMapper extends FhsBaseMapper<SettAlipaySettPO> {

}