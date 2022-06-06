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
 * 订单方资料(SupplierOrderParty)实体类
 *
 * @author liangxiaotao
 * @since 2022-06-01 16:50:55
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_supplier_order_party")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="SupplierOrderPartyPO",description ="订单方资料")
public class SupplierOrderPartyPO extends BasePO<SupplierOrderPartyPO> {
    
    private static final long serialVersionUID = -72165279964146526L;

    @TableId(value = "id", type = IdType.NONE)
    @NotNull(message = "id字段不可为空", groups = {Update.class, Delete.class})
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 订单方名称
     */
    @Length(message = "订单方名称字段的长度最大为128", groups = {Add.class, Update.class}, max = 128)
    @TableField("name")
    @ApiModelProperty(value = "订单方名称")
    private String name;

    /**
     * 订单方代码
     */
    @Length(message = "订单方代码字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("order_party_code")
    @ApiModelProperty(value = "订单方代码")
    private String orderPartyCode;

    /**
     * 合同编号
     */
    @TableField("contract_code")
    @ApiModelProperty(value = "合同编号")
    private Long contractCode;

    /**
     * 所属供应商
     */
    @TableField("supplier_id")
    @ApiModelProperty(value = "所属供应商")
    @Trans(type = TransType.RPC,targetClassName = "com.fhs.supplier.po.SupplierSupplierPO", alias = "supplierCode", fields = "supplierCode", serviceName = "basic", dataSource = "basic")
    private Integer supplierId;

    /**
     * 订单方状态（0合作中1暂停合作2终止合作）
     */
    @TableField("status")
    @ApiModelProperty(value = "订单方状态（0合作中1暂停合作2终止合作）")
    @Trans(type = TransType.DICTIONARY,key = "status")
    private Integer status;

    /**
     * 合作方式（0经销1代理2）
     */
    @TableField("cooperation_ways")
    @ApiModelProperty(value = "合作方式（0经销1代理2）")
    @Trans(type = TransType.DICTIONARY,key = "cooperationWays")
    private Integer cooperationWays;

    /**
     * 所属大类（0烟1酒2茶叶）
     */
    @TableField("category_id")
    @ApiModelProperty(value = "所属大类（0烟1酒2茶叶）")
    @Trans(type = TransType.DICTIONARY,key = "categoryId")
    private Integer categoryId;

    /**
     * 负责人
     */
    @TableField("principal_id")
    @ApiModelProperty(value = "负责人")
    @Trans(type = TransType.RPC,targetClassName = "com.fhs.basics.po.UcenterMsUserPO", alias = "principalId", fields = "userName", serviceName = "basic", dataSource = "basic")
    private Long principalId;

    /**
     * 所属部门
     */
    @TableField("org_id")
    @ApiModelProperty(value = "所属部门")
    @Trans(type = TransType.RPC,targetClassName = "com.fhs.basics.po.UcenterMsOrganizationPO", alias = "orgId", fields = "name", serviceName = "basic", dataSource = "basic")
    private Long orgId;

    /**
     * 默认仓储（0唯客润1胖东来2华润）
     */
    @TableField("default_storage")
    @ApiModelProperty(value = "默认仓储（0唯客润1胖东来2华润）")
    @Trans(type = TransType.DICTIONARY,key = "defaultStorage")
    private Integer defaultStorage;

    /**
     * 默认仓位（0常温仓1低温仓）
     */
    @TableField("default_location")
    @ApiModelProperty(value = "默认仓位（0常温仓1低温仓）")
    @Trans(type = TransType.DICTIONARY,key = "defaultLocation")
    private Integer defaultLocation;

    /**
     * 结算模式（0按对账结算1按付款结算）
     */
    @TableField("settlement_pattern")
    @ApiModelProperty(value = "结算模式（0按对账结算1按付款结算）")
    @Trans(type = TransType.DICTIONARY,key = "settlementPattern")
    private Integer settlementPattern;

    /**
     * 付款条件ID
     */
    @TableField("payment_conditions")
    @ApiModelProperty(value = "付款条件ID")
    @Trans(type = TransType.RPC,targetClassName = "com.fhs.supplier.po.SupplierPaymentConditionsPO", alias = "paymentConditions", fields = "name", serviceName = "basic", dataSource = "basic")
    private Integer paymentConditions;

    /**
     * 对账条件ID
     */
    @TableField("reconciliation_conditions")
    @ApiModelProperty(value = "对账条件ID")
    @Trans(type = TransType.RPC,targetClassName = "com.fhs.supplier.po.SupplierReconciliationConditionsPO", alias = "reconciliationConditions", fields = "name", serviceName = "basic", dataSource = "basic")
    private Integer reconciliationConditions;

}
