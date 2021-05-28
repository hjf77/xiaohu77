package com.fhs.app.test;

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

    @GetMapping("getToken")
    @ApiOperation("测试获取token")
    public HttpResult<String> getToken(){
       return HttpResult.success(loginService.login("76ff71492496405d8acec56e7578b658"));
    }

    @GetMapping("/front/testtoken_u")
    @ApiOperation("测试获取token")
    public HttpResult<String> test(){
        return HttpResult.success("成功了");
    }



}
