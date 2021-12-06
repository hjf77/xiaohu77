/*
package com.fhs.app.test;

import com.fhs.common.utils.ConverterUtils;
import com.fhs.core.cache.service.RedisCacheService;
import com.fhs.core.result.HttpResult;
import com.fhs.front.service.LoginService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FrontController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private RedisCacheService<String> redisCacheService;

    @GetMapping("getToken")
    @ApiOperation("测试获取token")
    public HttpResult<String> getToken(){
       return HttpResult.success(loginService.login("76ff71492496405d8acec56e7578b658"));
    }

    @GetMapping("refreshRedisTest")
    @ApiOperation("刷新redis")
    public HttpResult<String> refreshRedisTest(){
        for(int i =0;i<5000;i++){
            redisCacheService.addStr("111",i+"");
            if(ConverterUtils.toInt(redisCacheService.getStr("111"))!=i){
                System.out.println(i + "没更新成功");
            }
        }
        return HttpResult.success("");
    }

    @GetMapping("/front/testtoken_u")
    @ApiOperation("测试获取token")
    public HttpResult<String> test(){
        return HttpResult.success("成功了");
    }



}
*/
