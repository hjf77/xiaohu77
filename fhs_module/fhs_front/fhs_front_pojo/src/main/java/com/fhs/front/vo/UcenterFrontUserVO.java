package com.fhs.front.vo;

import com.fhs.core.trans.vo.VO;
import com.fhs.core.trans.constant.TransType;
import com.fhs.front.dox.UcenterFrontUserDO;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.Map;

/**
 * 前端用户vo
 *
 * @author user
 * @since 2019-05-18 11:49:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UcenterFrontUserVO", description = "UcenterFrontUser参数")
public class UcenterFrontUserVO extends UcenterFrontUserDO implements VO {

    /**
     * openidmap
     * key0 微信openid 1 支付宝openid
     */
    private Map<Integer, String> openIdMap;
}
