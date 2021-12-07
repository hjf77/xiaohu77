package com.fhs.front.po;

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
import javax.validation.constraints.NotNull;

/**
 * 前端用户绑定(UcenterFrontUserBind)实体类
 *
 * @author wanglei
 * @since 2019-03-11 14:37:18
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_ucenter_front_user_bind")
@ApiModel(value = "UcenterFrontUserBindDO", description = "UcenterFrontUserBind参数")
public class UcenterFrontUserBindPO extends BasePO<UcenterFrontUserBindPO> {
    private static final long serialVersionUID = 920530665191970437L;
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键id")
    private String id;

    /**
     * 用户id
     */
    @NotEmpty
    @NotNull(message = "用户id字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "用户id字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("user_id")
    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * openId
     */
    @NotEmpty
    @NotNull(message = "openId字段不可为null", groups = {Update.class, Delete.class})
    @Length(message = "openId字段的长度最大为50", groups = {Add.class, Update.class}, max = 50)
    @TableField("auth_openId")
    @ApiModelProperty(value = "openId")
    private String authOpenid;

    /**
     * 0微信公众号 1 小程序 2 微信APP 3 qq app 4 微博app
     */
    @NotNull(message = "0微信公众号 1 小程序 2 微信APP 3 qq app 4 微博app字段不可为null", groups = {Update.class, Delete.class})
    @TableField("auth_openId_type")
    @ApiModelProperty(value = "类型")
    private Integer authOpenidType;


}
