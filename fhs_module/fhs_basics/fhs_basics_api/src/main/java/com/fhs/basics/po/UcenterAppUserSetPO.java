package com.fhs.basics.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.base.valid.group.Add;
import com.fhs.core.base.valid.group.Delete;
import com.fhs.core.base.valid.group.Update;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * (UcenterAppUserSet)实体类
 *
 * @author miyaxin
 * @since 2022-08-11 09:29:44
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_ucenter_app_user_set")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value ="UcenterAppUserSetPO",description ="UcenterAppUserSet参数")
public class UcenterAppUserSetPO extends BasePO<UcenterAppUserSetPO> {

    private static final long serialVersionUID = -41937006275509525L;

    @TableId(value = "id", type = IdType.NONE)
    @NotNull(message = "主键id字段不可为空", groups = {Update.class, Delete.class})
    @ApiModelProperty(value = "主键id")
    private Long id;

    @TableField(value = "user_id")
    @NotNull(message = "用户id字段不可为空", groups = {Add.class})
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /**
     * 是否设置手势(0否 1是)
     */
    @TableField("is_gesture")
    @ApiModelProperty(value = "是否设置手势(0否 1是)")
    private Integer isGesture;
    
    /**
     * 语言
     */
    @TableField("language")
    @ApiModelProperty(value = "语言")
    private String language;
    
    /**
     * 时区
     */
    @TableField("time_zone")
    @ApiModelProperty(value = "时区")
    @Trans(type = TransType.DICTIONARY, key = "timeZoneType")
    private String timeZone;
    
    /**
     * 声音(0关闭 1开启)
     */
    @TableField("is_voice")
    @ApiModelProperty(value = "声音(0关闭 1开启)")
    private Integer isVoice;
    
    /**
     * 消息(0关闭 1开启)
     */
    @TableField("is_message")
    @ApiModelProperty(value = "消息(0关闭 1开启)")
    private Integer isMessage;
    
    /**
     * 免打扰时间区间
     */
    @TableField("disturb_date")
    @ApiModelProperty(value = "免打扰时间区间")
    private String disturbDate;
    
    /**
     * 温度类型(1摄氏 2华氏)
     */
    @TableField("temperature_type")
    @ApiModelProperty(value = "温度类型(1摄氏 2华氏)")
    private Integer temperatureType;

    /**
     * 启用告警消息推送(0否,1是)
     */
    @TableField("alarm_is_report")
    @ApiModelProperty(value = "启用告警消息推送(0否,1是)")
    private Integer alarmIsReport;

    /**
     * 启用家庭消息推送(0否,1是)
     */
    @TableField("family_is_report")
    @ApiModelProperty(value = "启用家庭消息推送(0否,1是)")
    private Integer familyIsReport;

    /**
     * 启用通知消息推送(0否,1是)
     */
    @TableField("notice_is_report")
    @ApiModelProperty(value = "启用通知消息推送(0否,1是)")
    private Integer noticeIsReport;

}
