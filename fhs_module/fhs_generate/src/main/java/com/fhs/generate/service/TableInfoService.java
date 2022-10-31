package com.fhs.generate.service;

import com.fhs.core.base.service.BaseService;
import com.fhs.generate.po.TableInfoPO;
import com.fhs.generate.vo.TableInfoVO;

/**
 * 表信息service
 */
public interface TableInfoService extends BaseService<TableInfoVO, TableInfoPO>{

    /**
     * 获取表信息
     *
     * @param tableSchema
     * @param tableName
     * @return
     */
    TableInfoVO getTableInfo(String tableSchema, String tableName,String configType);


}
