package com.fhs.supplier.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.base.valid.group.*;
import javax.validation.constraints.*;
import com.fhs.core.jsonfilter.anno.AutoArray;
import com.fhs.core.jsonfilter.serializer.Str2ArrayValueSerializer;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import org.hibernate.validator.constraints.Length;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 订单方状态管理(SupplierOrderStatus)实体类
 *
 * @author liangxiaotao
 * @since 2022-06-01 16:50:55
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_supplier_order_status")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="SupplierOrderStatusPO",description ="订单方状态管理")
public class SupplierOrderStatusPO extends BasePO<SupplierOrderStatusPO> {
    
    private static final long serialVersionUID = 462682198223189756L;

    @TableId(value = "id", type = IdType.NONE)
    @NotNull(message = "id字段不可为空", groups = {Update.class, Delete.class})
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 代码
     */
    @Length(message = "代码字段的长度最大为8", groups = {Add.class, Update.class}, max = 8)
    @TableField("order_status_code")
    @ApiModelProperty(value = "代码")
    private String orderStatusCode;

    /**
     * 名称
     */
    @Length(message = "名称字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("name")
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 是否启用（0否1是）
     */
    @TableField("is_enable")
    @ApiModelProperty(value = "是否启用（0否1是）")
    @Trans(type = TransType.DICTIONARY,key = "yesOrNo")
    private Integer isEnable;

    /**
     * 业务控制（0允许采购订货1允许采购收货2允许采购退货3允许采购退货出货）
     */
    @Length(message = "业务控制（0允许采购订货1允许采购收货2允许采购退货3允许采购退货出货）字段的长度最大为20", groups = {Add.class, Update.class}, max = 20)
    @TableField("business_control")
    @ApiModelProperty(value = "业务控制（0允许采购订货1允许采购收货2允许采购退货3允许采购退货出货）")
    @Trans(type = TransType.DICTIONARY,key = "businessControl")
    @AutoArray
    @JSONField(serializeUsing = Str2ArrayValueSerializer.class)
    private String businessControl;
}
