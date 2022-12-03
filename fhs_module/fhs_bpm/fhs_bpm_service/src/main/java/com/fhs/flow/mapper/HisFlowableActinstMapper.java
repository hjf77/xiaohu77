package com.fhs.flow.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : bruce.liu
 * @projectName : flowable
 * @description: 运行时的节点Dao
 * @date : 2019/12/417:55
 */
@Mapper
@Repository
public interface HisFlowableActinstMapper {

    /**
     * 删除节点信息
     * @param ids ids
     */
    void deleteHisActinstsByIds(List<String> ids) ;
}
