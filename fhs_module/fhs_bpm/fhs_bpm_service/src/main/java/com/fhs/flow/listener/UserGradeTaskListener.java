package com.fhs.flow.listener;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fhs.basics.service.UcenterMsUserService;
import com.fhs.basics.vo.UcenterMsUserVO;
import org.apache.commons.lang.StringUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.history.HistoricProcessInstance;

/**
 * 判断流程发起人所在机构的等级并设置进变量
 * @author tanyukun
 */
public class UserGradeTaskListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) {
        // 获取流程发起人
        HistoricProcessInstance hi = SpringUtil.getBean(HistoryService.class).createHistoricProcessInstanceQuery()
                .processInstanceId(execution.getProcessInstanceId())
                .singleResult();
        if (StringUtils.isEmpty(hi.getStartUserId())) {
            return;
        }
        UcenterMsUserVO startUserVo = SpringUtil.getBean(UcenterMsUserService.class).selectById(hi.getStartUserId());
        if (ObjUtil.isEmpty(startUserVo)) {
            return;
        }
        execution.setVariable("userGrade",startUserVo.getIsGrade());
    }
}
