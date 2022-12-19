package com.fhs.flow.controller.admin.task.vo.instance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

/**
 * @author tanyukun
 */
@ApiModel("管理后台 - 流程实例的创建 Request VO")
@Data
public class BpmProcessInstanceCreateBusVO {

    @ApiModelProperty(value = "业务id", required = true, example = "1024")
    @NotEmpty(message = "业务id")
    private String businessKey;

    @ApiModelProperty(value = "流程定义 KEY", required = true, example = "oa_leave")
    @NotEmpty(message = "流程定义 KEY")
    private String processDefinitionKey;

    @ApiModelProperty(value = "变量实例")
    private Map<String, Object> variables;

}
