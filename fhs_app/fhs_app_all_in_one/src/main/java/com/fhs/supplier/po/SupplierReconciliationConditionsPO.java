package com.fhs.supplier.po;

import java.io.Serializable;
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
 * 供应商对账条件配置(SupplierReconciliationConditions)实体类
 *
 * @author wanglei
 * @since 2022-06-06 09:43:27
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_supplier_reconciliation_conditions")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="SupplierReconciliationConditionsPO",description ="供应商对账条件配置")
public class SupplierReconciliationConditionsPO extends BasePO<SupplierReconciliationConditionsPO> {

    private static final long serialVersionUID = 753876570499997481L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "id字段不可为空", groups = {Update.class, Delete.class})
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 对账条件代码
     */
    @Length(message = "对账条件代码字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("conditions_code")
    @ApiModelProperty(value = "对账条件代码")
    private String conditionsCode;

    /**
     * 对账条件名称
     */
    @Length(message = "对账条件名称字段的长度最大为128", groups = {Add.class, Update.class}, max = 128)
    @TableField("name")
    @ApiModelProperty(value = "对账条件名称")
    private String name;

    /**
     * 说明
     */
    @Length(message = "说明字段的长度最大为256", groups = {Add.class, Update.class}, max = 256)
    @TableField("remark")
    @ApiModelProperty(value = "说明")
    private String remark;

    /**
     * 对账天数
     */
    @TableField("days")
    @ApiModelProperty(value = "对账天数")
    private Integer days;

    /**
     * 对账日期基数（0自然周1自然半月2自然月3无）
     */
    @TableField("date_base")
    @Trans(type= TransType.DICTIONARY,key = "supplierReconciliationDateBase")
    @ApiModelProperty(value = "对账日期基数（0自然周1自然半月2自然月3无）")
    private Integer dateBase;

    /**
     * 对账日期基数缺省值（0发生日期1创建日期2无）
     */
    @TableField("date_base_omit")
    @Trans(type= TransType.DICTIONARY,key = "supplierReconciliationDateBaseOmit")
    @ApiModelProperty(value = "对账日期基数缺省值（0发生日期1创建日期2无）")
    private Integer dateBaseOmit;



}
