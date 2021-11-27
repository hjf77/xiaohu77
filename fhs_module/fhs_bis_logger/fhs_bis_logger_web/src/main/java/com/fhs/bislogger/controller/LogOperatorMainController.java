package com.fhs.bislogger.controller;

import com.fhs.basics.po.UcenterMsUserPO;
import com.fhs.basics.service.UcenterMsUserService;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.bislogger.po.LogHistoryDataPO;
import com.fhs.bislogger.po.LogOperatorExtParamPO;
import com.fhs.bislogger.po.LogOperatorMainPO;
import com.fhs.bislogger.service.LogHistoryDataService;
import com.fhs.bislogger.service.LogOperatorExtParamService;
import com.fhs.bislogger.service.LogOperatorMainService;
import com.fhs.bislogger.vo.LogHistoryDataVO;
import com.fhs.bislogger.vo.LogOperatorExtParamVO;
import com.fhs.bislogger.vo.LogOperatorMainVO;
import com.fhs.core.base.pojo.pager.Pager;
import com.fhs.trans.service.impl.TransService;
import com.fhs.core.valid.checker.ParamChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;

import java.util.*;

/**
 * 操作日志(LogOperatorMain)表控制层
 *
 * @author wanglei
 * @since 2020-04-23 13:59:14
 */

@RestController
@Api(tags = {"操作日志"})
@RequestMapping("/ms/logOperatorMain")
public class LogOperatorMainController extends ModelSuperController<LogOperatorMainVO, LogOperatorMainPO> {

    @Autowired
    private LogOperatorMainService logOperatorMainService;
    @Autowired
    private LogOperatorExtParamService logOperatorExtParamService;
    @Autowired
    private TransService transService;
    @Autowired
    private UcenterMsUserService ucenterMsUserService;
    @Autowired
    private LogHistoryDataService logHistoryDataService;

    /**
     * 模型缓存,用于前端下拉
     */
    private List<LogOperatorMainVO> modelSelectCache = new ArrayList<>();
    /**
     * 用于刷新缓存1个小时刷新一次
     */
    private Date modelSelectCachedTime = new Date();


    /**
     * key namespace value 是module
     */
    private Map<String, String> namespaceModuleMap = new HashMap<>();


    @RequestMapping("/moduleSelect")
    public List<LogOperatorMainVO> getModuleSelect() {
        long time = modelSelectCachedTime.getTime();
        long timeMillis = System.currentTimeMillis();
        double oneHour = 60 * 60 * 1000;
        if (modelSelectCache.isEmpty()) {
            //doto 执行sql
            modelSelectCache = logOperatorMainService.getLoggerModelList();
        } else {
            //判断当前时间和  modelSelectCachedTime 是否大于一个小时,是的话执行刷新缓存
            if ((timeMillis - time) > oneHour) {
                new Thread(() -> {
                    //查询..
                    modelSelectCache = logOperatorMainService.getLoggerModelList();
                    namespaceModuleMap.clear();
                }).start();
            }
        }
        for (LogOperatorMainVO logOperatorMainVO : modelSelectCache) {
            namespaceModuleMap.put(logOperatorMainVO.getNamespace(), logOperatorMainVO.getModel());
        }
        return modelSelectCache;
    }

    /**
     * 根据日志id查询数据
     *
     * @param logId 日志id
     * @return
     */
    @RequestMapping("/getLogger")
    public LogOperatorMainVO getLogger(String logId) {
        LogOperatorMainVO logOperatorMainVO = logOperatorMainService.selectById(logId);
        return logOperatorMainVO;
    }


