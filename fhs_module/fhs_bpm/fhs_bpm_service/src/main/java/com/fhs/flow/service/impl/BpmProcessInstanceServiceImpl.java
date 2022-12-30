package com.fhs.flow.service.impl;

import com.fhs.flow.service.task.BpmProcessInstanceService;
import com.fhs.flow.vo.BpmProcessInstanceCreateReqDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Flowable 流程实例 Api 实现类
 *
 * @author 芋道源码
 * @author jason
 */
@Service("BpmProcessInstanceServiceImplApi")
@Validated
public class BpmProcessInstanceServiceImpl implements com.fhs.flow.service.BpmProcessInstanceService {

    @Resource
    private BpmProcessInstanceService processInstanceService;

    @Override
    public String createProcessInstance(Long userId, @Valid BpmProcessInstanceCreateReqDTO reqDTO) {
        return processInstanceService.createProcessInstance(userId, reqDTO, null);
    }
}
