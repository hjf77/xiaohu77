package com.fhs.fee.po;

import java.util.Date;
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
 * 费用方案(FeePlan)实体类
 *
 * @author wanglei
 * @since 2022-06-01 15:42:19
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_fee_plan")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FeePlanPO", description = "费用方案")
public class FeePlanPO extends BasePO<FeePlanPO> {

    private static final long serialVersionUID = 444975079415076041L;

    @TableId(value = "id", type = IdType.NONE)
    @NotNull(message = "${column.comment}字段不可为空", groups = {Update.class, Delete.class})
    @ApiModelProperty(value = "")
    private Long id;

    /**
     * 单号
     */
    @Length(message = "单号字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("no")
    @ApiModelProperty(value = "单号")
    private String no;

    /**
     * 订单状态（字典）：0未审核，1审核
     */
    @TableField("status")
    @ApiModelProperty(value = "订单状态（字典）：0未审核，1审核")
    private Integer status;

    /**
     * 订单方id
     */
    @TableField("order_party_id")
    @ApiModelProperty(value = "订单方id")
    private Integer orderPartyId;

    /**
     * 供应商id
     */
    @Trans(type = TransType.RPC, targetClassName = "com.fhs.supplier.po.SupplierSupplierPO", alias = "supplierId", fields = {"name", "supplierCode"}, serviceName = "basic", dataSource = "basic")
    @TableField("supplier_id")
    @ApiModelProperty(value = "供应商id")
    private Integer supplierId;

    /**
     * 采购合同id
     */
    @TableField("order_id")
    @ApiModelProperty(value = "采购合同id")
    private Long orderId;

    /**
     * 费用项目id
     */
    @Trans(type = TransType.RPC, targetClassName = "com.fhs.fee.po.FeeProjectPO", alias = "feeProjectId", fields = {"name", "projectCode"}, serviceName = "basic", dataSource = "basic")
    @TableField("fee_project_id")
    @ApiModelProperty(value = "费用项目id")
    private Integer feeProjectId;

    /**
     * 费用开始时间
     */
    @TableField("start_time")
    @ApiModelProperty(value = "费用开始时间")
    private Date startTime;

    /**
     * 费用结束时间
     */
    @TableField("end_time")
    @ApiModelProperty(value = "费用结束时间")
    private Date endTime;

    /**
     * 所属组织id
     */
    @TableField("organization_id")
    @ApiModelProperty(value = "所属组织id")
    private Long organizationId;


}
