package com.fhs.basics.service.impl;

import com.fhs.basics.po.SettAlipaySettPO;
import com.fhs.basics.service.SettAlipaySettService;
import com.fhs.basics.vo.SettAlipaySettVO;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.db.ds.DataSource;
import org.springframework.stereotype.Service;

/**
 * 支付宝设置(settAlipaySett)表服务实现类
 *
 * @author wanglei
 * @since 2019-03-19 16:10:29
 */
@Service("settAlipaySettService")
@DataSource("base_business")
public class SettAlipaySettServiceImpl extends BaseServiceImpl<SettAlipaySettVO, SettAlipaySettPO> implements SettAlipaySettService {

}
