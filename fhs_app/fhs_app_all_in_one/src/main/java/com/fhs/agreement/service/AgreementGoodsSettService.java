package com.fhs.agreement.service;

import com.fhs.agreement.po.AgreementGoodsSettPO;
import com.fhs.agreement.vo.AgreementGoodsSettVO;
import com.fhs.core.base.service.BaseService;

import java.util.List;

/**
 * 采购协议商品(AgreementGoodsSett)}表服务接口
 *
 * @author caiyu
 * @since 2022-06-01 11:16:29
 */
public interface AgreementGoodsSettService extends BaseService<AgreementGoodsSettVO, AgreementGoodsSettPO>{

    void physicsDelete(int agreemenetId);

    List<AgreementGoodsSettVO> goosByaAreementId(int agreemenetId);

}
