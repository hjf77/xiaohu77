package com.fhs.app.test;

import com.fhs.core.feign.autowired.annotation.AutowiredFhs;
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
public class TestListenerController implements ExecutionListener, TaskListener {


    /**
     * ExecutionListener实现
     */
    @Override
    public void notify(DelegateExecution execution) throws Exception {

        String eventName = execution.getEventName();
        //start
        if ("start".equals(eventName)) {
            System.out.println("start====================================================="+eventName);
        } else if ("end".equals(eventName)) {
            System.out.println("end======================================================="+eventName);
        }else if ("take".equals(eventName)) {
            System.out.println("take======================================================"+eventName);
        }
    }


    /**
     * TaskListener实现
     */
    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        System.out.println("start====================================================="+eventName);
        System.out.println("任务监听器:" + eventName);
        System.out.println("end======================================================="+eventName);
    }
}
