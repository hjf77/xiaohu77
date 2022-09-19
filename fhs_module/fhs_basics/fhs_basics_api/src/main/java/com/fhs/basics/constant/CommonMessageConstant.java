package com.fhs.basics.constant;

public interface CommonMessageConstant {


    /**
     * 是
     */
    Integer YES = 1;
    /**
     * 否
     */
    Integer NO = 0;

    /**
     * 消息类型 -- 告警
     */
    Integer REPORT_EMERGENCY = 1;

    /**
     * 消息类型 -- 家庭
     */
    Integer FAMILY = 2;
    /**
     * 消息类型 -- 通知
     */
    Integer NOTICE = 3;

    /**
     * 消息标题 -- 移除设备
     */
    String REMOVE_EQUIPMENT = "移除设备";

    /**
     * 消息标题 -- 家庭成员添加
     */
    String FAMILY_MEMBER_ADD = "家庭成员添加";

    /**
     * 消息标题 -- 邀请成为家庭成员
     */
    String INVITATION_FAMILY_MEMBER = "邀请成为家庭成员";

    /**
     * 消息标题 -- 邀请成为家庭成员
     */
    String BECOME_FAMILY_MEMBER = "已经成为家庭管理员";

    /**
     * 消息标题 -- 离开家庭
     */
    String LEAVE_FAMILY = "离开家庭";

    /**
     * 消息标题 -- 离开家庭
     */
    String REMOVE_FAMILY_MEMBER = "移除家庭成员";
}
