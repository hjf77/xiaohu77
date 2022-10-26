package com.fhs.basics.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.base.valid.group.Add;
import com.fhs.core.base.valid.group.Delete;
import com.fhs.core.base.valid.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 系统日志(LogOperatorSysLog)实体类
 *
 * @author wanglei
 * @since 2022-10-26 11:20:33
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value="t_log_operator_sys_log",autoResultMap = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="LogOperatorSysLogPO",description ="系统日志")
public class LogOperatorSysLogPO extends BasePO<LogOperatorSysLogPO> {
    
    private static final long serialVersionUID = -63030780447257294L;

    @TableId(value = "id", type = IdType.NONE)
    @NotNull(message = "id字段不可为空", groups = {Update.class, Delete.class})
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 系统日志
     */
    @Length(message = "系统日志字段的长度最大为-1", groups = {Add.class, Update.class}, max = -1)
    @TableField("sys_log")
    @ApiModelProperty(value = "系统日志")
    private String sysLog;

    /**
     * 主表id
     */
    @Length(message = "主表id字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("operator_main_id")
    @ApiModelProperty(value = "主表id")
    private String operatorMainId;



}
