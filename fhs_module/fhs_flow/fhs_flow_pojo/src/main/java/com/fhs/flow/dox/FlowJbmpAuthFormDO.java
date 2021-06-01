package com.fhs.flow.dox;

import java.io.Serializable;

import com.fhs.core.base.dox.BaseDO;
import com.mybatis.jpa.annotation.*;
import com.fhs.core.valid.group.*;

import javax.validation.constraints.*;

import org.hibernate.validator.constraints.Length;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 自定义审批表单(FlowJbmpAuthForm)实体类
 *
 * @author wanglei
 * @since 2021-06-01 15:03:53
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_flow_jbmp_auth_form")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FlowJbmpAuthFormDO", description = "FlowJbmpAuthForm参数")
public class FlowJbmpAuthFormDO extends BaseDO<FlowJbmpAuthFormDO> {
    private static final long serialVersionUID = -39085699120042500L;
    @TableId(value = "auth_form_id", type = IdType.UUID)
    @NotNull(message = "${column.comment}字段不可为空", groups = {Update.class, Delete.class})
    @ApiModelProperty(value = "")
    private String authFormId;

    /**
     * xmlid
     */
    @Length(message = "xmlid字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("xml_id")
    @ApiModelProperty(value = "xmlid")
    private String xmlId;

    /**
     * 任务名称
     */
    @Length(message = "任务名称字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("task_key")
    @ApiModelProperty(value = "任务名称")
    private String taskKey;

    /**
     * 按钮名称
     */
    @Length(message = "按钮名称字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("form_name")
    @ApiModelProperty(value = "按钮名称")
    private String formName;



    /**
     * 流程变量值
     */
    @Length(message = "流程变量值字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("form_url")
    @ApiModelProperty(value = "流程变量值")
    private String formUrl;


}
