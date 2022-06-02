package com.fhs.agreement.service.impl;


import com.fhs.agreement.mapper.AgreementGoodsSettMapper;
import com.fhs.agreement.po.AgreementGoodsSettPO;
import com.fhs.agreement.service.AgreementGoodsSettService;
import com.fhs.agreement.vo.AgreementGoodsSettVO;
import com.fhs.agreement.vo.AgreementSelectData;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.cache.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 采购协议商品(AgreementGoodsSett)表服务实现类
 *
 * @author caiyu
 * @since 2022-06-01 11:16:31
 */
@Service
@Namespace("agreement_goods_sett")
public class AgreementGoodsSettServiceImpl extends BaseServiceImpl<AgreementGoodsSettVO, AgreementGoodsSettPO> implements AgreementGoodsSettService {

    @Autowired
    AgreementGoodsSettMapper agreementGoodsSettMapper;

    @Override
    public void physicsDelete(int agreemenetId) {
        agreementGoodsSettMapper.physicsDelete(agreemenetId);
    }

    @Override
    public List<AgreementGoodsSettVO> goosByaAreementId(int agreemenetId) {
        List<AgreementGoodsSettVO> poList = agreementGoodsSettMapper.goosByaAreementId(agreemenetId);
        List<AgreementSelectData> selectDataList = new ArrayList<>();
        AgreementSelectData selectData = new AgreementSelectData();
        List<AgreementSelectData.Goodspecification> cations = new ArrayList<>();
        AgreementSelectData.Goodspecification  cation = new AgreementSelectData.Goodspecification();
        cation.setId(0);
        cation.setTitle("1*10");
        cations.add(cation);
        cation = new AgreementSelectData.Goodspecification();
        cation.setId(1);
        cation.setTitle("1*20");
        cations.add(cation);
        selectData.setGoodspecifications(cations);
        selectDataList.add(selectData);
        for (AgreementGoodsSettVO data: poList) {
            data.setSelectDataList(selectDataList);
        }
        return poList;
    }

}
