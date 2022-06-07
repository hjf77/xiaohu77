package com.fhs.supplier.po;

import com.fhs.core.base.po.BasePO;
import com.fhs.core.base.valid.group.*;
import javax.validation.constraints.*;

import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import org.hibernate.validator.constraints.Length;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 供应商付款条件(SupplierPaymentConditions)实体类
 *
 * @author liangxiaotao
 * @since 2022-06-06 11:09:11
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_supplier_payment_conditions")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="SupplierPaymentConditionsPO",description ="供应商付款条件")
public class SupplierPaymentConditionsPO extends BasePO<SupplierPaymentConditionsPO> {
    
    private static final long serialVersionUID = 866377902330210223L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "id字段不可为空", groups = {Update.class, Delete.class})
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 付款条件代码
     */
    @Length(message = "付款条件代码字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("conditions_code")
    @ApiModelProperty(value = "付款条件代码")
    private String conditionsCode;

    /**
     * 付款条件名称
     */
    @Length(message = "付款条件名称字段的长度最大为128", groups = {Add.class, Update.class}, max = 128)
    @TableField("name")
    @ApiModelProperty(value = "付款条件名称")
    private String name;

    /**
     * 说明
     */
    @Length(message = "说明字段的长度最大为256", groups = {Add.class, Update.class}, max = 256)
    @TableField("remark")
    @ApiModelProperty(value = "说明")
    private String remark;

    /**
     * 付款天数
     */
    @TableField("days")
    @ApiModelProperty(value = "付款天数")
    private Integer days;

    /**
     * 付款日期基数（0结转自然周1结转自然半月2结转自然月3固定日4无）
     */
    @TableField("date_base")
    @Trans(type= TransType.DICTIONARY,key = "supplierPaymentDateBase")
    @ApiModelProperty(value = "付款日期基数（0结转自然周1结转自然半月2结转自然月3固定日4无）")
    private Integer dateBase;

    /**
     * 固定日天数
     */
    @TableField("date_base_day")
    @ApiModelProperty(value = "固定日天数")
    private Integer dateBaseDay;

    /**
     * 付款日期基数缺省值（0发生日期1开票日期）
     */
    @TableField("date_base_omit")
    @Trans(type= TransType.DICTIONARY,key = "supplierPaymentDateBaseOmit")
    @ApiModelProperty(value = "付款日期基数缺省值（0发生日期1开票日期）")
    private Integer dateBaseOmit;



}
