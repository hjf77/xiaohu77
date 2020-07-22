package com.fhs.basics.vo;

import com.fhs.basics.dox.ServiceWordbookDO;
import com.fhs.core.base.pojo.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 字典vo
 * @author user
 * @date 2020-05-18 15:29:09
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="ServiceWordbookVO",description ="ServiceWordbook参数")
public class ServiceWordbookVO extends ServiceWordbookDO implements VO {

}
