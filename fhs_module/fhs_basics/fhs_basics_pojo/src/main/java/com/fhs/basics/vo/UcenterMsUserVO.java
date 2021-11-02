package com.fhs.basics.vo;

import com.fhs.core.trans.vo.VO;
import com.fhs.basics.po.UcenterMsUserPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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

}
