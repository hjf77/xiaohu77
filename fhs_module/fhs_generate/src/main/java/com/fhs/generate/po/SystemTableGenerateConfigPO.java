package com.fhs.generate.po;

import java.io.Serializable;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.base.valid.group.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.Length;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 表生成代码配置(SystemTableGenerateConfig)实体类
 *
 * @author wanglei
 * @since 2022-09-07 14:50:23
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_system_table_generate_config")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="SystemTableGenerateConfigPO",description ="表生成代码配置")
public class SystemTableGenerateConfigPO extends BasePO<SystemTableGenerateConfigPO> {
    
    private static final long serialVersionUID = -78171941400986460L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @NotNull(message = "${column.comment}字段不可为空", groups = {Update.class, Delete.class})
    @ApiModelProperty(value = "")
    private Long id;

    /**
     * 数据库名
     */
    @Length(message = "数据库名字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("db_name")
    @ApiModelProperty(value = "数据库名")
    private String dbName;

    /**
     * 表名
     */
    @Length(message = "表名字段的长度最大为64", groups = {Add.class, Update.class}, max = 64)
    @TableField("table_name")
    @ApiModelProperty(value = "表名")
    private String tableName;

    /**
     * 备注
     */
    @Length(message = "备注字段的长度最大为64", groups = {Add.class, Update.class}, max = 64)
    @TableField("remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 列表配置
     */
    @Length(message = "列表配置字段的长度最大为-1", groups = {Add.class, Update.class}, max = -1)
    @TableField("list_config")
    @ApiModelProperty(value = "列表配置")
    private String listConfig;

    /**
     * 表单配置
     */
    @Length(message = "表单配置字段的长度最大为-1", groups = {Add.class, Update.class}, max = -1)
    @TableField("form_config")
    @ApiModelProperty(value = "表单配置")
    private String formConfig;

    /**
     * 是否配置了列表
     */
    @TableField("is_config_list")
    @ApiModelProperty(value = "是否配置了列表")
    private Integer isConfigList;

    /**
     * 是否配置了表单
     */
    @TableField("is_config_form")
    @ApiModelProperty(value = "是否配置了表单")
    private Integer isConfigForm;

    /**
     * 集团编码
     */
    @Length(message = "集团编码字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("group_code")
    @ApiModelProperty(value = "集团编码")
    private String groupCode;

    /**
     * 列表列配置
     */
    @Length(message = "列表列配置字段的长度最大为-1", groups = {Add.class, Update.class}, max = -1)
    @TableField("list_column_sett")
    @ApiModelProperty(value = "列表列配置")
    private String listColumnSett;



}
