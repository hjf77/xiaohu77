package com.fhs.basics.vo;


import com.fhs.basics.po.LogOperatorSysLogPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 系统日志(LogOperatorSysLog)实体类
 *
 * @author wanglei
 * @since 2022-10-26 11:20:31
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="系统日志",description ="系统日志")
public class LogOperatorSysLogVO extends LogOperatorSysLogPO implements VO {
    
 }
    
