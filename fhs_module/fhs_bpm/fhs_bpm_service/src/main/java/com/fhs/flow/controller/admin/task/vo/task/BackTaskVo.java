package com.fhs.flow.controller.admin.task.vo.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Description: 驳回的实体VO
 * @Author: Bruce.liu
 * @Since:9:19 2018/9/8
 * 爱拼才会赢 2018 ~ 2030 版权所有
 */
@ApiModel("管理后台 - 流程任务驳回的实体VO Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BackTaskVo extends BaseProcessVo {

    /**
     * 需要驳回的节点id 必填
     */
    @ApiModelProperty(value = "需要驳回的节点id", required = true, example = "1024")
    private String distFlowElementId;

}
