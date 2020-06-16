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
 * 流程自定义按钮(FlowJbmpXmlButton)实体类
 *
 * @author wanglei
 * @since 2020-06-16 14:54:52
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_flow_jbmp_xml_button")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FlowJbmpXmlButtonDO", description = "FlowJbmpXmlButton参数")
public class FlowJbmpXmlButtonDO extends BaseDO<FlowJbmpXmlButtonDO> {
    private static final long serialVersionUID = 111685833854224986L;
    @TableId(value = "button_id", type = IdType.UUID)
    @ApiModelProperty(value = "主键")
    private String buttonId;

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
    @TableField("task_name")
    @ApiModelProperty(value = "任务名称")
    private String taskName;

    /**
     * 按钮名称
     */
    @Length(message = "按钮名称字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("button_name")
    @ApiModelProperty(value = "按钮名称")
    private String buttonName;

    /**
     * 流程变量key
     */
    @Length(message = "流程变量key字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("variables_key")
    @ApiModelProperty(value = "流程变量key")
    private String variablesKey;

    /**
     * 流程变量值
     */
    @Length(message = "流程变量值字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("variables_val")
    @ApiModelProperty(value = "流程变量值")
    private String variablesVal;


}