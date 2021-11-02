package com.fhs.bislogger.vo;


import com.fhs.bislogger.dox.LogHistoryDataDO;
import com.fhs.core.trans.vo.VO;
import com.fhs.core.trans.constant.TransType;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 日志历史(LogHistoryData)实体类 vo
 *
 * @author wanglei
 * @since 2020-04-23 14:27:43
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LogHistoryDataDO", description = "LogHistoryData参数")
public class LogHistoryDataVO extends LogHistoryDataDO implements VO {

    /**
     * 模型
     */
    String model;
}