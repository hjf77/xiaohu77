package com.fhs.basics.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 一次添加日志
 *
 * @author user
 * @date 2020-05-18 14:30:22
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
     * 系统日志
     */
    private LogOperatorSysLogVO operatorSysLogVO;

    /**
     * 扩展参数,修改或者删除的时候会有
     */
    private List<LogOperatorExtParamVO> operatorExtParamVOList = new ArrayList<>();


    /**
     * 历史数据
     */
    private List<LogHistoryDataVO> historyDataVOList = new ArrayList<>();


}
