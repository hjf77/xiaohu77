package com.fhs.basics.service;

import com.fhs.basics.po.LogOperationPO;
import com.fhs.basics.vo.LogOperationVO;
import com.fhs.core.base.service.BaseService;
import com.fhs.easycloud.anno.CloudApi;
import com.fhs.easycloud.anno.CloudMethod;

/**
 * (LogOperation)}表服务接口
 *
 * @author chenzhihu
 * @since 2022-03-10 16:43:43
 */
@CloudApi(serviceName = "basic", primary = false)
public interface LogOperationService extends BaseService<LogOperationVO, LogOperationPO>{

    @CloudMethod
    int insertSelective(LogOperationPO logOperationPO);


}
