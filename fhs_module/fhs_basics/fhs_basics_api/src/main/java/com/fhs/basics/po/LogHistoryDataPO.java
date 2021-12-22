package com.fhs.basics.po;

import com.fhs.core.base.po.BasePO;
import com.fhs.core.valid.group.*;

import org.hibernate.validator.constraints.Length;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 日志历史表(LogHistoryData)实体类
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
public class LogHistoryDataPO extends BasePO<LogHistoryDataPO> {
    private static final long serialVersionUID = 312083419921410582L;

    /**
     * id
     */
    @TableId(value = "his_id", type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "历史id")
    private String hisId;

    /**
     * 命名空间
     */
    @Length(message = "${column.comment}字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("namespace")
    @ApiModelProperty(value = "${column.comment}")
    private String namespace;

    /**
     * 主键
     */
    @Length(message = "${column.comment}字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("pkey")
    @ApiModelProperty(value = "${column.comment}")
    private String pkey;

    /**
     * mainId
     */
    @Length(message = "${column.comment}字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("main_id")
    @ApiModelProperty(value = "${column.comment}")
    private String mainId;

    /**
     * 内容
     */
    @Length(message = "${column.comment}字段的长度最大为-1", groups = {Add.class, Update.class}, max = -1)
    @TableField("data")
    @ApiModelProperty(value = "${column.comment}")
    private String data;

    /**
     * 版本
     */
    @TableField("version")
    @ApiModelProperty(value = "${column.comment}")
    private Integer version;


}
