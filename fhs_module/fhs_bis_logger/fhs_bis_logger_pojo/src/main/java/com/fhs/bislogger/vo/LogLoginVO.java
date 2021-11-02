package com.fhs.bislogger.vo;


import com.fhs.bislogger.dox.LogLoginDO;
import com.fhs.common.utils.NumberUtil;
import com.fhs.core.trans.vo.VO;
import com.fhs.core.trans.constant.TransType;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
public class LogLoginVO extends LogLoginDO implements VO {

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 登录次数
     */
    private Integer count;

}
