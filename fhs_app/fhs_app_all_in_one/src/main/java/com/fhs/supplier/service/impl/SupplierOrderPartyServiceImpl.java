package com.fhs.supplier.service.impl;

import com.fhs.basics.context.UserContext;
import com.fhs.common.utils.StringUtils;
import com.fhs.supplier.vo.SupplierOrderPartyVO;
import com.fhs.supplier.po.SupplierOrderPartyPO;
import com.fhs.supplier.service.SupplierOrderPartyService;
import org.springframework.stereotype.Service;
import com.fhs.core.cache.annotation.Namespace;
import com.fhs.core.base.service.impl.BaseServiceImpl;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单方资料(SupplierOrderParty)表服务实现类
 *
 * @author liangxiaotao
 * @since 2022-06-01 16:50:55
 */
@Service
@Namespace("supplier_order_party")
public class SupplierOrderPartyServiceImpl extends BaseServiceImpl<SupplierOrderPartyVO,SupplierOrderPartyPO> implements SupplierOrderPartyService {
    @Override
    public int insert(SupplierOrderPartyPO entity) {
        entity.setPrincipalId(UserContext.getSessionuser().getUserId());
        entity.setOrgId(Long.valueOf(UserContext.getSessionuser().getOrganizationId()));
        int insert = super.insert(entity);
        Integer id = entity.getId();
        String code = StringUtils.formatCountWith0("","%06d",id);
        entity.setOrderPartyCode(code);
        List<SupplierOrderPartyPO> list = new ArrayList<>();
        list.add(entity);
        this.batchUpdate(list);
        return insert;
    }
}
