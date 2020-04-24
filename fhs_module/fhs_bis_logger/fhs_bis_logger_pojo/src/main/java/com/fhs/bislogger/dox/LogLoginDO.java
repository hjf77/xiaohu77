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
 * 登录日志(LogLogin)实体类
 *
 * @author wanglei
 * @since 2020-04-23 13:58:42
 */

@Data
@Builder
@ApiModel(value = "LogLoginDO", description = "LogLogin参数")
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_log_login")
@EqualsAndHashCode(callSuper = true)
public class LogLoginDO extends BaseDO<LogLoginDO> {
    private static final long serialVersionUID = 330446860714574816L;
    @TableId(value = "log_id", type = IdType.UUID)
    @ApiModelProperty(value = "${column.comment}")
    private String logId;

    /**
     * 用户id
     */
    @NotEmpty
    @NotNull(message = "用户id字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "用户id字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("user_id")
    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * ip
     */
    @NotEmpty
    @NotNull(message = "ip字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "ip字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("ip_address")
    @ApiModelProperty(value = "ip")
    private String ipAddress;

    /**
     * ip信息
     */
    @Length(message = "ip信息字段的长度最大为-1", groups = {Add.class, Update.class}, max = -1)
    @TableField("ip_info")
    @ApiModelProperty(value = "ip信息")
    private String ipInfo;

    /**
     * 浏览器
     */
    @Length(message = "浏览器字段的长度最大为64", groups = {Add.class, Update.class}, max = 64)
    @TableField("browser")
    @ApiModelProperty(value = "浏览器")
    private String browser;

    /**
     * 操作系统
     */
    @Length(message = "操作系统字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("os")
    @ApiModelProperty(value = "操作系统")
    private String os;

    /**
     * 状态 0成功 1 失败
     */
    @TableField("state")
    @ApiModelProperty(value = "状态 0成功 1 失败")
    private Integer state;

    /**
     * 类型0 登入 1 登出
     */
    @TableField("type")
    @ApiModelProperty(value = "类型0 登入 1 登出")
    private Integer type;


}