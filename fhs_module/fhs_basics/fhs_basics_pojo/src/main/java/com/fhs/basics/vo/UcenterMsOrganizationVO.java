package com.fhs.basics.vo;

import com.fhs.basics.dox.UcenterMsOrganizationDO;
import com.fhs.core.base.pojo.vo.VO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 组织机构
 * @author user
 * @date 2020-05-18 15:47:28
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TransTypes(types = {TransType.WORD_BOOK, TransType.AUTO_TRANS})
@ApiModel(value ="UcenterMsOrganizationVO",description ="UcenterMsOrganization参数")
public class UcenterMsOrganizationVO extends UcenterMsOrganizationDO implements VO {

}
