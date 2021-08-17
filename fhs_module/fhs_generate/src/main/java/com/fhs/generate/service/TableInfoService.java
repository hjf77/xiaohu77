package com.fhs.generate.service;

import com.fhs.generate.vo.TableSearchVO;

public interface TableInfoService {

    TableSearchVO getTableInfo(String dbName,String tableName);
}
