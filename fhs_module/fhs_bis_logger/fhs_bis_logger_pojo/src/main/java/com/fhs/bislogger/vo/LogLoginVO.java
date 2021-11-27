package com.fhs.bislogger.vo;


import com.fhs.bislogger.po.LogLoginPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 登录日志(LogLogin)实体类
 *
 * @author wanglei
 * @since 2020-04-23 13:58:42
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LogLoginDO", description = "LogLogin参数")
public class LogLoginVO extends LogLoginPO implements VO {

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 登录次数
     */
    private Integer count;

}
