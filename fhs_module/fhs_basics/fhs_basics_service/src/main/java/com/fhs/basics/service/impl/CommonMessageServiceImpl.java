package com.fhs.basics.service.impl;

import com.fhs.basics.constant.CommonMessageConstant;
import com.fhs.basics.po.CommonMessagePO;
import com.fhs.basics.service.CommonMessageService;
import com.fhs.basics.vo.CommonMessageVO;
import com.fhs.common.utils.DateUtils;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.cache.annotation.Namespace;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 消息推送表(CommonMessage)表服务实现类
 *
 * @author miyaxin
 * @since 2022-08-18 16:21:42
 */
@Service
@Namespace("common_message")
public class CommonMessageServiceImpl extends BaseServiceImpl<CommonMessageVO, CommonMessagePO> implements CommonMessageService {

    private static String time = DateUtils.formartDate(new Date(), "HH:mm:ss");

    /**
     * 保存消息通知信息
     *
     * @param commonMessageVO
     */
    @Override
    public void insertCommonMsg(CommonMessageVO commonMessageVO) {
        String title = commonMessageVO.getTitle();
        commonMessageVO.setMsgType(CommonMessageConstant.FAMILY);
        if (null != title && title.equals(CommonMessageConstant.BECOME_FAMILY_MEMBER)) {
            commonMessageVO.setContent(this.getBecomeFamilmemberMsg(commonMessageVO.getFamilyName(), commonMessageVO.getUserName()));
        }
        if (null != title && title.equals(CommonMessageConstant.LEAVE_FAMILY)) {
            commonMessageVO.setContent(this.getLeaveFamilyMsg(commonMessageVO.getFamilyName(), commonMessageVO.getUserName()));
        }
        if (null != title && title.equals(CommonMessageConstant.INVITATION_FAMILY_MEMBER)) {
            commonMessageVO.setContent(this.getJoinFamilyMsg(commonMessageVO.getFamilyName(), commonMessageVO.getUserName()));
        }
        if (null != title && title.equals(CommonMessageConstant.REMOVE_FAMILY_MEMBER)) {
            commonMessageVO.setContent(this.getRemoveFamilmemberMsg(commonMessageVO.getFamilyName(), commonMessageVO.getUserName()));
        }
        if (StringUtils.isNotBlank(commonMessageVO.getPhoneModel())) {
            commonMessageVO.setContent(this.getLoginMsg(commonMessageVO.getPhoneModel()));
            commonMessageVO.setMsgType(CommonMessageConstant.NOTICE);
        }
        commonMessageVO.setIsRead(CommonMessageConstant.NO);
        commonMessageVO.setNoticeDate(DateUtils.formartDate(new Date(), "yyyy-MM-dd"));
        commonMessageVO.setUpdateUser(commonMessageVO.getCreateUser());
        this.insert(commonMessageVO);
    }

    private String getJoinFamilyMsg(String familyName, String userName) {
        return time + "|" + userName + " 邀请你加入家庭" + '"' + familyName + '"' + ",在" + '"' + "我的>家庭管理" + '"' + "中接受邀请后即可加入";
    }

    private String getLeaveFamilyMsg(String familyName, String userName) {
        return time + "|" + userName + " 已经离开当前你的家庭" + '"' + familyName + '"' + ",在" + '"' + "我的>家庭管理" + '"' + "中查看现有成员信息";
    }

    private String getBecomeFamilmemberMsg(String familyName, String userName) {
        return time + "|" + userName + " 将你设为家庭" + '"' + familyName + '"' + "的管理员,可以管理房间、设备和智能了";
    }

    private String getRemoveFamilmemberMsg(String familyName, String userName) {
        return time + "|" + userName + " 已经将你从家庭" + '"' + familyName + '"' + "中移除,在" + '"' + "我的>家庭管理" + '"' + "中查看现有家庭信息";
    }

    private String getLoginMsg(String phoneModel) {
        return "此账号刚刚在一个新设备上登录,型号:" + phoneModel + "。若非本人操作,建议马上修改登录密码,以防账号被盗用";
    }
}