    /**
     * 扩展参数列表
     *
     * @param mainId 主日志id
     */
    @RequestMapping("/getLoggerList")
    public void getExtendedParameters(String mainId) {
        ParamChecker.isNotNullOrEmpty(mainId, "mainId不能为空");
        List<LogOperatorExtParamVO> logExtParamList =
                logOperatorExtParamService.findForList(LogOperatorExtParamPO.builder().mainId(mainId).build());
        ParamChecker.isNotNull(logExtParamList, "mainId不存在");
        for (LogOperatorExtParamVO logOperatorExtParamVO : logExtParamList) {
            if (namespaceModuleMap != null && namespaceModuleMap.size() > 0) {
                String model = namespaceModuleMap.get(logOperatorExtParamVO.getNamespace());
                logOperatorExtParamVO.setModel(model);
            }
        }
        transService.transMore(logExtParamList);
        super.outJsonp(new Pager(logExtParamList.size(), logExtParamList).asJson());
    }


    /**
     * 查询人员列表
     *
     * @return
     */
    @RequestMapping("/getUserList")
    public List<UcenterMsUserVO> getUserList() {
        List<UcenterMsUserVO> users =
                ucenterMsUserService.findForList(UcenterMsUserPO.builder().build());
        return users;
    }


    /**
     * 根据主键和版本获取数据
     *
     * @param pkey      主键
     * @param version   版本号
     * @param namespace namespace
     * @return
     */
    @RequestMapping("/getLogHistoryData")
    public LogHistoryDataVO getLogHistoryData(String pkey, Integer version, String namespace) {
        LogHistoryDataVO logHistoryData =
                logHistoryDataService.selectBean(LogHistoryDataPO.builder().pkey(pkey).version(version).namespace(namespace).build());
        if (logHistoryData != null) {
            if (namespaceModuleMap != null && namespaceModuleMap.size() > 0) {
                String model = namespaceModuleMap.get(logHistoryData.getNamespace());
                logHistoryData.setModel(model);
            }
        }
        return logHistoryData;
    }

    /**
     * 根据namespace和pkey查询数据列表
     *
     * @param namespace        namespace
     * @param pkey             主键
     * @param logHistoryDataDO
     * @return
     */
    @RequestMapping("/getLogHistoryDataList")
    public Pager<LogHistoryDataVO> getLogHistoryDataList(String namespace, String pkey, LogHistoryDataPO logHistoryDataDO) {
        PageSizeInfo pgeSizeInfo = getPageSizeInfo();
        List<LogHistoryDataVO> logHistoryDataList =
                logHistoryDataService.findForList(LogHistoryDataPO.builder().namespace(namespace).pkey(pkey).build(), pgeSizeInfo.getPageStart(), pgeSizeInfo.getPageSize());
        ParamChecker.isNotNull(logHistoryDataList, "namespace或pkey不存在");
        int countJpa = logHistoryDataService.findCountJpa(logHistoryDataDO);
        return new Pager<>(countJpa, logHistoryDataList);
    }


    /**
     * 根据时间段查询
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    @RequestMapping("/getAccessManyList")
    public Pager<LogOperatorMainVO> getAccessManyList(String startTime, String endTime) {
        Map<String, Object> paramMap = super.getPageTurnNum();
        List<LogOperatorMainVO> accessManyList = null;
        if (startTime != null && endTime != null && startTime != "" && endTime != "") {
            paramMap.put("startTime", startTime);
            paramMap.put("endTime", endTime);
            accessManyList = logOperatorMainService.getAccessManyList(paramMap);
        } else {
            accessManyList = logOperatorMainService.getAccessManyList(paramMap);
        }
        for (LogOperatorMainVO logOperatorMainVO : accessManyList) {
            paramMap.put("url", logOperatorMainVO.getUrl());
            int periodLogCount =
                    logOperatorMainService.getLogCount(paramMap);
            logOperatorMainVO.setVisits(periodLogCount);
        }
        int reportCount = logOperatorMainService.getReportCount(paramMap);
        if (startTime != null && endTime != null && startTime != "" && endTime != "" && reportCount > 20) {
            return new Pager<>(20, accessManyList);
        }
        return new Pager<>(reportCount, accessManyList);
    }

}
