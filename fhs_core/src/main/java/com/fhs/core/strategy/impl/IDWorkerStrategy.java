package com.fhs.core.strategy.impl;

import com.fhs.common.utils.IdHelper;
import com.fhs.common.utils.StringUtil;
import com.fhs.core.strategy.IStrategy;
import com.fhs.core.strategy.aspect.GeneratedValueAspect;
import com.fhs.core.strategy.enume.GeneratedType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * UUID生成实现
 *
 * @author jianbo.qin
 */
@Component
public class IDWorkerStrategy implements IStrategy, InitializingBean {

    private static Logger LOG = LoggerFactory.getLogger(IDWorkerStrategy.class);

    @Autowired
    private IdHelper idHelper;

    @Override
    public void afterPropertiesSet()
            throws Exception {
        GeneratedValueAspect.registerIStrategy(GeneratedType.ID_WORKER, this);
    }

    @Override
    public Object getPkey(String className) {
        return idHelper.nextId();
    }
}
