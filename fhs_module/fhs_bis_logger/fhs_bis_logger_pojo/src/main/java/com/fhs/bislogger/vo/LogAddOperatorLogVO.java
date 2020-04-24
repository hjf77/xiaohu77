package com.fhs.bislogger.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 一次添加日志
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogAddOperatorLogVO {
    /**
     * 主请求日志
     */
    private LogOperatorMainVO operatorMainVO;

    /**
     * 扩展参数,修改或者删除的时候会有
     */
    private List<LogOperatorExtParamVO> operatorExtParamVOList;


    /**
     * 历史数据
     */
    private List<LogHistoryDataVO> historyDataVOList;


}
