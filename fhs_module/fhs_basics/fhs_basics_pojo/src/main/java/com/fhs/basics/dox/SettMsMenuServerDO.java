package com.fhs.basics.dox;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.base.dox.BaseDO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.valid.group.Delete;
import com.fhs.core.valid.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 菜单服务(SettMsMenuServer)实体类
 *
 * @author user
 * @since 2020-05-18 15:09:29
 */

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_sett_ms_menu_server")
@ApiModel(value = "SettMsMenuServerDO", description = "SettMsMenuServer参数")
public class SettMsMenuServerDO extends BaseDO<SettMsMenuServerDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 服务编号
     */
    @TableId(value="id",type = IdType.ASSIGN_UUID)
    @NotNull(message = "id字段不可为null ", groups = {Update.class, Delete.class})
    @ApiModelProperty("服务编号id")
    private String id;
    /**
     * 服务名称
     */
    @TableField("server_name")
    @ApiModelProperty("服务名称")
    private String serverName;
    /**
     * 服务链接
     */
    @TableField("server_url")
    @ApiModelProperty("服务链接")
    private String serverUrl;

}
