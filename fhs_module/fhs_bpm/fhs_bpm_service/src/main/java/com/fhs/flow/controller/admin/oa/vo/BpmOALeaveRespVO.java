package com.fhs.flow.controller.admin.oa.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fhs.common.utils.DateUtils;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@ApiModel("管理后台 - 请假申请 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmOALeaveRespVO extends BpmOALeaveBaseVO {

    @ApiModelProperty(value = "请假表单主键", required = true, example = "1024")
    private Long id;

    @ApiModelProperty(value = "状态", required = true, example = "1", notes = "参见 bpm_process_instance_result 枚举")
    @Trans(type = TransType.DICTIONARY, key = "bpm_process_instance_result")
    private Integer result;

    @ApiModelProperty(value = "申请时间", required = true)
    @NotNull(message = "申请时间不能为空")
    @JSONField(format = DateUtils.DATETIME_PATTERN)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "流程id")
    private String processInstanceId;

}
