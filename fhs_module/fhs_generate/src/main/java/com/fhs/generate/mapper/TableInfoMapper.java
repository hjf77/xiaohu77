package com.fhs.generate.mapper;

import com.fhs.generate.vo.FieldsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableInfoMapper {

    /**
     * 获取表字段
     *
     * @param dbName    库名
     * @param tableName 表名
     * @return
     */
    List<FieldsVO> getTableFields(@Param("dbName") String dbName, @Param("tableName") String tableName);

    /**
     * 获取表注释
     *
     * @param dbName    库名
     * @param tableName 表名
     * @return
     */
    String getTableComment(@Param("dbName") String dbName, @Param("tableName") String tableName);
}
