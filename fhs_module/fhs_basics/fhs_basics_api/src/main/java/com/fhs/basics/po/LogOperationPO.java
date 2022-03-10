package com.fhs.basics.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.valid.group.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * (LogOperation)实体类
 *
 * @author LiYuLin
 * @since 2022-03-10 16:43:43
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_log_operation")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LogOperationPO", description = "")
public class LogOperationPO extends BasePO<LogOperationPO> {
    private static final long serialVersionUID = -41806741299645474L;
    @TableId(value = "id", type = IdType.NONE)
    @NotNull(message = "${column.comment}字段不可为空", groups = {Update.class, Delete.class})
    @ApiModelProperty(value = "")
    private String id;

    @Length(message = "${column.comment}字段的长度最大为-1", groups = {Add.class, Update.class}, max = -1)
    @TableField("data")
    @ApiModelProperty(value = "${column.comment}")
    private String data;


}
