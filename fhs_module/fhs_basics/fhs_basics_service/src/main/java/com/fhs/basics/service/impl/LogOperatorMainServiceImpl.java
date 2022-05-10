package com.fhs.basics.service.impl;

import com.fhs.basics.constant.LoggerConstant;
import com.fhs.basics.po.LogHistoryDataPO;
import com.fhs.basics.po.LogOperatorExtParamPO;
import com.fhs.basics.po.LogOperatorMainPO;
import com.fhs.basics.service.LogHistoryDataService;
import com.fhs.basics.service.LogOperatorExtParamService;
import com.fhs.basics.vo.LogAddOperatorLogVO;
import com.fhs.basics.vo.LogHistoryDataVO;
import com.fhs.basics.vo.LogOperatorExtParamVO;
import com.fhs.basics.vo.LogOperatorMainVO;
import com.fhs.basics.mapper.LogOperatorMainMapper;
import com.fhs.basics.service.LogOperatorMainService;
import com.fhs.common.utils.ListUtils;
import com.fhs.core.cache.annotation.Namespace;
import com.fhs.core.db.ds.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 操作日志(LogOperatorMain)表服务实现类
 *
 * @author wanglei
 * @since 2020-04-23 13:59:14
 */
@Service
@DataSource("basic")
@Namespace("log_operator_main")
public class LogOperatorMainServiceImpl extends BaseServiceImpl<LogOperatorMainVO, LogOperatorMainPO> implements LogOperatorMainService{

    @Autowired
    private LogOperatorExtParamService logOperatorExtParamService;

    @Autowired
    private LogHistoryDataService logHistoryDataService;

    @Autowired
    private LogOperatorMainMapper logOperatorMainMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addLog(LogAddOperatorLogVO logAddOperatorLogVO) {
        //添加或者修改的时候,如果只有一个,history,则吧history放到reqparam中
        if ((logAddOperatorLogVO.getOperatorMainVO().getType() == LoggerConstant.METHOD_TYPE_UPATE
                || logAddOperatorLogVO.getOperatorMainVO().getType() == LoggerConstant.METHOD_TYPE_ADD)
                && logAddOperatorLogVO.getHistoryDataVOList() != null && logAddOperatorLogVO.getHistoryDataVOList().size() == 1) {
            logAddOperatorLogVO.getOperatorMainVO().setReqParam(logAddOperatorLogVO.getHistoryDataVOList().get(0).getData());
        }
        //返回值最多保留6000个字符
        if (logAddOperatorLogVO.getOperatorMainVO().getRespBody() != null && logAddOperatorLogVO.getOperatorMainVO().getRespBody().length() > 6000) {
            logAddOperatorLogVO.getOperatorMainVO().setRespBody(logAddOperatorLogVO.getOperatorMainVO().getRespBody().substring(0, 6000));
        }
        super.insert(logAddOperatorLogVO.getOperatorMainVO());
        Map<String, LogHistoryDataVO> hisMap = new HashMap<>();
        for (LogOperatorExtParamVO extParamVO : logAddOperatorLogVO.getOperatorExtParamVOList()) {
            if (extParamVO.getOperatorType() != null) {
                LogHistoryDataVO historyDataVO = extParamVO.getOperatorType() == LoggerConstant.OPERATOR_TYPE_ADD
                        ? null : logHistoryDataService.getLastVersionData(extParamVO.getPkey(), extParamVO.getNamespace());
                hisMap.put(extParamVO.getNamespace() + "_" + extParamVO.getPkey(), historyDataVO);
                switch (extParamVO.getOperatorType()) {
                    //添加的默认version是0
                    case LoggerConstant.OPERATOR_TYPE_ADD:
                        extParamVO.setVersion(1);
                        break;
                    case LoggerConstant.OPERATOR_TYPE_UPDATE:
                        extParamVO.setVersion(historyDataVO == null ? 1 : historyDataVO.getVersion() + 1);
                        break;
                    case LoggerConstant.OPERATOR_TYPE_DEL:
                        extParamVO.setVersion(historyDataVO == null ? 0 : historyDataVO.getVersion() + 1);
                        break;
                    default:
                        break;
                }
            }
            extParamVO.preInsert(logAddOperatorLogVO.getOperatorMainVO().getCreateUser());
        }
        logOperatorExtParamService.batchInsert(ListUtils.copyListToPararentList(
                logAddOperatorLogVO.getOperatorExtParamVOList(), LogOperatorExtParamPO.class));
        LogHistoryDataVO oldHis = null;
        for (LogHistoryDataVO historyDataVO : logAddOperatorLogVO.getHistoryDataVOList()) {
            if (hisMap.containsKey(historyDataVO.getNamespace() + "_" + historyDataVO.getPkey())) {
                oldHis = hisMap.get(historyDataVO.getNamespace() + "_" + historyDataVO.getPkey());
            } else {
                oldHis = logHistoryDataService.getLastVersionData(historyDataVO.getPkey(), historyDataVO.getNamespace());
            }
            historyDataVO.preInsert(logAddOperatorLogVO.getOperatorMainVO().getCreateUser());
            historyDataVO.setVersion(oldHis == null ? 1 : (oldHis.getVersion() + 1));
        }
        logHistoryDataService.batchInsert(ListUtils.copyListToPararentList(logAddOperatorLogVO.getHistoryDataVOList(),
                LogHistoryDataPO.class));
    }

    @Override
    public List<LogOperatorMainVO> getLoggerModelList() {
        List<LogOperatorMainVO> loggerModelList =
                logOperatorMainMapper.getLoggerModelList();
        return loggerModelList;
    }

    @Override
    public List<LogOperatorMainVO> getAccessManyList(Map<String, Object> paramMap) {
        List<LogOperatorMainVO> accessManyList =
                logOperatorMainMapper.getAccessManyList(paramMap);
        return accessManyList;
    }

    @Override
    public int getLogCount(Map<String, Object> paramMap) {
        int logCount = logOperatorMainMapper.getLogCount(paramMap);
        return logCount;
    }

    @Override
    public int getReportCount(Map<String, Object> paramMap) {
        int reportCount = logOperatorMainMapper.getReportCount(paramMap);
        return reportCount;
    }
}
