package com.fhs.flow.service.oa.listener;

import com.fhs.flow.framework.bpm.core.event.BpmProcessInstanceResultEvent;
import com.fhs.flow.framework.bpm.core.event.BpmProcessInstanceResultEventListener;
import com.fhs.flow.service.oa.BpmOALeaveService;
import com.fhs.flow.service.oa.BpmOALeaveServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * OA 请假单的结果的监听器实现类
 *
 * @author 芋道源码
 */
@Component
public class BpmOALeaveResultListener extends BpmProcessInstanceResultEventListener {

    @Resource
    private BpmOALeaveService leaveService;

    @Override
    protected String getProcessDefinitionKey() {
        return BpmOALeaveServiceImpl.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceResultEvent event) {
        leaveService.updateLeaveResult(Long.parseLong(event.getBusinessKey()), event.getResult());
    }

}
