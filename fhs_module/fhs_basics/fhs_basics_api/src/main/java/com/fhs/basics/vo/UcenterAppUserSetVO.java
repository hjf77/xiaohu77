package com.fhs.basics.vo;


import com.fhs.basics.po.UcenterAppUserSetPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (UcenterAppUserSet)实体类
 *
 * @author miyaxin
 * @since 2022-08-11 09:29:44
 */

@Data
@NoArgsConstructor
@ApiModel(value ="UcenterAppUserSetVO",description ="UcenterAppUserSet参数")
public class UcenterAppUserSetVO extends UcenterAppUserSetPO implements VO {

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("邮箱")
    private String email;
}
    
