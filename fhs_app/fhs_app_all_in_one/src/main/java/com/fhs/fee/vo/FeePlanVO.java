package com.fhs.fee.vo;

import java.util.Date;

import com.fhs.fee.po.FeePlanPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 费用方案(FeePlan)实体类
 *
 * @author wanglei
 * @since 2022-06-01 15:42:18
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="费用方案",description ="费用方案")
public class FeePlanVO extends FeePlanPO implements VO {
    
 }
    
