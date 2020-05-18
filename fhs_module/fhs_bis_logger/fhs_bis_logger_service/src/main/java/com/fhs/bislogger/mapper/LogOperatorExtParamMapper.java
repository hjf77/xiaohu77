package com.fhs.bislogger.mapper;

import com.fhs.bislogger.dox.LogOperatorExtParamDO;
import com.fhs.bislogger.vo.LogOperatorExtParamVO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import com.mybatis.jpa.annotation.MapperDefinition;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 扩展参数(LogOperatorExtParam)表数据库访问层
 *
 * @author wanglei
 * @since 2020-04-23 13:58:59
 */
@Repository
@MapperDefinition(domainClass = LogOperatorExtParamDO.class, orderBy = " update_time DESC")
public interface LogOperatorExtParamMapper extends FhsBaseMapper<LogOperatorExtParamDO> {

}