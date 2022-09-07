package com.fhs.generate.service.impl;


import com.fhs.generate.vo.SystemTableGenerateConfigVO;
import com.fhs.generate.po.SystemTableGenerateConfigPO;
import com.fhs.generate.service.SystemTableGenerateConfigService;
import org.springframework.stereotype.Service;
import com.fhs.core.db.ds.DataSource;
import com.fhs.core.cache.annotation.Namespace;
import com.fhs.core.base.service.impl.BaseServiceImpl;

/**
 * 表生成代码配置(SystemTableGenerateConfig)表服务实现类
 *
 * @author wanglei
 * @since 2022-09-07 14:50:21
 */
@Service
@Namespace("system_table_generate_config")
public class SystemTableGenerateConfigServiceImpl extends BaseServiceImpl<SystemTableGenerateConfigVO,SystemTableGenerateConfigPO> implements SystemTableGenerateConfigService {
    
}
