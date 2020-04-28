package com.fhs.bislogger.service.impl;

import com.fhs.bislogger.api.rpc.FeignBisLoggerApiService;
import com.fhs.bislogger.constant.LoggerConstant;
import com.fhs.bislogger.dox.LogHistoryDataDO;
import com.fhs.bislogger.dox.LogOperatorExtParamDO;
import com.fhs.bislogger.dox.LogOperatorMainDO;
import com.fhs.bislogger.service.LogHistoryDataService;
import com.fhs.bislogger.service.LogOperatorExtParamService;
import com.fhs.bislogger.vo.LogAddOperatorLogVO;
import com.fhs.bislogger.vo.LogHistoryDataVO;
import com.fhs.bislogger.vo.LogOperatorExtParamVO;
import com.fhs.bislogger.vo.LogOperatorMainVO;
import com.fhs.bislogger.mapper.LogOperatorMainMapper;
import com.fhs.bislogger.service.LogOperatorMainService;
import com.fhs.common.utils.ListUtils;
import com.fhs.core.db.ds.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * (LogOperatorMain)表服务实现类
 *
 * @author wanglei
 * @since 2020-04-23 13:59:14
 */
@Service
@DataSource("log")
public class LogOperatorMainServiceImpl extends BaseServiceImpl<LogOperatorMainVO, LogOperatorMainDO> implements LogOperatorMainService, FeignBisLoggerApiService {

    @Autowired
    private LogOperatorExtParamService logOperatorExtParamService;

    @Autowired
    private LogHistoryDataService logHistoryDataService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addLog(LogAddOperatorLogVO logAddOperatorLogVO) {
        super.insertSelective(logAddOperatorLogVO.getOperatorMainVO());
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
                }
            }
            extParamVO.preInsert(logAddOperatorLogVO.getOperatorMainVO().getCreateUser());
        }
        logOperatorExtParamService.batchInsert(ListUtils.copyListToPararentList(
                logAddOperatorLogVO.getOperatorExtParamVOList(), LogOperatorExtParamDO.class));
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
                LogHistoryDataDO.class));
    }
}