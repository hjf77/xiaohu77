package com.fhs.basics.mapper;

import com.fhs.basics.po.CommonMessagePO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 消息推送表(CommonMessage)表数据库访问层
 *
 * @author miyaxin
 * @since 2022-08-18 16:21:42
 */
@Repository
public interface CommonMessageMapper extends FhsBaseMapper<CommonMessagePO> {

}
