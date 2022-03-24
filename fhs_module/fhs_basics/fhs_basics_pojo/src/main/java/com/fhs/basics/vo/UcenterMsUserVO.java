package com.fhs.basics.vo;

import com.fhs.core.base.pojo.vo.VO;
import com.fhs.basics.dox.UcenterMsUserDO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
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
@TransTypes(types = {TransType.WORD_BOOK, TransType.AUTO_TRANS,"pagex"})
@ApiModel(value = "UcenterMsUserVO", description = "UcenterMsUserVO参数")
public class UcenterMsUserVO extends UcenterMsUserDO implements VO {

    @ApiModelProperty("组织机构对应的公司id")
    private String companyId;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("部门名称")
    private String orgName;

}
