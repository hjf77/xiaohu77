package com.fhs.basics.mapper;

import com.fhs.basics.po.LogOperatorMainPO;
import com.fhs.basics.vo.LogOperatorMainVO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 操作日志(LogOperatorMain)表数据库访问层
 *
 * @author wanglei
 * @since 2020-04-23 13:59:14
 */
@Repository
public interface LogOperatorMainMapper extends FhsBaseMapper<LogOperatorMainPO> {

    /**
     * 查询模块列表
     *
     * @return
     */
    List<LogOperatorMainVO> getLoggerModelList();


    /**
     * 根据时间段查询数据
     *
     * @param paramMap
     * @return
     */
    List<LogOperatorMainVO> getAccessManyList(Map<String, Object> paramMap);

    /**
     * 根据url查询接口调用多少次
     *
     * @param paramMap
     * @return
     */
    int getLogCount(Map<String, Object> paramMap);

    /**
     * 查询分组后的总记录数
     *
     * @param paramMap
     * @return
     */
    int getReportCount(Map<String, Object> paramMap);

}
