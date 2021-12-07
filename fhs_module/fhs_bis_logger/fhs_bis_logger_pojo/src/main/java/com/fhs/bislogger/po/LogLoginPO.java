package com.fhs.bislogger.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.valid.group.Add;
import com.fhs.core.valid.group.Delete;
import com.fhs.core.valid.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
public class LogLoginPO extends BasePO<LogLoginPO> {
    private static final long serialVersionUID = 330446860714574816L;

    /**
     * 日志id
     */
    @TableId(value = "log_id", type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键id")
    private String logId;

    /**
     * 访问次数
     */
    @TableField(value = "visit_number")
    private Long visitNumber;

    /**
     * 用户id
     */
    @NotEmpty
    @NotNull(message = "用户id字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "用户id字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("user_id")
    @ApiModelProperty(value = "用户id")
    private Long userId;

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

    /**
     * 0 用户名不存在 1 密码错误 2 验证码错误 3 验证码失效 4 其他
     */
    @TableField("error_info")
    @ApiModelProperty(value = "0 用户名不存在 1 密码错误 2 验证码错误 3 验证码失效 4 其他")
    private Integer errorInfo;

    /**
     * 登录名
     */
    @TableField("login_name")
    @ApiModelProperty(value = "登录名")
    private String loginName;

}
