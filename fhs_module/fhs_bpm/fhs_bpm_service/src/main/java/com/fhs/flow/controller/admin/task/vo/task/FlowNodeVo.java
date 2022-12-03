package com.fhs.flow.controller.admin.task.vo.task;

import com.alibaba.fastjson.annotation.JSONField;
import com.fhs.common.utils.DateUtils;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : bruce.liu
 * @title: : FlowNodeVo
 * @projectName : flowable
 * @description: 流程节点的Vo
 * @date : 2019/12/616:24
 */
@ApiModel("管理后台 - 流程任务的审批节点 Request VO")
@Data
@ToString(callSuper = true)
public class FlowNodeVo implements Serializable {
    /**
     * 节点id
     */
    @ApiModelProperty(value = "节点id", required = true, example = "1024")
    private String nodeId;
    /**
     * 节点名称
     */
    @ApiModelProperty(value = "节点名称", required = true, example = "1024")
    private String nodeName;
    /**
     * 执行人的code
     */
    @ApiModelProperty(value = "执行人的code", required = true, example = "1024")
    @Trans(type = TransType.RPC, targetClassName = "com.fhs.basics.po.UcenterMsUserPO", alias = "userId", fields = "userName", serviceName = "basic", dataSource = "basic")
    private Long userId;
    /**
     * 执行人姓名
     */
    @ApiModelProperty(value = "执行人姓名", required = true, example = "1024")
    private String userName;

    /**
     * 任务节点结束时间
     */
    @ApiModelProperty(value = "任务节点结束时间", required = true, example = "1024")
    @JSONField(format = DateUtils.DATETIME_PATTERN)
    private Date endTime;
}
