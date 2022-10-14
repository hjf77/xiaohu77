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
    @TableField("content")
    @ApiModelProperty(value = "消息内容")
    private String content;

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
    @TableField("notice_date")
    @ApiModelProperty(value = "通知日期")
    private String noticeDate;

    /**
     * 是否发布(0否,1是)
     */
    @TableField("is_release")
    @ApiModelProperty(value = "是否发布(0否,1是)")
    @Trans(type = TransType.DICTIONARY, key = "yesOrNo")
    private Integer isRelease;
}
