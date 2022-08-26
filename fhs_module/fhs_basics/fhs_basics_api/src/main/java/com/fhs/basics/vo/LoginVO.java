package com.fhs.basics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("登录表单")
public class LoginVO {
    @ApiModelProperty("验证码")
    private String identifyCode;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("用户名")
    private String userLoginName;
    @ApiModelProperty("随机串")
    private String uuid;
    @ApiModelProperty("租户编码")
    private String groupCode;
    @ApiModelProperty("是否验证码登录（0否 1是）")
    private Integer isCode;
    @ApiModelProperty("短信验证码")
    private String smsCode;
    @ApiModelProperty("是否注册(0 否 1是)")
    private Integer isRegister;

}
