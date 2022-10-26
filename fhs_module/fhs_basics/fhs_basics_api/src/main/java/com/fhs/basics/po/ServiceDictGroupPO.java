package com.fhs.basics.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.base.anno.NotRepeatDesc;
import com.fhs.core.base.anno.NotRepeatField;
import com.fhs.core.base.po.BasePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.fhs.core.base.valid.group.Add;
import com.fhs.core.base.valid.group.Delete;
import com.fhs.core.base.valid.group.Update;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_service_dict_group", autoResultMap = true)
@NotRepeatDesc("分组编码不可重复")
@ApiModel(description = "字典分组参数")
public class ServiceDictGroupPO extends BasePO<ServiceDictGroupPO> {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7194228955295169250L;

    /**
     * ID
     */
    @TableId(value = "group_id", type = IdType.NONE)
    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("主键id")
    private Integer groupId;


    /**
     * 分组名称
     */
    @NotEmpty(message = "分组名称不能为空", groups = {Update.class, Add.class, Delete.class})
    @TableField("group_name")
    @ApiModelProperty("分组名称")
    private String groupName;

    /**
     * 分组编码
     */
    @NotEmpty(message = "分组编码不能为空", groups = {Update.class, Add.class, Delete.class})
    @TableField("group_code")
    @ApiModelProperty("分组编码")
    @NotRepeatField
    private String groupCode;

}
