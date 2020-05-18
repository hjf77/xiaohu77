package com.fhs.basics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fhs.basics.dox.SettMpSettDO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import com.mybatis.jpa.annotation.MapperDefinition;
import org.springframework.stereotype.Repository;

/**
 * 公众号配置(settMpSett)表数据库访问层
 *
 * @author jackwong
 * @since 2019-03-11 14:09:24
 */
@Repository
@MapperDefinition(domainClass = SettMpSettDO.class, orderBy = " update_time DESC")
public interface SettMpSettMapper extends FhsBaseMapper<SettMpSettDO> {

}