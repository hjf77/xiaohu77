package com.fhs.generate.service.impl;

import com.fhs.generate.mapper.TableInfoMapper;
import com.fhs.generate.service.TableInfoService;
import com.fhs.generate.vo.FieldsVO;
import com.fhs.generate.vo.TableSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TableInfoServiceImpl implements TableInfoService {

    @Autowired
    private TableInfoMapper tableInfoMapper;

    @Override
    public TableSearchVO getTableInfo(String dbName, String tableName) {
        List<FieldsVO> fieldList = tableInfoMapper.getTableInfo(dbName,tableName);
        TableSearchVO tableSearchVO = new TableSearchVO();
        tableSearchVO.setDbName(dbName);
        tableSearchVO.setTableName(tableName);
        tableSearchVO.setList(fieldList);
        return tableSearchVO;
    }
}