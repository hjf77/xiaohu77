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
    @ApiModelProperty("单点登录参数")
    private String jwt;
    @ApiModelProperty("用户编号")
    private String hrCode;

}
