package com.fhs.generate.mapper;

import com.fhs.core.base.mapper.FhsBaseMapper;
import com.fhs.generate.po.TableInfoPO;
import com.fhs.generate.vo.FieldsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableInfoMapper extends FhsBaseMapper<TableInfoPO> {

    /**
     * 获取表字段
     *
     * @param tableSchema    库名
     * @param tableName 表名
     * @return
     */
    List<FieldsVO> getTableFields(@Param("tableSchema") String tableSchema, @Param("tableName") String tableName);

    /**
     * 获取表注释
     *
     * @param tableSchema    库名
     * @param tableName 表名
     * @return
     */
    String getTableComment(@Param("tableSchema") String tableSchema, @Param("tableName") String tableName);
}
