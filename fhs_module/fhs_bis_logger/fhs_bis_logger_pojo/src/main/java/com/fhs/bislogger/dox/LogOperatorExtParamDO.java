package com.fhs.bislogger.dox;

import java.io.Serializable;

import com.fhs.core.base.dox.BaseDO;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.mybatis.jpa.annotation.*;
import com.fhs.core.valid.group.*;

import javax.validation.constraints.*;

import org.hibernate.validator.constraints.Length;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 日志扩展参数(LogOperatorExtParam)实体类
 *
 * @author wanglei
 * @since 2020-04-23 13:58:59
 */

@Data
@Builder
@ApiModel(value = "LogOperatorExtParamDO", description = "LogOperatorExtParam参数")
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_log_operator_ext_param")
@EqualsAndHashCode(callSuper = true)
public class LogOperatorExtParamDO extends BaseDO<LogOperatorExtParamDO> {
    private static final long serialVersionUID = -88643840123395899L;
    @TableId(value = "ext_param_id", type = IdType.UUID)
    @ApiModelProperty(value = "扩展id")
    private String extParamId;

    /**
     * 命名空间
     */
    @NotEmpty
    @NotNull(message = "命名空间字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "命名空间字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("namespace")
    @ApiModelProperty(value = "命名空间")
    private String namespace;

    /**
     * 主键
     */
    @NotEmpty
    @NotNull(message = "主键字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "主键字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("pkey")
    @ApiModelProperty(value = "主键")
    private String pkey;

    /**
     * 版本
     */
    @TableField("version")
    @ApiModelProperty(value = "版本")
    private Integer version;

    /**
     * 主表id
     */
    @NotEmpty
    @NotNull(message = "主表id字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "主表id字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("main_id")
    @ApiModelProperty(value = "主表id")
    private String mainId;

    /**
     * historyid
     */
    @NotEmpty
    @NotNull(message = "historyid字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "historyid字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("his_id")
    @ApiModelProperty(value = "historyid")
    private String hisId;

    /**
     * 操作类型 0 新增1修改2删除
     */
    @NotNull(message = "操作类型字段不可为null", groups = {Update.class, Delete.class})
    @TableField("operator_type")
    @Trans(type = TransType.WORD_BOOK, key = "operator_type")
    private Integer operatorType;

}
