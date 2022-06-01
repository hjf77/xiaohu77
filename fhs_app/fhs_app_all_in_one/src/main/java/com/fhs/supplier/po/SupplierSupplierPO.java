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
 * 供应商(SupplierSupplier)实体类
 *
 * @author liangxiaotao
 * @since 2022-05-31 14:15:01
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_supplier_supplier")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="SupplierSupplierPO",description ="供应商")
public class SupplierSupplierPO extends BasePO<SupplierSupplierPO> {
    
    private static final long serialVersionUID = -80514871843441784L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "供应商代码字段不可为空", groups = {Update.class, Delete.class})
    @ApiModelProperty(value = "供应商代码")
    private Integer id;

    /**
     * 供应商名称
     */
    @Length(message = "供应商名称字段的长度最大为128", groups = {Add.class, Update.class}, max = 128)
    @TableField("name")
    @ApiModelProperty(value = "供应商名称")
    private String name;

    /**
     * 供应商编码
     */
    @Length(message = "供应商编码字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("code")
    @ApiModelProperty(value = "供应商编码")
    private String code;

    /**
     * 简称
     */
    @Length(message = "简称字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("short_name")
    @ApiModelProperty(value = "简称")
    private String shortName;

    /**
     * 供应商状态
     */
    @TableField("status")
    @ApiModelProperty(value = "供应商状态")
    @Trans(type = TransType.DICTIONARY,key = "supplierState")
    private Integer status;

    /**
     * 进项税率
     */
    @TableField("rate")
    @ApiModelProperty(value = "进项税率")
    @Trans(type = TransType.DICTIONARY,key = "InputTaxRate")
    private Integer rate;

    /**
     * 供应商等级
     */
    @TableField("level")
    @ApiModelProperty(value = "供应商等级")
    @Trans(type = TransType.DICTIONARY,key = "supplierLevel")
    private Integer level;

    /**
     * 供应商类型
     */
    @TableField("supplier_type")
    @ApiModelProperty(value = "供应商类型")
    @Trans(type = TransType.DICTIONARY,key = "supplierType")
    private Integer supplierType;

    /**
     * 联系人
     */
    @Length(message = "联系人字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("contact")
    @ApiModelProperty(value = "联系人")
    private String contact;

    /**
     * 联系人电话
     */
    @Length(message = "联系人电话字段的长度最大为20", groups = {Add.class, Update.class}, max = 20)
    @TableField("contact_mobile")
    @ApiModelProperty(value = "联系人电话")
    private String contactMobile;

    /**
     * 企业性质
     */
    @TableField("nature")
    @ApiModelProperty(value = "企业性质")
    private Integer nature;

    /**
     * 企业法人
     */
    @Length(message = "企业法人字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("legal_person")
    @ApiModelProperty(value = "企业法人")
    private String legalPerson;

    /**
     * 法人身份证号
     */
    @Length(message = "法人身份证号字段的长度最大为20", groups = {Add.class, Update.class}, max = 20)
    @TableField("id_no")
    @ApiModelProperty(value = "法人身份证号")
    private String idNo;

    /**
     * 企业类型
     */
    @TableField("company_type")
    @ApiModelProperty(value = "企业类型")
    @Trans(type = TransType.DICTIONARY,key = "enterpriseType")
    private Integer companyType;

    /**
     * 公司电话
     */
    @Length(message = "公司电话字段的长度最大为20", groups = {Add.class, Update.class}, max = 20)
    @TableField("company_mobile")
    @ApiModelProperty(value = "公司电话")
    private String companyMobile;

    /**
     * 注册资金
     */
    @TableField("registered_capital")
    @ApiModelProperty(value = "注册资金")
    private Integer registeredCapital;

    /**
     * 供应商邮箱
     */
    @Length(message = "供应商邮箱字段的长度最大为256", groups = {Add.class, Update.class}, max = 256)
    @TableField("email")
    @ApiModelProperty(value = "供应商邮箱")
    private String email;

    /**
     * 省份id
     */
    @TableField("province")
    @ApiModelProperty(value = "省份id")
    private Integer province;

    /**
     * 市id
     */
    @TableField("city")
    @ApiModelProperty(value = "市id")
    private Integer city;

    /**
     * 区县 id
     */
    @TableField("area")
    @ApiModelProperty(value = "区县 id")
    private Integer area;

    /**
     * 经营品牌
     */
    @Length(message = "经营品牌字段的长度最大为128", groups = {Add.class, Update.class}, max = 128)
    @TableField("brand")
    @ApiModelProperty(value = "经营品牌")
    private String brand;

    /**
     * 地址
     */
    @Length(message = "地址字段的长度最大为256", groups = {Add.class, Update.class}, max = 256)
    @TableField("address")
    @ApiModelProperty(value = "地址")
    private String address;

    /**
     * 营业执照id
     */
    @TableField("business_license")
    @ApiModelProperty(value = "营业执照id")
    private Long businessLicense;

    /**
     * 收款账号
     */
    @Length(message = "收款账号字段的长度最大为64", groups = {Add.class, Update.class}, max = 64)
    @TableField("account_num")
    @ApiModelProperty(value = "收款账号")
    private String accountNum;



}
