package com.fhs.generate.mapper;

import com.fhs.generate.vo.FieldsVO;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TableInfoMapper {

    List<FieldsVO> getTableInfo(@Param("dbName")String dbName,@Param("tableName") String tableName);
}
