package com.fhs.basics.vo;

import com.fhs.basics.po.CommonMessagePO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 消息推送表(CommonMessage)实体类
 *
 * @author miyaxin
 * @since 2022-08-18 16:21:42
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CommonMessageVO", description = "CommonMessage参数")
public class CommonMessageVO extends CommonMessagePO implements VO {

    /**
     * 家庭名称
     */
    @ApiModelProperty(value = "家庭名称")
    String familyName;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    String userName;
    /**
     * 手机型号
     */
    @ApiModelProperty(value = "手机型号")
    String phoneModel;
}
    
