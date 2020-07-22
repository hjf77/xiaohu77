package com.fhs.basics.dox;

import com.fhs.core.base.dox.BaseDO;
import com.mybatis.jpa.annotation.Like;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import com.fhs.core.valid.group.Add;
import com.fhs.core.valid.group.Delete;
import com.fhs.core.valid.group.Update;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 字典分组
 *
 * @author nanshouxiao
 * @version [版本号, 2015/12/22 15:13:20]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "t_service_wordbook_group")
@ApiModel(value = "ServiceWordbookGroupDO", description = "ServiceWordbookGroup参数")
public class ServiceWordbookGroupDO extends BaseDO<ServiceWordbookGroupDO> {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7194228955295169250L;

    /**
     * ID
     */
    @Id
    @NotNull(message = "{wordbook.groupId.null}", groups = {Update.class, Delete.class})
    @Max(message = "{wordbook.groupId.max}", value = 2147483647, groups = {Delete.class, Update.class})
    @Min(message = "{wordbook.groupId.min}", value = -2147483648, groups = {Delete.class, Update.class})
    @ApiModelProperty("主键id")
    private Integer groupId;

    /**
     * 加密ID
     */
    @NotNull(message = "{wordbook.groupIdE.null}")
    @Transient
    @ApiModelProperty("加密id")
    private String groupIdE;

    /**
     * 分组名称
     */
    @Like
    @NotNull(message = "groupName字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "groupName字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @Column(name = "group_name")
    @ApiModelProperty("分组名称")
    private String groupName;

    /**
     * 分组编码
     */
    @Like
    @NotNull(message = "wordbookGroupCode字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "wordbookGroupCode字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @Column(name = "wordbook_group_code")
    @ApiModelProperty("分组编码")
    private String wordbookGroupCode;

}
