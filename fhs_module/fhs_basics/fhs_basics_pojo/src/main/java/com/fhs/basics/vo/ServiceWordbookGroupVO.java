package com.fhs.basics.vo;

import com.fhs.basics.dox.ServiceWordbookGroupDO;
import com.fhs.core.base.pojo.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 字典分组vo
 * @author user
 * @date 2020-05-18 15:28:36
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="ServiceWordbookGroupVO",description ="ServiceWordbookGroup参数")
public class ServiceWordbookGroupVO extends ServiceWordbookGroupDO implements VO {

}
