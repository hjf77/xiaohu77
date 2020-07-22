package com.fhs.flow.dox;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.common.constant.Constant;
import com.fhs.common.utils.DateUtils;
import com.fhs.core.base.dox.BaseDO;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.valid.group.*;
import com.mybatis.jpa.annotation.Between;
import com.mybatis.jpa.annotation.Like;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * fhs的流程实例，为activiti的实例扩展表(FlowInstance)实体类
 *
 * @author jackwong
 * @since 2019-11-11 19:40:44
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_flow_instance")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FlowInstanceDO", description = "FlowInstance参数")
public class FlowInstanceDO extends BaseDO<FlowInstanceDO> {
    private static final long serialVersionUID = -80075792071824152L;
    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 实例标题
     */
    @NotEmpty
    @NotNull(message = "实例标题字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "实例标题字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("title")
    @ApiModelProperty("实例标题")
    private String title;

    /**
     * xml id
     */
    @NotEmpty
    @NotNull(message = "xml id字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "xml id字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("xml_id")
    @Trans(type = TransType.AUTO_TRANS,key = "flow_jbpm_xml")
    @ApiModelProperty("xml id")
    private String xmlId;

    /**
     * activiti中流程实例id
     */
    @Length(message = "activiti中流程实例id字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("activiti_process_instance_id")
    @ApiModelProperty("activiti中流程实例id")
    private String activitiProcessInstanceId;

    /**
     * 流程外键
     */
    @NotEmpty
    @NotNull(message = "流程外键字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "流程外键字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("form_pkey")
    @ApiModelProperty("流程外键")
    private String formPkey;

    /**
     * 扩展参数-在打开表单的时候会附加上
     */
    @NotEmpty
    @NotNull(message = "扩展参数-在打开表单的时候会附加上字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "扩展参数-在打开表单的时候会附加上字段的长度最大为512", groups = {Add.class, Update.class}, max = 512)
    @TableField("ext_form_param")
    @ApiModelProperty("扩展参数")
    private String extFormParam;

    /**
     * 状态
     */
    @TableField("status")
    @Trans(type = TransType.WORD_BOOK, key = "workFlow_process_status")
    @ApiModelProperty("状态")
    private Integer status;

    /**
     * 第一个用户任务的key
     */
    @TableField("first_definition_key")
    @ApiModelProperty("第一个用户任务key")
    private String firstDefinitionKey;

    /**
     * 抄送
     */
    @Like
    @Trans(type = TransType.AUTO_TRANS,key = Constant.USER_INFO + "#ccToUser")
    @TableField("cc_to")
    @ApiModelProperty("抄送")
    private String ccTo;

    /**
     * 完成时间
     */
    @Between
    @TableField("finish_time")
    @JSONField(format = DateUtils.DATETIME_PATTERN)
    @ApiModelProperty("完成时间")
    private Date finishTime;

}