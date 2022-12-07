package com.fhs.flow.service;

import com.fhs.easycloud.anno.CloudApi;
import com.fhs.easycloud.anno.CloudMethod;
import com.fhs.flow.vo.BpmProcessInstanceCreateReqDTO;

import javax.validation.Valid;

/**
 * 流程实例 Api 接口
 *
 * @author 芋道源码
 */
@CloudApi(serviceName = "bpm")
public interface BpmProcessInstanceService {

    /**
     * 创建流程实例（提供给内部）
     *
     * @param userId 用户编号
     * @param reqDTO 创建信息
     * @return 实例的编号
     */
    @CloudMethod
    String createProcessInstance(Long userId, @Valid BpmProcessInstanceCreateReqDTO reqDTO);
}
