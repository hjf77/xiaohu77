package com.fhs.basics.vo;

import com.fhs.common.utils.StringUtils;
import com.fhs.core.trans.vo.VO;
import com.fhs.basics.po.UcenterMsUserPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Map;

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

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("顶级单位")
    private String company;

    @ApiModelProperty("顶级单位的下一级单位")
    private String oilpro;

    @ApiModelProperty("三级单位")
    private String opeArea;

    @ApiModelProperty("四级单位")
    private String centralStat;

    /**
     * 组织单位拼接
     * @return
     */
    public String getOrgName(){
        StringBuilder sb = new StringBuilder();
        if (this.getOrganizationId().length()==3){
            return centralStat;
        }
        if (!StringUtils.isEmpty(this.getCompany())){
            sb.append(this.getCompany()).append("/");
        }
        if (!StringUtils.isEmpty(this.getOilpro())){
            sb.append(this.getOilpro()).append("/");
        }
        if (!StringUtils.isEmpty(this.getOpeArea())){
            sb.append(this.getOpeArea()).append("/");
        }
        if (!StringUtils.isEmpty(this.getCentralStat())){
            sb.append(this.getCentralStat()).append("/");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();

    }


}
