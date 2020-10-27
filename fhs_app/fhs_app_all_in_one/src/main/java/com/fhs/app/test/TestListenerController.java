package com.fhs.app.test;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author user
 */
@RestController
@RequestMapping("/ms/test/listener")
@Slf4j
public class TestListenerController implements ExecutionListener, TaskListener {


    /**
     * ExecutionListener实现
     */
    @Override
    public void notify(DelegateExecution execution) throws Exception {

        String eventName = execution.getEventName();
        //start 未测试  update by cyx 将 System.out.println 替换成 log.info
        if ("start".equals(eventName)) {
            log.info("start====================================================="+eventName);
        } else if ("end".equals(eventName)) {
            log.info("end======================================================="+eventName);
        }else if ("take".equals(eventName)) {
            log.info("take======================================================"+eventName);
        }
    }


    /**
     * TaskListener实现
     */
    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        log.info("start====================================================="+eventName);
        log.info("任务监听器:" + eventName);
        log.info("end======================================================="+eventName);
    }
}
