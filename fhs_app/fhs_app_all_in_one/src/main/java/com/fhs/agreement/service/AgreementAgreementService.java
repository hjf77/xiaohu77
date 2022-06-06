package com.fhs.agreement.service;

import com.fhs.agreement.po.AgreementAgreementPO;
import com.fhs.agreement.vo.AgreementAgreementVO;
import com.fhs.core.base.service.BaseService;

/**
 * 采购协议(AgreementAgreement)}表服务接口
 *
 * @author caiyu
 * @since 2022-06-01 10:09:42
 */
public interface AgreementAgreementService extends BaseService<AgreementAgreementVO, AgreementAgreementPO>{

    AgreementAgreementVO getAgreementGoosInfo(int id);


}
