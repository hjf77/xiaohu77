package com.fhs.basics.vo;


import com.fhs.basics.po.LogHistoryDataPO;
import com.fhs.core.trans.vo.VO;
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
public class LogHistoryDataVO extends LogHistoryDataPO implements VO {

    /**
     * 模型
     */
    String model;
}
