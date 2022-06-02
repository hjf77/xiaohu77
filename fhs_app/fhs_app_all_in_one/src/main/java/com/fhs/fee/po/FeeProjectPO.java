package com.fhs.fee.po;

import java.io.Serializable;

import com.fhs.common.tree.Treeable;
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
 * 费用项目(FeeProject)实体类
 *
 * @author yutao
 * @since 2022-05-31 14:50:23
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_fee_project")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="FeeProjectPO",description ="费用项目")
public class FeeProjectPO extends BasePO<FeeProjectPO> implements Treeable {
    
    private static final long serialVersionUID = -81325356050250978L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "id字段不可为空", groups = {Update.class, Delete.class})
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 费用项目名称
     */
    @Length(message = "费用项目名称字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("name")
    @ApiModelProperty(value = "费用项目名称")
    private String name;

    /**
     * 费用项目编码
     */
    @Length(message = "费用项目编码字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("project_code")
    @ApiModelProperty(value = "费用项目编码")
    private String projectCode;

    /**
     * 费用主体类型
     */
    @Trans(type = TransType.DICTIONARY, key = "costMainType")
    @TableField("main_type")
    @ApiModelProperty(value = "费用主体类型")
    private Integer mainType;

    /**
     * 税率
     */
    @Trans(type = TransType.DICTIONARY, key = "rate")
    @TableField("rate")
    @ApiModelProperty(value = "税率")
    private Integer rate;

    /**
     * 收付款类型（收付方向）
     */
    @Trans(type = TransType.DICTIONARY, key = "receiptsDirection")
    @TableField("proceeds_pay_type")
    @ApiModelProperty(value = "收付款类型（收付方向）")
    private Integer proceedsPayType;

    /**
     * 票扣
     */
    @Trans(type = TransType.DICTIONARY, key = "yesOrNo")
    @TableField("ticket_deduct")
    @ApiModelProperty(value = "票扣")
    private Integer ticketDeduct;

    /**
     * 收款方式
     */
    @Trans(type = TransType.DICTIONARY, key = "paymentMethods")
    @TableField("proceeds_method")
    @ApiModelProperty(value = "收款方式")
    private Integer proceedsMethod;

    /**
     * 状态
     */
    @Trans(type = TransType.DICTIONARY, key = "is_enable")
    @TableField("status")
    @ApiModelProperty(value = "状态")
    private Integer status;

    /**
     * 备注
     */
    @Length(message = "备注字段的长度最大为256", groups = {Add.class, Update.class}, max = 256)
    @TableField("remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 固定金额
     */
    @TableField("fixed_amount")
    @ApiModelProperty(value = "固定金额")
    private Double fixedAmount;

    /**
     * 固定去税金额
     */
    @TableField("fixed_amount_go_tax")
    @ApiModelProperty(value = "固定去税金额")
    private Double fixedAmountGoTax;

    /**
     * 结案方式
     */
    @Trans(type = TransType.DICTIONARY, key = "settlementWay")
    @TableField("settlement_way")
    @ApiModelProperty(value = "结案方式")
    private Integer settlementWay;

    /**
     * 结案数据来源 多个逗号分隔
     */
    @Length(message = "结案数据来源 多个逗号分隔字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("settlement_datasource")
    @ApiModelProperty(value = "结案数据来源 多个逗号分隔")
    private String settlementDatasource;

    /**
     * 等级
     */
    @TableField("level")
    @ApiModelProperty(value = "等级")
    private Integer level;

    /**
     * 类别（父id）
     */
    @TableField("parent_id")
    @ApiModelProperty(value = "类别（父id）")
    private Integer parentId;


    @Override
    public Serializable getTreeNodeParentId() {
        return this.parentId;
    }

    @Override
    public Serializable getTreeNodeId() {
        return this.id;
    }

    @Override
    public String getTreeNodeName() {
        return this.projectCode + this.name;
    }
}
