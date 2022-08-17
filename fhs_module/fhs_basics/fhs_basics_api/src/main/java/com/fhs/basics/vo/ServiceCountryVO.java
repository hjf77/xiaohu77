package com.fhs.basics.vo;


import com.fhs.basics.po.ServiceCountryPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * (ServiceCountry)实体类
 *
 * @author miyaxin
 * @since 2022-08-17 17:18:34
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="ServiceCountryVO",description ="ServiceCountry参数")
public class ServiceCountryVO extends ServiceCountryPO implements VO {

}
    
