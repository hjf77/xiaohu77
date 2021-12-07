package com.fhs.bislogger.mapper;

import com.fhs.bislogger.po.LogLoginPO;
import com.fhs.bislogger.vo.LogLoginVO;
import com.fhs.core.base.mapper.FhsBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 登录日志(LogLogin)表数据库访问层
 *
 * @author wanglei
 * @since 2020-04-23 13:58:43
 */
@Repository
public interface LogLoginMapper extends FhsBaseMapper<LogLoginPO> {

    /**
     * 汇总前20名 登录玩家的登录次数，根基时间段查询
     *
     * @param statTime
     * @param endTime
     * @return
     */
    List<LogLoginVO> getLoginIogSummary(@Param("statTime") Date statTime, @Param("endTime") Date endTime);

}
