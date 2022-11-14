package com.fhs.basics.mapper;

import com.fhs.basics.po.SettUserColumnPO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * 用户自定义列信息配置(SettUserColumn)表数据库访问层
 *
 * @author wanglei
 * @since 2022-10-13 11:46:51
 */
@Repository
public interface SettUserColumnMapper extends FhsBaseMapper<SettUserColumnPO> {

    int deleteById(Long id);
}
