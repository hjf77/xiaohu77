package com.fhs.flow.controller.admin.task.vo.instance;

import com.fhs.flow.vo.BpmProcessInstanceCreateReqDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


/**
 * @author tanyukun
 */
@ApiModel("管理后台 - 流程实例的创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmProcessInstanceCreateBusVO extends BpmProcessInstanceCreateReqDTO {

    /**
     * 流程名称
     */
    private String processInstanceName;
}
