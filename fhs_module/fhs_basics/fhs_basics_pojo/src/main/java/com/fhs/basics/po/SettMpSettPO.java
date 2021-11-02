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
 * 公众号配置(settMpSett)实体类
 *
 * @author jackwong
 * @since 2019-03-11 14:24:39
 */

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_sett_mp_sett")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SettMpSettDO", description = "SettMpSett参数")
public class SettMpSettPO extends BasePO<SettMpSettPO> {
    private static final long serialVersionUID = 296286961263761719L;
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 公众号名称
     */
    @NotEmpty
    @NotNull(message = "公众号名称字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "公众号名称字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("name")
    @ApiModelProperty("公众号名称")
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
     * 密钥
     */
    @NotEmpty
    @NotNull(message = "密钥字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "密钥字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("app_secret")
    @ApiModelProperty("密钥")
    private String appSecret;

    /**
     * 管理员
     */
    @NotEmpty
    @NotNull(message = "管理员字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "管理员字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("contacts")
    @ApiModelProperty("管理员")
    private String contacts;

    /**
     * 第三方编码(做多个公众号的时候使用)
     */
    @Length(message = "第三方编码(做多个公众号的时候使用)字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("extends_code")
    @ApiModelProperty("第三方编码")
    private String extendsCode;

    /**
     * token
     */
    @Length(message = "token字段的长度最大为50", groups = {Add.class, Update.class}, max = 50)
    @TableField("token")
    @ApiModelProperty("token")
    private String token;

    /**
     * aeskey
     */
    @Length(message = "aeskey字段的长度最大为50", groups = {Add.class, Update.class}, max = 50)
    @TableField("aes_key")
    @ApiModelProperty("aeskey")
    private String aesKey;

}
