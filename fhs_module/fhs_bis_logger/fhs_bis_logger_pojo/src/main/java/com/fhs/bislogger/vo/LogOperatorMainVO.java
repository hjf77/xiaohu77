package com.fhs.bislogger.vo;


import com.fhs.bislogger.dox.LogOperatorMainDO;
import com.fhs.core.trans.vo.VO;
import com.fhs.core.trans.constant.TransType;
import io.swagger.annotations.ApiModel;
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
public class LogOperatorMainVO extends LogOperatorMainDO implements VO {

    /**
     * 访问次数
     */
    private Integer visits;

}