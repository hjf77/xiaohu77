package com.fhs.bislogger.controller;

import com.fhs.basics.dox.UcenterMsUserDO;
import com.fhs.basics.service.UcenterMsUserService;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.bislogger.dox.LogHistoryDataDO;
import com.fhs.bislogger.dox.LogOperatorExtParamDO;
import com.fhs.bislogger.dox.LogOperatorMainDO;
import com.fhs.bislogger.service.LogHistoryDataService;
import com.fhs.bislogger.service.LogOperatorExtParamService;
import com.fhs.bislogger.service.LogOperatorMainService;
import com.fhs.bislogger.vo.LogHistoryDataVO;
import com.fhs.bislogger.vo.LogOperatorExtParamVO;
import com.fhs.bislogger.vo.LogOperatorMainVO;
import com.fhs.core.base.pojo.pager.Pager;
import com.fhs.core.trans.service.impl.TransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 操作日志(LogOperatorMain)表控制层
 *
 * @author wanglei
 * @since 2020-04-23 13:59:14
 */

@RestController
@Api(tags = {""})
@RequestMapping("/ms/logOperatorMain")
public class LogOperatorMainController extends ModelSuperController<LogOperatorMainVO, LogOperatorMainDO> {

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
    public List<LogOperatorMainVO> getModuleSelect(){
        long time = modelSelectCachedTime.getTime();
        long timeMillis = System.currentTimeMillis();
        double oneHour = 60 * 60 * 1000;
        if(modelSelectCache.isEmpty()){
            //doto 执行sql
            modelSelectCache = logOperatorMainService.getLoggerModelList();
        }else{
            //判断当前时间和  modelSelectCachedTime 是否大于一个小时,是的话执行刷新缓存
            if ((timeMillis-time)>oneHour){
                new Thread(()->{
                    //查询..
                    modelSelectCache = logOperatorMainService.getLoggerModelList();
                    namespaceModuleMap.clear();
                }).start();
            }
        }
        for (LogOperatorMainVO logOperatorMainVO : modelSelectCache) {
            namespaceModuleMap.put(logOperatorMainVO.getNamespace(),logOperatorMainVO.getModel());
        }
        return modelSelectCache;
    }

    /**
     * 根据日志id查询日志
     * @param logId
     * @return
     */
    @RequestMapping("/getLogger")
    public LogOperatorMainVO getLogger(String logId){
        LogOperatorMainVO logOperatorMainVO = logOperatorMainService.selectById(logId);
        return logOperatorMainVO;
    }

    /**
     * 扩展参数列表
     */
    @RequestMapping("/getLoggerList")
    public void getExtendedParameters(String mainId){
        List<LogOperatorExtParamVO> logExtParamList =
                logOperatorExtParamService.findForList(LogOperatorExtParamDO.builder().mainId(mainId).build());
        if (logExtParamList != null && logExtParamList.size()>0){
            for (LogOperatorExtParamVO logOperatorExtParamVO : logExtParamList) {
                if (namespaceModuleMap!=null && namespaceModuleMap.size()>0){
                    String model = namespaceModuleMap.get(logOperatorExtParamVO.getNamespace());
                    logOperatorExtParamVO.setModel(model);
                }
            }
        }
        transService.transMore(logExtParamList);
        super.outJsonp(new Pager(logExtParamList.size(), logExtParamList).asJson());
    }

    /**
     * 查询人员列表
     * @param request
     * @return
     */
    @RequestMapping("/getUserList")
    public List<UcenterMsUserVO> getUserList(HttpServletRequest request){
        PageSizeInfo pageSizeInfo = super.getPageSizeInfo();
        UcenterMsUserDO queryParam = UcenterMsUserDO.builder().userName(request.getParameter("userName")).organizationId(request.getParameter("orgId")).build();
        List<UcenterMsUserVO> users = ucenterMsUserService.selectPage(queryParam,
                pageSizeInfo.getPageStart(),pageSizeInfo.getPageSize());
        return users;
    }

    /**
     * 根据主键和版本获取数据
     * @param pkey
     * @param version
     * @return
     */
    @RequestMapping("/getLogHistoryData")
    public LogHistoryDataVO getLogHistoryData(String pkey, Integer version){
        LogHistoryDataVO logHistoryData =
                logHistoryDataService.selectBean(LogHistoryDataDO.builder().pkey(pkey).version(version).build());
        if (logHistoryData != null){
            if (namespaceModuleMap!=null && namespaceModuleMap.size()>0){
                String model = namespaceModuleMap.get(logHistoryData.getNamespace());
                logHistoryData.setModel(model);
            }
        }
        return logHistoryData;
    }

    /**
     * 根据namespace和pkey查询数据列表
     * @param nameSpace
     * @param pkey
     * @param logHistoryDataDO
     * @return
     */
    @RequestMapping("/getLogHistoryDataList")
    public Pager<LogHistoryDataVO> getLogHistoryDataList(String nameSpace,String pkey,LogHistoryDataDO logHistoryDataDO){
        PageSizeInfo pgeSizeInfo = getPageSizeInfo();
        List<LogHistoryDataVO> logHistoryDataList =
                logHistoryDataService.findForList(LogHistoryDataDO.builder().namespace(nameSpace).pkey(pkey).build(),pgeSizeInfo.getPageStart(), pgeSizeInfo.getPageSize());
        int countJpa = logHistoryDataService.findCountJpa(logHistoryDataDO);
        return new Pager<>(countJpa,logHistoryDataList);
    }



    /**
     * 根据时间段查询
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping("/getAccessManyList")
    public Pager<LogOperatorMainVO> getAccessManyList(String startTime,String endTime){
        Map<String, Object> paramMap = super.getPageTurnNum();
        List<LogOperatorMainVO> accessManyList = null;
        if (startTime!=null && endTime!=null && startTime!="" && endTime!=""){
            paramMap.put("startTime",startTime);
            paramMap.put("endTime",endTime);
            accessManyList = logOperatorMainService.getAccessManyList(paramMap);
        }else {
            accessManyList = logOperatorMainService.getAccessManyList(paramMap);
        }
        for (LogOperatorMainVO logOperatorMainVO : accessManyList) {
            paramMap.put("url",logOperatorMainVO.getUrl());
            int periodLogCount =
                    logOperatorMainService.getLogCount(paramMap);
            logOperatorMainVO.setVisits(periodLogCount);
        }
        int reportCount = logOperatorMainService.getReportCount(paramMap);
        if (startTime!=null && endTime!=null && startTime!="" && endTime!="" && reportCount>20){
            return new Pager<>(20,accessManyList);
        }
        return new Pager<>(reportCount,accessManyList);
    }

}