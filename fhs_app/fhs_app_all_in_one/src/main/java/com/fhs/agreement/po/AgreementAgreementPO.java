package com.fhs.agreement.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.agreement.vo.AgreementGoodsSettVO;
import com.fhs.basics.constant.BaseTransConstant;
import com.fhs.basics.po.UcenterMsOrganizationPO;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.base.valid.group.Add;
import com.fhs.core.base.valid.group.Delete;
import com.fhs.core.base.valid.group.Update;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.trans.vo.TransPojo;
import com.fhs.supplier.po.SupplierOrderPartyPO;
import com.fhs.supplier.po.SupplierSupplierPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 采购协议(AgreementAgreement)实体类
 *
 * @author caiyu
 * @since 2022-06-01 10:09:46
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_agreement_agreement")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="AgreementAgreementPO",description ="采购协议")
public class AgreementAgreementPO extends BasePO<AgreementAgreementPO> {
    
    private static final long serialVersionUID = 150133574788971137L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "id字段不可以为空", groups = {Update.class, Delete.class})
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 协议号
     */
    @Length(message = "协议号字段的长度最大为20", groups = {Add.class, Update.class}, max = 20)
    @TableField("no")
    @ApiModelProperty(value = "协议号")
    private String no;

    /**
     * 状态 0未审核 1已审核 2已驳回
     */
    @TableField("status")
    @ApiModelProperty(value = "状态 0未审核 1已审核 2已驳回")
    @Trans(type = TransType.DICTIONARY, key = "agreementState")
    private Integer status;

    /**
     * 订单方id
     */
    @TableField("order_party_id")
    @ApiModelProperty(value = "订单方id")
    @Trans(type = TransType.RPC, targetClassName = "com.fhs.supplier.po.SupplierOrderPartyPO", alias = "orderPartyId", fields = {"name", "orderPartyCode"}, serviceName = "basic", dataSource = "basic")
    private Integer orderPartyId;

    /**
     * 供应商id
     */
    @TableField("supplier_id")
    @ApiModelProperty(value = "供应商id")
    @Trans(type = TransType.RPC, targetClassName = "com.fhs.supplier.po.SupplierSupplierPO", alias = "supplierId", fields = {"name", "supplierCode"}, serviceName = "basic", dataSource = "basic")
    private Integer supplierId;

    /**
     * 合同编号
     */
    @Length(message = "合同编号字段的长度最大为64", groups = {Add.class, Update.class}, max = 64)
    @TableField("contract_no")
    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    /**
     * 明细数
     */
    @TableField("detail_num")
    @ApiModelProperty(value = "明细数")
    private Integer detailNum;

    /**
     * 所属组织
     */
    @TableField("org_id")
    @ApiModelProperty(value = "所属组织")
    @Trans(type = TransType.AUTO_TRANS, key = BaseTransConstant.ORG)
    private String orgId;

    /**
     * 开始时间
     */
    @TableField("start_time")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 备注
     */
    @Length(message = "备注字段的长度最大为200", groups = {Add.class, Update.class}, max = 200)
    @TableField("remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 商品所有id
     */
    @Length(message = "商品ids", groups = {Add.class, Update.class}, max = 200)
    @TableField("goods_id")
    @ApiModelProperty(value = "商品ids")
    private String goodsId;

    /**
     * 商品
     */
    @TableField(exist = false)
    private AgreementGoodsSettVO[] goodsVOs;



}
