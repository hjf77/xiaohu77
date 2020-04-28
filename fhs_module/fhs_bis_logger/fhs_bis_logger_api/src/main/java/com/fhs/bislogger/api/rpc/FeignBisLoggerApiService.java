package com.fhs.bislogger.api.rpc;

import com.fhs.basics.vo.SettMsMenuServerVO;
import com.fhs.bislogger.vo.LogAddOperatorLogVO;
import com.fhs.core.feign.config.FeignConfiguration;
import com.fhs.core.result.HttpResult;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 日志服务
 */
@FeignClient(value = "bizl", configuration = FeignConfiguration.class,primary = false)
public interface FeignBisLoggerApiService {

    /**
     * 添加操作日志
     * @param logAddOperatorLogVO 服务id
     * @return HttpResult 服务对象
     */
    @RequestLine("POST /api/com.fhs.bislogger.api.rpc.FeignBisLoggerApiService/addLog")
    void addLog(@RequestBody LogAddOperatorLogVO logAddOperatorLogVO);



}
