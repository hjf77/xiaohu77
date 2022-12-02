package com.fhs.flow.controller.admin.oa.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fhs.common.utils.DateUtils;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.trans.vo.TransPojo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


/**
* 请假申请 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class BpmOALeaveBaseVO implements TransPojo {

    @ApiModelProperty(value = "请假的开始时间", required = true)
    @NotNull(message = "开始时间不能为空")
    @JSONField(format = DateUtils.DATETIME_PATTERN)
    private LocalDateTime startTime;

    @ApiModelProperty(value = "请假的结束时间", required = true)
    @NotNull(message = "结束时间不能为空")
    @JSONField(format = DateUtils.DATETIME_PATTERN)
    private LocalDateTime endTime;

    @ApiModelProperty(value = "请假类型", required = true, example = "1", notes = "参见 bpm_oa_type 枚举")
    @Trans(type = TransType.DICTIONARY, key = "bpm_oa_leave_type")
    private Integer type;

    @ApiModelProperty(value = "原因", required = true, example = "阅读芋道源码")
    private String reason;

}
