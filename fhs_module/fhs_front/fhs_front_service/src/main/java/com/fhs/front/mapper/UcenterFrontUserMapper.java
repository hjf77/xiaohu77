package com.fhs.front.mapper;

import com.fhs.core.base.mapper.FhsBaseMapper;
import com.fhs.front.po.UcenterFrontUserPO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 前端用户表(UcenterFrontUser)表数据库访问层
 *
 * @author wanglei
 * @since 2019-03-11 14:14:58
 */
@Repository
public interface UcenterFrontUserMapper extends FhsBaseMapper<UcenterFrontUserPO> {

    List<UcenterFrontUserPO> findListFilterMobile();

}
