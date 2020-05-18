package com.fhs.basics.dox;


import com.fhs.core.base.dox.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 模块(SettMsModel)实体类
 *
 * @author jackwong
 * @since 2020-05-18 15:10:29
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "t_sett_ms_model")
public class SettMsModelDO extends BaseDO<SettMsModelDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 模块编号
     */
    @Id
    private String id;
    /**
     * 模块名称
     */
    @Column(name = "model_name", nullable = true, length = 255)
    private String modelName;
    /**
     * 服务编号
     */
    @Column(name = "model_server_id", nullable = true, length = 32)
    private String modelServerId;


    @Override
    public String toString() {
        return "SysModel{" +
                "id=" + id +
                ", modelName=" + modelName +
                ", modelServerId=" + modelServerId +
                ", createUser=" + createUser +
                ", createTime=" + createTime +
                ", updateUser=" + updateUser +
                ", updateTime=" + updateTime +
                "}";
    }
}
