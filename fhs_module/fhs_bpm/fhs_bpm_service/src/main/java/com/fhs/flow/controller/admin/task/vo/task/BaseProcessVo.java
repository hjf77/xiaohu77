package com.fhs.flow.controller.admin.task.vo.task;

import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 流程执行过程中的基本参数VO
 * @Author: Bruce.liu
 */
@Data
public class BaseProcessVo implements Serializable {
    /**********************任务相关的参数**********************/
    /**
     * 任务id 必填
     */
    @ApiModelProperty(value = "任务id", required = true, example = "1024")
    private String taskId;
    /**********************审批意见的参数**********************/
    /**
     * 操作人code 必填
     */
    @ApiModelProperty(value = "操作人code", required = true, example = "1024")
    @Trans(type = TransType.RPC, targetClassName = "com.fhs.basics.po.UcenterMsUserPO", alias = "userId", fields = "userName", serviceName = "basic", dataSource = "basic")
    private Long userId;
    /**
     * 审批意见 必填
     */
    @ApiModelProperty(value = "审批意见", required = true, example = "1024")
    private String message;
    /**
     * 流程实例的id 必填
     */
    @ApiModelProperty(value = "流程实例的id", required = true, example = "1024")
    private String processInstanceId;
    /**
     * 审批类型 必填
     */
    @ApiModelProperty(value = "审批类型", required = true, example = "1024")
    private String type;

}
