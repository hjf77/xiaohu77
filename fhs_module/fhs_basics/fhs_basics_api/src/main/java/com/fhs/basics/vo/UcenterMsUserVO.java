package com.fhs.basics.vo;

import com.fhs.basics.po.UcenterMsUserPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户vo
 *
 * @author user
 * @date 2020-05-18 15:49:03
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UcenterMsUserVO", description = "UcenterMsUserVO参数")
public class UcenterMsUserVO extends UcenterMsUserPO implements VO {

    @ApiModelProperty("组织机构对应的公司id")
    private String companyId;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("部门名称")
    private String orgName;

    @ApiModelProperty("随机串")
    private String uuid;

    @ApiModelProperty("短信验证码")
    private String smsCode;

    @ApiModelProperty("涂鸦用户注册类型(1 手机号 2邮箱 3其他)")
    private Integer usernameType;

    @ApiModelProperty("国家名称")
    private String countryName;

    @ApiModelProperty("设备名称")
    private String name;

    @ApiModelProperty("国家化码")
    private String internationalCode;

}
