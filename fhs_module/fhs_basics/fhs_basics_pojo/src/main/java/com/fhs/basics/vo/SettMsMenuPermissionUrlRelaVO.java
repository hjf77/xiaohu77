package com.fhs.basics.vo;

import com.fhs.basics.po.SettMsMenuPermissionUrlRelaPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 菜单和URL对应关系
 *
 * @author user
 * @date 2020-05-18 15:33:38
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SettMsMenuPermissionUrlRelaVO", description = "SettMsMenuPermissionUrlRela参数")
public class SettMsMenuPermissionUrlRelaVO extends SettMsMenuPermissionUrlRelaPO implements VO {

}
