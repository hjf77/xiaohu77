package com.fhs.front.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.valid.group.Add;
import com.fhs.core.valid.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

/**
 * 前端用户表(UcenterFrontUser)实体类
 *
 * @author wanglei
 * @since 2019-03-25 13:58:21
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_ucenter_front_user")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UcenterFrontUserDO", description = "UcenterFrontUser参数")
public class UcenterFrontUserPO extends BasePO<UcenterFrontUserPO> {
    private static final long serialVersionUID = 545604903343287075L;

    /**
     * 用户id
     */
    @TableId(value = "user_id", type = IdType.ASSIGN_UUID)
    @ApiModelProperty("用户id")
    private String userId;

    /**
     * 用户姓名
     */
    @Length(message = "${column.comment}字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("user_name")
    @ApiModelProperty("用户姓名")
    private String userName;

    /**
     * 昵称
     */
    @Length(message = "昵称字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("nick_name")
    @ApiModelProperty("昵称")
    private String nickName;

    /**
     * 生日
     */
    @Length(message = "生日字段的长度最大为20", groups = {Add.class, Update.class}, max = 20)
    @TableField("birthday")
    @ApiModelProperty("生日")
    private String birthday;

    /**
     * 手机号
     */
    @Length(message = "手机号字段的长度最大为20", groups = {Add.class, Update.class}, max = 20)
    @TableField("mobile")
    @ApiModelProperty("手机号")
    private String mobile;

    /**
     * 性别 1-男 0-女 2-未填写
     */
    @Length(message = "性别 1-男 0-女 2-未填写字段的长度最大为11", groups = {Add.class, Update.class}, max = 11)
    @TableField("sex")
    @ApiModelProperty("性别")
    private String sex;

    /**
     * 用户密码
     */
    @Length(message = "用户密码字段的长度最大为64", groups = {Add.class, Update.class}, max = 64)
    @TableField("passwd")
    @ApiModelProperty("用户密码")
    private String passwd;

    /**
     * 身份证号
     */
    @Length(message = "身份证号字段的长度最大为32", groups = {Add.class, Update.class}, max = 32)
    @TableField("user_card")
    @ApiModelProperty("身份证号")
    private String userCard;

    /**
     * 用户来源 0 PC 1微信
     */
    @Length(message = "用户来源 0 PC 1微信字段的长度最大为11", groups = {Add.class, Update.class}, max = 11)
    @TableField("user_resource")
    @ApiModelProperty("用户来源")
    private String userResource;

    /**
     * 语种
     */
    @Length(message = "语种字段的长度最大为20", groups = {Add.class, Update.class}, max = 20)
    @TableField("language")
    @ApiModelProperty("语种")
    private String language;

    /**
     * 省
     */
    @Length(message = "省字段的长度最大为64", groups = {Add.class, Update.class}, max = 64)
    @TableField("province_id")
    @ApiModelProperty("省")
    private String provinceId;

    /**
     * 市
     */
    @Length(message = "市字段的长度最大为64", groups = {Add.class, Update.class}, max = 64)
    @TableField("city_id")
    @ApiModelProperty("市")
    private String cityId;

    /**
     * 区
     */
    @Length(message = "区字段的长度最大为64", groups = {Add.class, Update.class}, max = 64)
    @TableField("area_id")
    @ApiModelProperty("区")
    private String areaId;

    /**
     * 地址
     */
    @Length(message = "地址字段的长度最大为200", groups = {Add.class, Update.class}, max = 200)
    @TableField("address")
    @ApiModelProperty("地址")
    private String address;

    /**
     * 头像
     */
    @Length(message = "头像字段的长度最大为200", groups = {Add.class, Update.class}, max = 200)
    @TableField("image_path")
    @ApiModelProperty("头像")
    private String imagePath;


    /**
     * 启用标识 0-启用 1-禁用
     */
    @Length(message = "启用标识 0-启用 1-禁用字段的长度最大为1", groups = {Add.class, Update.class}, max = 1)
    @TableField("is_enable")
    @ApiModelProperty("是否启用")
    private Integer isEnable;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    @ApiModelProperty("真实姓名")
    private String realName;


}
