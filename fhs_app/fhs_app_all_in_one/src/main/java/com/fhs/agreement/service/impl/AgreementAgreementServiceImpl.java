package com.fhs.agreement.service.impl;


import com.fhs.agreement.po.AgreementAgreementPO;
import com.fhs.agreement.po.AgreementGoodsSettPO;
import com.fhs.agreement.service.AgreementAgreementService;
import com.fhs.agreement.service.AgreementGoodsSettService;
import com.fhs.agreement.vo.AgreementAgreementVO;
import com.fhs.agreement.vo.AgreementGoodsSettVO;
import com.fhs.basics.context.UserContext;
import com.fhs.common.utils.StringUtils;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.cache.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 采购协议(AgreementAgreement)表服务实现类
 *
 * @author caiyu
 * @since 2022-06-01 10:09:43
 */
@Service
@Namespace("agreement_agreement")
public class AgreementAgreementServiceImpl extends BaseServiceImpl<AgreementAgreementVO, AgreementAgreementPO> implements AgreementAgreementService {

    @Autowired
    AgreementGoodsSettService agreementGoodsSettService;

    @Override
    public int insert(AgreementAgreementPO entity) {
        entity.setStatus(0);
        int insert = super.insert(entity);
        Integer id = entity.getId();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        String no = fmt.format(new Date()) + StringUtils.formatCountWith0("","%06d",id);
        entity.setNo(no);
        List<AgreementAgreementPO> list = new ArrayList<>();
        list.add(entity);
        this.batchUpdate(list);
        saveGoods(entity.getGoodsVOs(), entity.getId(),false);
        return insert;
    }

    @Override
    public int updateById(AgreementAgreementPO entity) {
        int insert = super.updateById(entity);
        saveGoods(entity.getGoodsVOs(), entity.getId(),false);
        return insert;
    }

    @Override
    public int deleteById(Serializable primaryValue) {
        AgreementGoodsSettPO info = new AgreementGoodsSettPO();
        info.setAgreementId(Integer.parseInt(primaryValue.toString()));
        agreementGoodsSettService.deleteBean(info);
        return super.deleteById(primaryValue);
    }

    public void saveGoods(AgreementGoodsSettVO[] goodsVOs, int agreemenetId, boolean isAdd){
        if(goodsVOs == null || UserContext.getSessionuser() == null){
            return;
        }
        if(!isAdd){
            agreementGoodsSettService.physicsDelete(agreemenetId);
            for (AgreementGoodsSettVO data: goodsVOs) {
                data.preUpdate(UserContext.getSessionuser().getUserId());
                data.setAgreementId(agreemenetId);
            }

        }else{
            for (AgreementGoodsSettVO data: goodsVOs) {
                data.preInsert(UserContext.getSessionuser().getUserId());
                data.setAgreementId(agreemenetId);
            }
        }
        agreementGoodsSettService.batchInsert(Arrays.asList(goodsVOs));
    }


    @Override
    public AgreementAgreementVO getAgreementGoosInfo(int id) {
        AgreementAgreementVO data = super.selectById(id);
        List<AgreementGoodsSettVO> goosList = agreementGoodsSettService.goosByaAreementId(id);

        AgreementGoodsSettVO[] goosArr = new AgreementGoodsSettVO[goosList.size()];
        goosArr = goosList.toArray(goosArr);
        data.setGoodsVOs(goosArr);
        return data;
    }

    @Override
    public boolean agreementAudit(int id) {
        AgreementAgreementPO info = super.selectById(id);
        info.setStatus(1);
        info.preUpdate(UserContext.getSessionuser().getUserId());
        super.updateById(info);
        return true;
    }
}
