package com.fhs.pub.vo;

import com.fhs.core.base.pojo.vo.VO;
import com.fhs.pub.dox.OrderNumberDO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 订单号相关信息
 * @author user
 * @since 2019-03-11 14:37:18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="OrderNumberVO",description ="OrderNumber参数")
public class OrderNumberVO extends OrderNumberDO implements VO {
}