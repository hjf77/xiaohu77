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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 流程任务日志(FlowTaskHistory)实体类
 *
 * @author jackwong
 * @since 2019-11-12 14:40:34
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_flow_task_history")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FlowTaskHistoryDO", description = "FlowTaskHistory参数")
public class FlowTaskHistoryDO extends BaseDO<FlowTaskHistoryDO> {
    private static final long serialVersionUID = -88447633213612521L;
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @ApiModelProperty("主键id")
    private String id;

    /**
     * activiti 实例id
     */
    @Length(message = "${column.comment}字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("instance_id")
    @ApiModelProperty("实例id")
    private String instanceId;

    /**
     * 完成时间
     */
    @TableField("task_finish_time")
    @JSONField(format = DateUtils.DATETIME_PATTERN)
    @ApiModelProperty("完成时间")
    private Date taskFinishTime;

    /**
     * 任务id
     */
    @Length(message = "任务id字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("task_id")
    @ApiModelProperty("任务id")
    private String taskId;

    /**
     * 候选用户
     */
    @Length(message = "候选用户字段的长度最大为512", groups = {Add.class, Update.class}, max = 512)
    @TableField("candidate_user_id")
    @ApiModelProperty("候选用户")
    private String candidateUserId;

    /**
     * 候选角色
     */
    @Length(message = "候选角色字段的长度最大为512", groups = {Add.class, Update.class}, max = 512)
    @TableField("candidate_rule_id")
    @ApiModelProperty("候选角色")
    private String candidateRuleId;

    /**
     * 0 未完成 1 已完成
     */
    @NotNull(message = "0 未完成 1 已完成字段不可为null", groups = {Update.class, Delete.class})
    @TableField("status")
    @Trans(type = TransType.WORD_BOOK, key = "task_history_status")
    @ApiModelProperty("状态")
    private Integer status;

    /**
     * 任务标题
     */
    @TableField("title")
    @ApiModelProperty("任务标题")
    private String title;

    /**
     * 任务父taskid
     */
    @TableField("parent_task_id")
    @ApiModelProperty("任务父taskid")
    private String parentTaskId;

    /**
     * 使用时间
     */
    @TableField("use_time")
    @ApiModelProperty("使用时间")
    private Integer useTime;

    /**
     * 完成用户id
     */
    @TableField("assignee_user_id")
    @ApiModelProperty("完成用户id")
    private String assigneeUserId;

    /**
     * 结果
     */
    @TableField("result")
    @ApiModelProperty("结果")
    private Integer result;

    /**
     * 备注
     */
    @TableField("remark")
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 节点编码
     */
    @TableField("code")
    @ApiModelProperty("节点编码")
    private String code;

    /**
     * 排序
     */
    @TableField("order_num")
    @ApiModelProperty("排序")
    private Integer orderNum;

    /**
     * 节点usertaskid
     */
    @TableField("definition_key")
    @ApiModelProperty("节点usertaskid")
    private String definitionKey;

}
