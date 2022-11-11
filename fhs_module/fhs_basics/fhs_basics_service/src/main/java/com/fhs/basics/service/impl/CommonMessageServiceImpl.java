package com.fhs.basics.service.impl;

import com.fhs.basics.constant.CommonMessageConstant;
import com.fhs.basics.po.CommonMessagePO;
import com.fhs.basics.service.CommonLanguageService;
import com.fhs.basics.service.CommonMessageService;
import com.fhs.basics.vo.CommonMessageVO;
import com.fhs.common.utils.DateUtils;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.cache.annotation.Namespace;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    //private static String time = DateUtils.formartDate(new Date(), "HH:mm:ss");

    @Autowired
    private CommonLanguageService commonLanguageService;

    /**
     * 保存消息通知信息
     *
     * @param commonMessageVO
     */
    @Override
    public void insertCommonMsg(CommonMessageVO commonMessageVO) {
        //获取当前环境语言
        String languageTag = commonLanguageService.getLanguageTag();
        String title = commonMessageVO.getTitle();
        commonMessageVO.setMsgType(CommonMessageConstant.FAMILY);
        if (null != title && title.equals(CommonMessageConstant.BECOME_FAMILY_MEMBER)) {
            commonMessageVO.setMsgContent(this.getBecomeFamilmemberMsg(commonMessageVO.getFamilyName(), commonMessageVO.getUserName(), languageTag));
        }
        if (null != title && title.equals(CommonMessageConstant.LEAVE_FAMILY)) {
            commonMessageVO.setMsgContent(this.getLeaveFamilyMsg(commonMessageVO.getFamilyName(), commonMessageVO.getUserName(), languageTag));
        }
        if (null != title && title.equals(CommonMessageConstant.INVITATION_FAMILY_MEMBER)) {
            commonMessageVO.setMsgContent(this.getJoinFamilyMsg(commonMessageVO.getFamilyName(), commonMessageVO.getUserName(), languageTag));
        }
        if (null != title && title.equals(CommonMessageConstant.REMOVE_FAMILY_MEMBER)) {
            commonMessageVO.setMsgContent(this.getRemoveFamilmemberMsg(commonMessageVO.getFamilyName(), commonMessageVO.getUserName(), languageTag));
        }
        if (StringUtils.isNotBlank(commonMessageVO.getPhoneModel())) {
            commonMessageVO.setMsgContent(this.getLoginMsg(commonMessageVO.getPhoneModel(), languageTag));
            commonMessageVO.setMsgType(CommonMessageConstant.NOTICE);
        }
        commonMessageVO.setIsRead(CommonMessageConstant.NO);
        commonMessageVO.setDateTime(DateUtils.formartDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        commonMessageVO.setUpdateUser(commonMessageVO.getCreateUser());
        this.insert(commonMessageVO);
    }

    private String getJoinFamilyMsg(String familyName, String userName, String languageTag) {

        if (languageTag.equals(CommonMessageConstant.languag_zh_CN)) {
            return userName + " 邀请你加入家庭" + '"' + familyName + '"' + ",在" + '"' + "我的>家庭管理" + '"' + "中接受邀请后即可加入_zh_CN";
        } else if (languageTag.equals(CommonMessageConstant.languag_ar)) {
            return userName + " 邀请你加入家庭" + '"' + familyName + '"' + ",在" + '"' + "我的>家庭管理" + '"' + "中接受邀请后即可加入_ar";
        } else {
            return userName + " 邀请你加入家庭" + '"' + familyName + '"' + ",在" + '"' + "我的>家庭管理" + '"' + "中接受邀请后即可加入_en";
        }
    }

    private String getLeaveFamilyMsg(String familyName, String userName, String languageTag) {
        if (languageTag.equals(CommonMessageConstant.languag_zh_CN)) {
            return userName + " 已经离开当前你的家庭" + '"' + familyName + '"' + ",在" + '"' + "我的>家庭管理" + '"' + "中查看现有成员信息_zh_CN";
        } else if (languageTag.equals(CommonMessageConstant.languag_ar)) {
            return userName + " 已经离开当前你的家庭" + '"' + familyName + '"' + ",在" + '"' + "我的>家庭管理" + '"' + "中查看现有成员信息_ar";
        } else {
            return userName + " 已经离开当前你的家庭" + '"' + familyName + '"' + ",在" + '"' + "我的>家庭管理" + '"' + "中查看现有成员信息_en";
        }
    }

    private String getBecomeFamilmemberMsg(String familyName, String userName, String languageTag) {
        if (languageTag.equals(CommonMessageConstant.languag_zh_CN)) {
            return userName + " 将你设为家庭" + '"' + familyName + '"' + "的管理员,可以管理房间、设备和智能了_zh_CN";
        } else if (languageTag.equals(CommonMessageConstant.languag_ar)) {
            return userName + " 将你设为家庭" + '"' + familyName + '"' + "的管理员,可以管理房间、设备和智能了_ar";
        } else {
            return userName + " 将你设为家庭" + '"' + familyName + '"' + "的管理员,可以管理房间、设备和智能了_en";
        }
    }

    private String getRemoveFamilmemberMsg(String familyName, String userName, String languageTag) {
        if (languageTag.equals(CommonMessageConstant.languag_zh_CN)) {
            return userName + " 已经将你从家庭" + '"' + familyName + '"' + "中移除,在" + '"' + "我的>家庭管理" + '"' + "中查看现有家庭信息_zh_CN";
        } else if (languageTag.equals(CommonMessageConstant.languag_ar)) {
            return userName + " 已经将你从家庭" + '"' + familyName + '"' + "中移除,在" + '"' + "我的>家庭管理" + '"' + "中查看现有家庭信息_ar";
        } else {
            return userName + " 已经将你从家庭" + '"' + familyName + '"' + "中移除,在" + '"' + "我的>家庭管理" + '"' + "中查看现有家庭信息_en";
        }
    }

    private String getLoginMsg(String phoneModel, String languageTag) {
        if (languageTag.equals(CommonMessageConstant.languag_zh_CN)) {
            return "此账号刚刚在一个新设备上登录,型号:" + phoneModel + "。若非本人操作,建议马上修改登录密码,以防账号被盗用_zh_CN";
        } else if (languageTag.equals(CommonMessageConstant.languag_ar)) {
            return "此账号刚刚在一个新设备上登录,型号:" + phoneModel + "。若非本人操作,建议马上修改登录密码,以防账号被盗用_ar";
        } else {
            return "此账号刚刚在一个新设备上登录,型号:" + phoneModel + "。若非本人操作,建议马上修改登录密码,以防账号被盗用_en";
        }
    }
}
