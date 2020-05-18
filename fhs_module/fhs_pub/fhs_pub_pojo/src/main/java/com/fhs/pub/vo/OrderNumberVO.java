package com.fhs.pub.vo;

import com.fhs.core.base.pojo.vo.VO;
import com.fhs.pub.dox.OrderNumberDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 订单号相关信息
 * @author user
 * @since 2019-03-11 14:37:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OrderNumberVO extends OrderNumberDO implements VO {
}
