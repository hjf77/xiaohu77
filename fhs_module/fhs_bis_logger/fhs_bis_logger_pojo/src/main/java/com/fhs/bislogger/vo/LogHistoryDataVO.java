package com.fhs.bislogger.vo;


import com.fhs.bislogger.dox.LogHistoryDataDO;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * (LogHistoryData)实体类
 *
 * @author wanglei
 * @since 2020-04-23 14:27:43
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="LogHistoryDataDO",description ="LogHistoryData参数")
@TransTypes(types = {TransType.WORD_BOOK, TransType.AUTO_TRANS})
public class LogHistoryDataVO extends LogHistoryDataDO implements VO {
    
 }