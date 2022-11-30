package com.fhs.basics.po;

import java.io.Serializable;

import com.fhs.core.base.po.BasePO;
import com.fhs.core.base.valid.group.*;

import javax.validation.constraints.*;

import org.hibernate.validator.constraints.Length;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 用户自定义列信息配置(SettUserColumn)实体类
 *
 * @author wanglei
 * @since 2022-10-13 11:46:52
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_sett_user_column",autoResultMap = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SettUserColumnPO", description = "用户自定义列信息配置")
public class SettUserColumnPO extends BasePO<SettUserColumnPO> {

    private static final long serialVersionUID = 934032023194445970L;

    @TableId(value = "id", type = IdType.NONE)
    @ApiModelProperty(value = "")
    private Long id;


    @TableField("namespace")
    @NotBlank(message = "命名空间不能为空")
    @ApiModelProperty(value = "命名空间")
    private String namespace;

    @TableField("column_json")
    @NotBlank(message = "配置json不能为空")
    @ApiModelProperty(value = "${column.comment}")
    private String columnJson;


}
