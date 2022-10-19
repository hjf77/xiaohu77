package com.fhs.basics.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.base.valid.group.Delete;
import com.fhs.core.base.valid.group.Update;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 消息推送表(CommonMessage)实体类
 *
 * @author miyaxin
 * @since 2022-08-18 16:21:42
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_common_message")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CommonMessagePO", description = "CommonMessage参数")
public class CommonMessagePO extends BasePO<CommonMessagePO> {
    private static final long serialVersionUID = -85728215986519613L;
    @TableId(value = "id", type = IdType.NONE)
    @NotNull(message = "主键id字段不可为空", groups = {Update.class, Delete.class})
    @ApiModelProperty(value = "主键id")
    private Long id;

    /**
     * 消息类型
     */
    @TableField("msg_type")
    @ApiModelProperty(value = "消息类型")
    @Trans(type = TransType.DICTIONARY, key = "msgType")
    private Integer msgType;

    /**
     * 消息编号
     */
    @TableField("number")
    @ApiModelProperty(value = "消息编号")
    private String number;

    /**
     * 消息标题
     */
    @TableField("title")
    @ApiModelProperty(value = "消息标题")
    private String title;

    /**
     * 消息内容
     */
    @TableField("msg_content")
    @ApiModelProperty(value = "消息内容")
    private String msgContent;

    /**
     * 有效开始时间
     */
    @TableField("start_time")
    @ApiModelProperty(value = "有效开始时间")
    private Date startTime;

    /**
     * 有效结束时间
     */
    @TableField("end_time")
    @ApiModelProperty(value = "有效结束时间")
    private Date endTime;

    /**
     * 推送语言(字典维护)
     */
    @TableField("msg_language")
    @ApiModelProperty(value = "推送语言(字典维护)")
    private String msgLanguage;

    /**
     * 是否弹框(0否,1是)
     */
    @TableField("is_alert")
    @ApiModelProperty(value = "是否弹框(0否,1是)")
    @Trans(type = TransType.DICTIONARY, key = "yesOrNo")
    private Integer isAlert;

    /**
     * 推送范围(1所有用户 2产品类型)
     */
    @TableField("msg_range")
    @ApiModelProperty(value = "推送范围(1所有用户 2产品类型)")
    @Trans(type = TransType.DICTIONARY, key = "rangeType")
    private Integer msgRange;

    /**
     * 产品类型(1热水壶 2滑板车 3摄像头)
     */
    @TableField("msg_product")
    @ApiModelProperty(value = "产品类型(1热水壶 2滑板车 3摄像头)")
    @Trans(type = TransType.DICTIONARY, key = "productType")
    private Integer msgProduct;

    /**
     * 是否已读(0否,1是)
     */
    @TableField("is_read")
    @ApiModelProperty(value = "是否已读(0否,1是)")
    @Trans(type = TransType.DICTIONARY, key = "yesOrNo")
    private Integer isRead;


    /**
     * 用户id
     */
    @TableField("user_id")
    @ApiModelProperty(value = "用户id")
    @Trans(type = TransType.SIMPLE, target = UcenterMsUserPO.class, fields = "userName",alias="userName")
    private Long userId;


    /**
     * 通知日期
     */
    @TableField("date_time")
    @ApiModelProperty(value = "通知日期")
    private String dateTime;

    /**
     * 设备id
     */
    @TableField("device_id")
    @ApiModelProperty(value = "设备id")
    private String deviceId;

    /**
     * 家庭id
     */
    @TableField("home_id")
    @ApiModelProperty(value = "家庭id")
    private Long homeId;


   /**
     * 是否发布(0否,1是)
     */
    @TableField("is_release")
    @ApiModelProperty(value = "是否发布(0否,1是)")
    @Trans(type = TransType.DICTIONARY, key = "yesOrNo")
    private Integer isRelease;

    /**
     * 国家
     */
    @TableField("area")
    @ApiModelProperty(value = "国家")
    @Trans(type = TransType.SIMPLE, target = ServiceCountryPO.class, fields = "cname" ,alias="name")
    private String area;

    /**
     * 系统提醒
     */
    @TableField("msg_type_content")
    @ApiModelProperty(value = "系统提醒")
    private String msgTypeContent;

    /**
     * 图标
     */
    @TableField("icon")
    @ApiModelProperty(value = "图标")
    private String icon;

    /**
     * 指令
     */
    @TableField("action_url")
    @ApiModelProperty(value = "指令")
    private String actionURL;

    /**
     * 报警类型
     */
    @TableField("alarm_type")
    @ApiModelProperty(value = "报警类型")
    private String alarmType;

    /**
     * 报警类型
     */
    @TableField("attach_audios")
    @ApiModelProperty(value = "")
    private String attachAudios;

    /**
     * 报警类型
     */
    @TableField("attach_pics")
    @ApiModelProperty(value = "")
    private String attachPics;

    /**
     * 报警类型
     */
    @TableField("attach_videos")
    @ApiModelProperty(value = "")
    private String attachVideos;

    /**
     * 密钥
     */
    @TableField("encrypt_key")
    @ApiModelProperty(value = "密钥")
    private String encryptKey;

    /**
     * 家庭名称
     */
    @TableField("home_name")
    @ApiModelProperty(value = "家庭名称")
    private String homeName;

    /**
     * 家庭名称
     */
    @TableField("message_attach")
    @ApiModelProperty(value = "")
    private String messageAttach;

    /**
     * 家庭名称
     */
    @TableField("msg_srcid")
    @ApiModelProperty(value = "")
    private String msgSrcid;

    /**
     * 家庭名称
     */
    @TableField("time")
    @ApiModelProperty(value = "")
    private String time;

    /**
     * 关系id
     */
    @TableField("relation_id")
    @ApiModelProperty(value = "关系id")
    private String relationId;
}
