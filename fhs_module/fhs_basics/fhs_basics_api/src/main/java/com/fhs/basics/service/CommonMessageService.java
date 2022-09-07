package com.fhs.basics.service;

import com.fhs.basics.po.CommonMessagePO;
import com.fhs.basics.vo.CommonMessageVO;
import com.fhs.core.base.service.BaseService;


/**
 * 消息推送表(CommonMessage)}表服务接口
 *
 * @author miyaxin
 * @since 2022-08-18 16:21:42
 */
public interface CommonMessageService extends BaseService<CommonMessageVO, CommonMessagePO> {

    void insertCommonMsg(CommonMessageVO commonMessageVO);
}
