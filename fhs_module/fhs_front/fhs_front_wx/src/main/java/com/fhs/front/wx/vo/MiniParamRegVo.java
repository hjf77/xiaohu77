package com.fhs.front.wx.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel("小程序注册表单")
public class MiniParamRegVo {
    @NotEmpty(message = "openid 不可为空")
    @ApiModelProperty("openid")
    private String openid;
    @NotEmpty(message = "openid 不可为空")
    @ApiModelProperty("openid")
    private String sessionKey;
    @NotEmpty(message = "signature 不可为空")
    @ApiModelProperty(" wx.getUserInfo 获取 ")
    private String signature;
    @NotEmpty(message = "rawData 不可为空")
    @ApiModelProperty(" wx.getUserInfo 获取 ")
    private String rawData;
    @NotEmpty(message = "encryptedData  不可为空")
    @ApiModelProperty(" wx.getUserInfo 获取 ")
    private String encryptedData;
    @NotEmpty(message = "iv 不可为空")
    @ApiModelProperty(" wx.getUserInfo 获取 ")
    private String iv;
}
