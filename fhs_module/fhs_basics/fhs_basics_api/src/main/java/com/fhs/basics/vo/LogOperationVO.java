package com.fhs.basics.vo;

import com.fhs.basics.po.LogOperationPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * (LogOperation)实体类
 *
 * @author LiYuLin
 * @since 2022-03-10 16:43:43
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="",description ="")
public class LogOperationVO extends LogOperationPO implements VO {
    
 }
    
