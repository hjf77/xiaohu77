package com.fhs.basics.dox;


import com.fhs.core.base.dox.BaseDO;
import com.fhs.core.trans.anno.TransTypes;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.valid.group.Delete;
import com.fhs.core.valid.group.Update;
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
@TransTypes(types = {TransType.WORD_BOOK})
@Table(name = "t_sett_ms_menu_server")
public class SettMsMenuServerDO extends BaseDO<SettMsMenuServerDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 服务编号
     */
    @Id
    @NotNull(message = "id字段不可为null ", groups = {Update.class, Delete.class})
    private String id;
    /**
     * 服务名称
     */
    @Column(name = "server_name", nullable = true, length = 255)
    private String serverName;
    /**
     * 服务链接
     */
    @Column(name = "server_url", nullable = true, length = 500)
    private String serverUrl;

}
