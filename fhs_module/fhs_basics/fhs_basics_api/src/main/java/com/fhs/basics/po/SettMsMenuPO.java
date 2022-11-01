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
package com.fhs.basics.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.common.tree.Treeable;
import com.fhs.common.utils.CheckUtils;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.base.valid.group.Add;
import com.fhs.core.base.valid.group.Delete;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.sf.jsqlparser.statement.update.Update;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
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
@Data
@TableName(value = "t_sett_ms_menu", autoResultMap = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SettMsMenuDO", description = "SettMsMenu参数")
public class SettMsMenuPO extends BasePO<SettMsMenuPO> implements Treeable {


    private static final long serialVersionUID = 1L;

    /**
     * 菜单id
     */
    @TableId(value = "menu_id", type = IdType.ASSIGN_UUID)
    @NotNull(message = "id不可为空", groups = {Update.class, Delete.class})
    @ApiModelProperty("菜单id")
    private String menuId;


    /**
     * 菜单名称
     */
    @ApiModelProperty("菜单名称")
    @TableField("menu_name")
    @NotNull(message = "菜单名称不可为空", groups = {Update.class, Add.class})
    private String menuName;

    /**
     * 父菜单id
     */
    @TableField("father_menu_id")
    @NotNull(message = "父菜单不可为空", groups = {Update.class, Add.class})
    @ApiModelProperty("父菜单id")
    private String fatherMenuId;


    /**
     * 链接地址
     */
    @TableField("menu_url")
    @ApiModelProperty("链接地址")
    private String menuUrl;

    /**
     * Namespace
     */
    @TableField("namespace")
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
    @TableField("system_id")
    @ApiModelProperty("所属系统")
    private String systemId;


    /**
     * 菜单类型 菜单类型 0 运营菜单  1 租户菜单 2 共享菜单
     */
    @TableField("menu_type")
    @Trans(type = TransType.DICTIONARY, key = "menu_type")
    @ApiModelProperty("菜单类型")
    private Integer menuType;


    @TableField(exist = false)
    private String[] menuTypes;


    /**
     * 是否禁用 0:启用 1:禁用
     */
    @NotNull(message = "isEnable不能为空", groups = {Update.class, Add.class})
    @ApiModelProperty("是否禁用")
    @Trans(type = TransType.DICTIONARY, key = "isEnable")
    private Integer isEnable;


    /**
     * 子菜单
     */
    @TableField(exist = false)
    @ApiModelProperty("子菜单")
    @Trans(type = TransType.AUTO_TRANS, key = "sett_ms_system")
    private List<SettMsMenuPO> sonMenu;

    /**
     * 菜单状态
     */
    @ApiModelProperty("菜单状态")
    @Trans(type = TransType.DICTIONARY, key = "yesOrNo")
    private Integer isShow;


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


    @Override
    public Serializable getTreeNodeParentId() {
        return this.fatherMenuId;
    }

    @Override
    public String getName() {
        return this.menuName;
    }

    @Override
    public Serializable getTreeNodeId() {
        return this.menuId;
    }


}
