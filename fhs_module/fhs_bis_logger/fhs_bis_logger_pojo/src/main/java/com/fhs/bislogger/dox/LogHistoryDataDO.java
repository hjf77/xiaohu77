package com.fhs.bislogger.dox;

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
 * (LogHistoryData)实体类
 *
 * @author wanglei
 * @since 2020-04-23 14:27:42
 */

@Data
@Builder
@ApiModel(value = "LogHistoryDataDO", description = "LogHistoryData参数")
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_log_history_data")
@EqualsAndHashCode(callSuper = true)
public class LogHistoryDataDO extends BaseDO<LogHistoryDataDO> {
    private static final long serialVersionUID = 312083419921410582L;
    @TableId(value = "his_id", type = IdType.UUID)
    @ApiModelProperty(value = "${column.comment}")
    private String hisId;

    @Length(message = "${column.comment}字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("namespace")
    @ApiModelProperty(value = "${column.comment}")
    private String namespace;

    @Length(message = "${column.comment}字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("pkey")
    @ApiModelProperty(value = "${column.comment}")
    private String pkey;

    @Length(message = "${column.comment}字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("main_id")
    @ApiModelProperty(value = "${column.comment}")
    private String mainId;

    @Length(message = "${column.comment}字段的长度最大为-1", groups = {Add.class, Update.class}, max = -1)
    @TableField("data")
    @ApiModelProperty(value = "${column.comment}")
    private String data;

    @TableField("version")
    @ApiModelProperty(value = "${column.comment}")
    private Integer version;


}