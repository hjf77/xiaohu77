package com.fhs.basics.vo;

import com.fhs.basics.po.SettMsMenuPermissionPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 菜单权限vo
 *
 * @author user
 * @date 2020-05-18 15:33:48
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SettMsMenuPermissionVO", description = "SettMsMenuPermission参数")
public class SettMsMenuPermissionVO extends SettMsMenuPermissionPO implements VO {

}
