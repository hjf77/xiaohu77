package com.fhs.basics.vo;


import com.fhs.basics.po.LogOperatorExtParamPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 扩展参数(LogOperatorExtParam)实体类 vo
 *
 * @author wanglei
 * @since 2020-04-23 13:58:59
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LogOperatorExtParamDO", description = "LogOperatorExtParam参数")
public class LogOperatorExtParamVO extends LogOperatorExtParamPO implements VO {

    /**
     * 模型
     */
    private String model;
}
