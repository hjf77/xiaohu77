package com.fhs.bislogger.service;

import com.fhs.bislogger.dox.LogOperatorMainDO;
import com.fhs.bislogger.vo.LogOperatorMainVO;
import com.fhs.core.base.service.BaseService;
import com.fhs.core.cache.annotation.Namespace;

import java.util.List;
import java.util.Map;

/**
 * 操作日志(LogOperatorMain)}表服务接口
 *
 * @author wanglei
 * @since 2020-04-23 13:59:14
 */
@Namespace("log_operator_main")
public interface LogOperatorMainService extends BaseService<LogOperatorMainVO,LogOperatorMainDO>{

    /**
     * 查询模块列表
     * @return
     */
    List<LogOperatorMainVO> getLoggerModelList();


    /**
     * 根据时间段查询数据
     * @param paramMap
     * @return
     */
    List<LogOperatorMainVO> getAccessManyList(Map<String, Object> paramMap);

    /**
     * 根据url查询接口调用多少次
     * @param paramMap
     * @return
     */
    int getLogCount(Map<String, Object> paramMap);

    /**
     * 查询分组后的总记录数
     * @param paramMap
     * @return
     */
    int getReportCount(Map<String, Object> paramMap);

}