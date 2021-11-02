package com.fhs.basics.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.base.anno.NotRepeatDesc;
import com.fhs.core.base.anno.NotRepeatField;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.valid.group.Add;
import com.fhs.core.valid.group.Delete;
import com.fhs.core.valid.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 字典实体类
 *
 * @author wanglei
 * @version [版本号, 2015年8月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_service_dict_item")
@EqualsAndHashCode(callSuper = true)
@NotRepeatDesc("字典编码不可重复")
@ApiModel(description = "字典项")
public class ServiceDictItemPO extends BasePO<ServiceDictItemPO> {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4651593237917581586L;

    /**
     * id
     */
    @TableId(value = "dict_id", type = IdType.AUTO)
    @ApiModelProperty("主键id")
    private Integer dictId;


    /**
     * 字典code
     */
    @NotNull(message = "字典编码", groups = {Update.class, Delete.class})
    @Length(message = "字典code字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("dict_code")
    @ApiModelProperty("字典code")
    @NotRepeatField
    private String dictCode;

    /**
     * 字典解释/描述
     */
    @NotNull(message = "字典解释/描述", groups = {Add.class,Update.class, Delete.class})
    @TableField("dict_desc")
    @ApiModelProperty("字典解释/描述")
    private String dictDesc;

    /**
     * 扩展字段
     */
    @TableField("ext")
    @ApiModelProperty("扩展字段")
    private String ext;


    /**
     * 字典分组code
     */
    @NotRepeatField
    @NotNull(message = "字典分组code不可为null", groups = {Update.class, Delete.class})
    @TableField("dict_group_code")
    @ApiModelProperty("字典分组code")
    private String dictGroupCode;

    /**
     * 排序字段
     */
    @Max(message = "orderNum字段大于int最大值", value = 2147483647, groups = {Add.class, Update.class})
    @Min(message = "orderNum字段小于int最小值", value = -2147483648, groups = {Add.class, Update.class})
    @TableField("order_num")
    @ApiModelProperty("排序字段")
    private Integer orderNum;


}
