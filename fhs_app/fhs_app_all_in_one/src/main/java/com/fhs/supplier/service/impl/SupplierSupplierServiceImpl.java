package com.fhs.supplier.service.impl;


import com.fhs.common.utils.StringUtils;
import com.fhs.supplier.vo.SupplierSupplierVO;
import com.fhs.supplier.po.SupplierSupplierPO;
import com.fhs.supplier.service.SupplierSupplierService;
import org.springframework.stereotype.Service;
import com.fhs.core.db.ds.DataSource;
import com.fhs.core.cache.annotation.Namespace;
import com.fhs.core.base.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * 供应商(SupplierSupplier)表服务实现类
 *
 * @author liangxiaotao
 * @since 2022-05-31 14:14:48
 */
@Service
@Namespace("supplier_supplier")
public class SupplierSupplierServiceImpl extends BaseServiceImpl<SupplierSupplierVO,SupplierSupplierPO> implements SupplierSupplierService {
    @Override
    public int insert(SupplierSupplierPO entity) {
        int insert = super.insert(entity);
        Integer id = entity.getId();
        String code = StringUtils.formatCountWith0("","%06d",id);
        entity.setCode(code);
        List<SupplierSupplierPO> list = new ArrayList<>();
        list.add(entity);
        this.batchUpdate(list);
        return insert;
    }
}
