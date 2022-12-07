package com.fhs.basics.vo;

import com.fhs.basics.po.SettMsMenuPO;
import com.fhs.core.trans.vo.VO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 系统菜单vo
 *
 * @author user
 * @date 2020-05-18 15:36:38
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SettMsMenuVO", description = "SettMsMenu参数")
public class SettMsMenuVO extends SettMsMenuPO implements VO {
}
