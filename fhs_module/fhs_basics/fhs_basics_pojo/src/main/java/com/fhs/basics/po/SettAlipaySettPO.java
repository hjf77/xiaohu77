package com.fhs.basics.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.valid.group.Add;
import com.fhs.core.valid.group.Delete;
import com.fhs.core.valid.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * 支付宝设置(settAlipaySett)实体类
 *
 * @author wanglei
 * @since 2019-03-19 16:10:29
 */

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_sett_alipay_sett")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SettAlipaySettDO", description = "SettAlipaySett参数")
public class SettAlipaySettPO extends BasePO<SettAlipaySettPO> {
    private static final long serialVersionUID = -22527353735624120L;
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 服务号名称
     */
    @NotEmpty
    @NotNull(message = "服务号名称字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "服务号名称字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("name")
    @ApiModelProperty("服务号名称")
    private String name;

    /**
     * appid
     */
    @NotEmpty
    @NotNull(message = "appid字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "appid字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("app_id")
    @ApiModelProperty("appid")
    private String appId;

    /**
     * 应用公钥
     */
    @NotEmpty
    @NotNull(message = "应用公钥字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "应用公钥字段的长度最大为1000", groups = {Add.class, Update.class}, max = 1000)
    @TableField("app_key")
    @ApiModelProperty("应用公钥")
    private String appKey;

    /**
     * 支付宝公钥
     */
    @NotEmpty
    @NotNull(message = "支付宝公钥字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "支付宝公钥字段的长度最大为1000", groups = {Add.class, Update.class}, max = 1000)
    @TableField("alipay_key")
    @ApiModelProperty("支付宝公钥")
    private String alipayKey;

    /**
     * 扩展编码
     */
    @NotEmpty
    @NotNull(message = "扩展编码字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "扩展编码字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("extends_code")
    @ApiModelProperty("扩展编码")
    private String extendsCode;

    /**
     * 应用私钥
     */
    @TableField("app_private_key")
    @ApiModelProperty("应用私钥")
    private String appPrivateKey;

}
