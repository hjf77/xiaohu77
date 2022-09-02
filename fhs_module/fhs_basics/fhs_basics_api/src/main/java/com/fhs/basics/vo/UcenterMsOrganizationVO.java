package com.fhs.basics.vo;

import com.fhs.basics.po.UcenterMsOrganizationPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 组织机构
 *
 * @author user
 * @date 2020-05-18 15:47:28
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UcenterMsOrganizationVO", description = "UcenterMsOrganization参数")
public class UcenterMsOrganizationVO extends UcenterMsOrganizationPO implements VO {

}