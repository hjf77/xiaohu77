package com.fhs.app.seata;

import com.fhs.core.feign.config.FeignConfiguration;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "basics", configuration= FeignConfiguration.class,primary = false)
public interface DemoApi {

    @RequestLine("GET /seata/add?uuid={uuid}")
    void insert(@RequestParam("uuid") String uuid);

}
