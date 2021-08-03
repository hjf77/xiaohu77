package com.fhs.pub.api.rpc;

import com.fhs.core.feign.config.FeignConfiguration;
import com.fhs.core.result.HttpResult;
import com.fhs.pub.vo.PubFileVO;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

import java.io.File;

/**
 * 文件服务工具类
 */
@FeignClient(value = "system", configuration = FeignConfiguration.class)
public interface FeignFileApiService {
    /**
     * 上传文件
     * todo  springcloud rpc调用的时候问题
     * @param filedata 文件
     * @return
     */
    @RequestLine("POST upload/file")
    HttpResult<PubFileVO> upload(@Param("Filedata") File filedata);
}
