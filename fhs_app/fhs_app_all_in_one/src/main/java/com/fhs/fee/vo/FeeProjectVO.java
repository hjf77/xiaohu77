package com.fhs.fee.vo;


import com.fhs.fee.po.FeeProjectPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * 费用项目(FeeProject)实体类
 *
 * @author yutao
 * @since 2022-05-31 14:50:22
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="费用项目",description ="费用项目")
public class FeeProjectVO extends FeeProjectPO implements VO {

}
    
