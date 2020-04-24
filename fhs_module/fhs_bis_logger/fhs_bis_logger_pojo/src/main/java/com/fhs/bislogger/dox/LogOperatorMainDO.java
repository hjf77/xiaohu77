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
 * (LogOperatorMain)实体类
 *
 * @author wanglei
 * @since 2020-04-23 13:59:14
 */

@Data
@Builder
@ApiModel(value = "LogOperatorMainDO", description = "LogOperatorMain参数")
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_log_operator_main")
@EqualsAndHashCode(callSuper = true)
public class LogOperatorMainDO extends BaseDO<LogOperatorMainDO> {
    private static final long serialVersionUID = -83321483098557581L;
    @TableId(value = "log_id", type = IdType.UUID)
    @ApiModelProperty(value = "${column.comment}")
    private String logId;

    /**
     * 模块
     */
    @Length(message = "模块字段的长度最大为255", groups = {Add.class, Update.class}, max = 255)
    @TableField("model")
    @ApiModelProperty(value = "模块")
    private String model;

    /**
     * url
     */
    @Length(message = "url字段的长度最大为255", groups = {Add.class, Update.class}, max = 255)
    @TableField("url")
    @ApiModelProperty(value = "url")
    private String url;

    /**
     * ip地址
     */
    @Length(message = "ip地址字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("ip")
    @ApiModelProperty(value = "ip地址")
    private String ip;

    /**
     * 请求方法 0 get 1post 2 put 3 delete 4 head 5 connect 6 options 7 race 8 patch
     */
    @TableField("req_method")
    @ApiModelProperty(value = "请求方法 0 get 1post 2 put 3 delete 4 head 5 connect 6 options 7 race 8 patch")
    private String reqMethod;

    @Length(message = "请求参数字段的长度最大为-1", groups = {Add.class, Update.class}, max = -1)
    @TableField("req_param")
    @ApiModelProperty(value = "请求参数")
    private String reqParam;

    @Length(message = "返回数据字段的长度最大为-1", groups = {Add.class, Update.class}, max = -1)
    @TableField("resp_body")
    @ApiModelProperty(value = "返回数据")
    private String respBody;

    /**
     * 0成功1 失败
     */
    @TableField("state")
    @ApiModelProperty(value = "状态")
    private Integer state;

    /**
     * 0 新增 1 修改 2删除 3 查询 4 导入  5 导出
     */
    @TableField("type")
    @ApiModelProperty(value = "0 新增 1 修改 2删除 3 查询 4 导入  5 导出")
    private Integer type;


}