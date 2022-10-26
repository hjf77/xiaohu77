package com.fhs.basics.vo;


import com.fhs.basics.po.LogOperatorMainPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 操作日志(LogOperatorMain)实体类 vo
 *
 * @author wanglei
 * @since 2020-04-23 13:59:14
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LogOperatorMainDO", description = "LogOperatorMain参数")
public class LogOperatorMainVO extends LogOperatorMainPO implements VO {

    /**
     * 访问次数
     */
    private Integer visits;

    @ApiModelProperty("系统日志")
    private String sysLog;

}
