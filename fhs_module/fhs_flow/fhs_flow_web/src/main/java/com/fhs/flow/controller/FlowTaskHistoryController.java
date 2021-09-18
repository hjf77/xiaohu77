package com.fhs.flow.controller;

import com.fhs.bislogger.api.anno.LogMethod;
import com.fhs.bislogger.api.anno.LogNamespace;
import com.fhs.bislogger.constant.LoggerConstant;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.common.utils.DateUtils;
import com.fhs.core.base.pojo.pager.Pager;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.service.impl.TransService;
import com.fhs.core.valid.checker.ParamChecker;
import com.fhs.flow.constant.FlowConstant;
import com.fhs.flow.dox.FlowTaskHistoryDO;
import com.fhs.flow.service.FlowTaskHistoryService;
import com.fhs.flow.vo.FlowTaskHistoryVO;
import com.fhs.flow.vo.TaskHistoryVO;
import com.fhs.module.base.controller.ModelSuperController;
import io.swagger.annotations.Api;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 流程任务日志(FlowTaskHistory)表控制层
 *
 * @author jackwong
 * @since 2019-11-12 14:40:34
 */
@RestController
@Api(tags = {"流程任务日志"})
@RequestMapping("/ms/flowTaskHistory")
@LogNamespace(namespace = "flow_task_history", module = "流程任务日志管理")
public class FlowTaskHistoryController extends ModelSuperController<FlowTaskHistoryVO, FlowTaskHistoryDO> {

    @Autowired
    private FlowTaskHistoryService flowTaskHistoryService;

    @Autowired
    private TransService transService;

    @Autowired
    private HistoryService historyService;

    /**
     * 办理历史
     *
     * @param request
     * @return
     */
    @RequestMapping("getTaskHistoryList")
    @LogMethod
    public void getTaskHistoryList(HttpServletRequest request) {
        Map<String, Object> paramMap = super.getPageTurnNum();
        paramMap.put("userId", this.getSessionuser().getUserId());
        List<TaskHistoryVO> alreadyDoneList = flowTaskHistoryService.findTaskHistoryList(paramMap);
        alreadyDoneList.forEach(alreadyDoneVO -> {
            alreadyDoneVO.setUseTime(DateUtils.timeCount(ConverterUtils.toInt(alreadyDoneVO.getUseTime())));
        });
        transService.transMore(alreadyDoneList);
        int alreadyDoneCount = flowTaskHistoryService.findTaskHistoryCount(paramMap);
        super.outJsonp(new Pager(alreadyDoneCount, alreadyDoneList).asJson());
    }

    /**
     * 审批历史
     */
    @RequestMapping("getApprovalRecord")
    @LogMethod
    public void getApprovalRecord(String instanceId) {
        ParamChecker.isNotNullOrEmpty(instanceId, "流程实例id不能为空");
        List<TaskHistoryVO> approvalRecord = flowTaskHistoryService.findApprovalRecord(instanceId);
        List<HistoricTaskInstance> histList =
                historyService.createHistoricTaskInstanceQuery().processInstanceId(instanceId).list();
        TaskHistoryVO taskHistoryVO = null;
        for (HistoricTaskInstance historicTaskInstance : histList) {
            for (TaskHistoryVO historyVO : approvalRecord) {
                if (historyVO.getTaskFinishTime() != null) {
                    long finishTime = DateUtils.parseStr(historyVO.getTaskFinishTime(), DateUtils.DATETIME_PATTERN).getTime();
                    long createTime = DateUtils.parseStr(historyVO.getCreateTime(), DateUtils.DATETIME_PATTERN).getTime();
                    long useTime = (finishTime - createTime) / 1000 / 60;
                    historyVO.setUseTime(DateUtils.timeCount(ConverterUtils.toInt(useTime)));
                }
            }
            taskHistoryVO = new TaskHistoryVO();
            if (!("completed".equals(historicTaskInstance.getDeleteReason()))) {
                taskHistoryVO.setCreateTime(DateUtils.doConvertToString(historicTaskInstance.getStartTime()));
                taskHistoryVO.setTaskFinishTime(DateUtils.doConvertToString(historicTaskInstance.getEndTime()));
                taskHistoryVO.setCreateUser(historicTaskInstance.getAssignee());
                taskHistoryVO.setTaskName(historicTaskInstance.getName());
                taskHistoryVO.setTaskId(historicTaskInstance.getId());
                if (DateUtils.doConvertToString(historicTaskInstance.getEndTime()) == null) {
                    taskHistoryVO.setUseStatus(String.valueOf(FlowConstant.APPROVING));
                } else if ("deleted".equals(historicTaskInstance.getDeleteReason())) {
                    taskHistoryVO.setUseStatus(String.valueOf(FlowConstant.RESULT_END));
                }
                approvalRecord.add(taskHistoryVO);
            }
        }
        transService.transMore(approvalRecord);
        super.outJsonp(new Pager(approvalRecord.size(), approvalRecord).asJson());
    }


}
