/*
 * 文 件 名:  AdminMenu.java
 * 版    权:  sxpartner Technology International Ltd.
 * 描    述:  &lt;描述&gt;.
 * 修 改 人:  wanglei
 * 修改时间:  ${date}
 * 跟踪单号:  &lt;跟踪单号&gt;
 * 修改单号:  &lt;修改单号&gt;
 * 修改内容:  &lt;修改内容&gt;
 */
package com.fhs.basics.dox;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fhs.common.utils.CheckUtils;
import com.fhs.core.base.dox.BaseDO;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.valid.group.Add;
import com.fhs.core.valid.group.Delete;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.sf.jsqlparser.statement.update.Update;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单(SettMsMenu)实体类
 *
 * @author 朱俊
 * @version [版本号, 2015/08/13 11:37:31]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Entity
@Data
@Table(name = "t_sett_ms_menu")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SettMsMenuDO", description = "SettMsMenu参数")
public class SettMsMenuDO extends BaseDO<SettMsMenuDO> {


    private static final long serialVersionUID = 1L;

    /**
     * 菜单id
     */
    @Id
    @NotNull(message = "{test.menuId.null}", groups = {Update.class, Delete.class})
    @Max(message = "{test.menuId.max}", value = 2147483647, groups = {Add.class, Update.class})
    @Min(message = "{test.menuId.min}", value = 0, groups = {Add.class, Update.class})
    @ApiModelProperty("菜单id")
    private Integer menuId;


    /**
     * 菜单名称
     */
    @NotNull(message = "{test.menuName.null}", groups = {Update.class, Add.class})
    @Length(message = "{test.menuName.length}", max = 20, min = 0)
    @ApiModelProperty("菜单名称")
    private String menuName;

    /**
     * 父菜单id
     */
    @NotNull(message = "{test.fatherMenuId.null}", groups = {Update.class, Add.class})
    @Max(message = "{test.fatherMenuId.max}", value = 2147483647, groups = {Add.class, Update.class})
    @Min(message = "{test.fatherMenuId.min}", value = -2147483648, groups = {Add.class, Update.class})
    @ApiModelProperty("父菜单id")
    private Integer fatherMenuId;

    /**
     * 服务器地址
     */
    @TableField(exist = false)
    @ApiModelProperty("服务器地址")
    private String serverUrl;

    /**
     * 链接地址
     */
    @ApiModelProperty("链接地址")
    private String menuUrl;

    /**
     * Namespace
     */
    @ApiModelProperty("namespace")
    private String namespace;

    /**
     * 菜单等级-1根0一级1二级2三级....
     */
    @TableField(exist = false)
    @ApiModelProperty("菜单等级")
    private Integer menuLevel;

    /**
     * 所属系统
     */
    @ApiModelProperty("所属系统")
    private String systemId;


    /**
     * 菜单类型0 物业公司菜单1 物业集团菜单2 平台菜单
     */
    @Trans(type = TransType.WORD_BOOK, key = "menu_type")
    @ApiModelProperty("菜单类型")
    private String menuType;


    @TableField(exist = false)
    private String[] menuTypes;

    /**
     * 适配的主页菜单
     */
    @TableField(exist = false)
    @ApiModelProperty("适配的主页菜单")
    private String configurationHomeMenu;

    @TableField(exist = false)
    private String[] configurationHomeMenus;


    public String[] getConfigurationHomeMenus() {
        return configurationHomeMenus;
    }

    public void setConfigurationHomeMenus(String[] configurationHomeMenus) {
        this.configurationHomeMenus = configurationHomeMenus;
    }

    public String getConfigurationHomeMenu() {
        return configurationHomeMenu;
    }

    public void setConfigurationHomeMenu(String configurationHomeMenu) {
        this.configurationHomeMenu = configurationHomeMenu;
        if (CheckUtils.isNullOrEmpty(configurationHomeMenu)) {
            configurationHomeMenus = new String[0];
            return;
        }
        this.configurationHomeMenus = configurationHomeMenu.split(",");
    }

    /**
     * 是否禁用 0:启用 1:禁用
     */
    @NotNull(message = "{test.isDisable.null}", groups = {Update.class, Add.class})
    @Max(message = "{test.isDisable.max}", value = 2147483647, groups = {Add.class, Update.class})
    @Min(message = "{test.isDisable.min}", value = -2147483648, groups = {Add.class, Update.class})
    @ApiModelProperty("是否禁用")
    @Trans(type = TransType.WORD_BOOK, key = "is_enable")
    private Integer isEnable;


    private String checkHelpUrl;

    /**
     * 子菜单
     */
    @TableField(exist = false)
    @ApiModelProperty("子菜单")
    @Trans(type = TransType.AUTO_TRANS, key = "sett_ms_system")
    private List<SettMsMenuDO> sonMenu;

    /**
     * 菜单状态
     */
    @ApiModelProperty("菜单状态")
    @Trans(type = TransType.WORD_BOOK, key = "yesOrNo")
    private Integer menuState;

    public List<SettMsMenuDO> getSonMenu() {
        if (sonMenu == null) {
            sonMenu = new ArrayList<>();
        }
        return sonMenu;
    }

    /**
     * 图片
     */
    @ApiModelProperty("图片")
    private String image;

    /**
     * 是否关注
     */
    @ApiModelProperty("是否关注")
    private String isAttention;

    private Integer serverNameId = 1;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer orderIndex;

    /**
     * 排序
     */
    @ApiModelProperty("图标")
    private String icon;

    /**
     * 给菜单类型0 运营菜单 1 园区菜单
     */
    public void setMenuType(String menuType) {
        this.menuType = menuType;
        if (CheckUtils.isNullOrEmpty(menuType)) {
            menuTypes = new String[0];
            return;
        }
        this.menuTypes = menuType.split(",");
    }


}
