package com.fhs.flow.controller.admin.definition.vo.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fhs.common.utils.DateUtils;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.trans.vo.TransPojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@ApiModel("管理后台 - 流程模型的分页的每一项 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmModelPageItemRespVO extends BpmModelBaseVO {

    @ApiModelProperty(value = "编号", required = true, example = "1024")
    private String id;

    @ApiModelProperty(value = "表单名字", example = "请假表单")
    private String formName;

    @ApiModelProperty(value = "创建时间", required = true)
    @JSONField(format = DateUtils.DATETIME_PATTERN)
    private LocalDateTime createTime;

    /**
     * 最新部署的流程定义
     */
    private ProcessDefinition processDefinition;

    @ApiModel("流程定义")
    @Data
    public static class ProcessDefinition implements TransPojo {

        @ApiModelProperty(value = "编号", required = true, example = "1024")
        private String id;

        @ApiModelProperty(value = "版本", required = true, example = "1")
        private Integer version;

        @ApiModelProperty(value = "部署时间", required = true)
        @JSONField(format = DateUtils.DATETIME_PATTERN)
        private LocalDateTime deploymentTime;

        @ApiModelProperty(value = "中断状态", required = true, example = "1", notes = "参见 SuspensionState 枚举")
        @Trans(type = TransType.DICTIONARY, key = "suspensionState")
        private Integer suspensionState;

    }

}
