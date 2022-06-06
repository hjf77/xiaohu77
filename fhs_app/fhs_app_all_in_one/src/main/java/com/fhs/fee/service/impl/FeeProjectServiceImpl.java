package com.fhs.fee.service.impl;


import com.fhs.core.base.service.impl.BaseServiceImpl;
import com.fhs.core.cache.annotation.Namespace;
import com.fhs.core.valid.checker.ParamChecker;
import com.fhs.fee.po.FeeProjectPO;
import com.fhs.fee.service.FeeProjectService;
import com.fhs.fee.vo.FeeProjectVO;
import org.springframework.stereotype.Service;

/**
 * 费用项目(FeeProject)表服务实现类
 *
 * @author yutao
 * @since 2022-05-31 14:50:21
 */
@Service
@Namespace("fee_project")
public class FeeProjectServiceImpl extends BaseServiceImpl<FeeProjectVO, FeeProjectPO> implements FeeProjectService {
    @Override
    public int insert(FeeProjectPO entity) {
        ParamChecker.isNotNullOrEmpty(entity.getParentId(), "父类id不能为空");
        FeeProjectVO feeProjectVO = this.selectById(entity.getParentId());
        ParamChecker.isNotNullOrEmpty(feeProjectVO, "无效的父类id");
        entity.setLevel(feeProjectVO.getLevel() + 1);
        return super.insert(entity);
    }
}
