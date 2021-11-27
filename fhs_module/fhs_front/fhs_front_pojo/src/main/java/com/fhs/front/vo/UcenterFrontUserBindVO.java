package com.fhs.front.vo;

import com.fhs.core.trans.vo.VO;
import com.fhs.front.po.UcenterFrontUserBindPO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 前端用户绑定 openidvo
 *
 * @author user
 * @since 2019-05-18 11:49:21
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UcenterFrontUserBindVO", description = "UcenterFrontUserBind参数")
public class UcenterFrontUserBindVO extends UcenterFrontUserBindPO implements VO {

}
