package com.fhs.bislogger.vo;


import com.fhs.bislogger.dox.LogOperatorExtParamDO;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
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
@TransTypes(types = {TransType.WORD_BOOK, TransType.AUTO_TRANS})
public class LogOperatorExtParamVO extends LogOperatorExtParamDO implements VO {

    /**
     * 模型
     */
    private String model;
}
