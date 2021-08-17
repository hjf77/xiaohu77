package com.fhs.generate.service;

import com.fhs.generate.vo.TableInfoVO;

/**
 * 表信息service
 */
public interface TableInfoService {

    /**
     * 获取表信息
     * @param dbName
     * @param tableName
     * @return
     */
    TableInfoVO getTableInfo(String dbName, String tableName);
}
