package com.fhs.basics.vo;


import com.fhs.basics.po.CommonLanguagePO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 语言设置表(CommonLanguage)实体类
 *
 * @author LiYuLin
 * @since 2022-09-13 15:05:44
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "语言设置表", description = "语言设置表")
public class CommonLanguageVO extends CommonLanguagePO implements VO {

}
    
