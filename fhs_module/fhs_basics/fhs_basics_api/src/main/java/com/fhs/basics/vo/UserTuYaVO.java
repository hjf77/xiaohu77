package com.fhs.basics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserTuYaVO {


    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("电话号码")
    private String mobile;

    @ApiModelProperty("用户uid")
    private String uid;

    @ApiModelProperty("邮箱")
    private String email;
}
