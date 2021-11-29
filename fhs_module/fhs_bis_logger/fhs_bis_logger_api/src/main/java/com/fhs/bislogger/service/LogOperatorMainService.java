package com.fhs.bislogger.service;

import com.fhs.bislogger.po.LogOperatorMainPO;
import com.fhs.bislogger.vo.LogAddOperatorLogVO;
import com.fhs.bislogger.vo.LogOperatorMainVO;
import com.fhs.core.base.service.BaseService;
import com.fhs.easycloud.anno.CloudApi;
import com.fhs.easycloud.anno.CloudMethod;
import java.util.List;
import java.util.Map;

/**
 * 操作日志(LogOperatorMain)}表服务接口
 *
 * @author wanglei
 * @since 2020-04-23 13:59:14
 */
@CloudApi(serviceName = "basic",primary = false)
public interface LogOperatorMainService extends BaseService<LogOperatorMainVO, LogOperatorMainPO> {

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

    /**
     * 添加操作日志
     * @param logAddOperatorLogVO 数据
     */
    @CloudMethod
    void addLog( LogAddOperatorLogVO logAddOperatorLogVO);

}
