package com.fhs.basics.api.rpc;

import com.fhs.basics.vo.ServiceWordbookVO;
import com.fhs.core.feign.config.FeignConfiguration;
import com.fhs.core.result.HttpResult;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;


/**
 * 字典公共服务
 * @author user
 * @date 2020-05-18 14:40:58
 */
@FeignClient(value = "basics", configuration = FeignConfiguration.class,primary = false)
public interface FeignWordBookApiService {

    /**
     * 根据字典编码获取字典信息
     * @param wordBookGroupCode 字典编码
     * @return HttpResult 角色数据权限
     */
    @RequestLine("GET /api/com.fhs.basics.api.rpc.FeignWordBookApiService/getWordBookListByWordBookGroupCode?wordBookGroupCode={wordBookGroupCode}")
    HttpResult<List<ServiceWordbookVO>> getWordBookListByWordBookGroupCode(@Param("wordBookGroupCode") String wordBookGroupCode);
}