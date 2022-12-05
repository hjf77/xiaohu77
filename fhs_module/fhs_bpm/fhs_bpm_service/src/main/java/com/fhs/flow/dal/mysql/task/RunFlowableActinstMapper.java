package com.fhs.flow.dal.mysql.task;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : bruce.liu
 * @projectName : flowable
 * @description: 运行时的节点Dao
 * @date : 2019/12/417:55
 */
@Mapper
public interface RunFlowableActinstMapper {

    /**
     * 删除节点信息
     * @param ids ids
     */
    void deleteRunActinstsByIds(@Param("ids") List<String> ids) ;
}
