package com.fhs.app.test;

import com.fhs.core.result.HttpResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("flow_demo")
public class FlowDemoContorller {

    @PostMapping("submit")
    public HttpResult<String> submit(HttpServletRequest request){
        System.out.println(request.getParameterMap());
        return HttpResult.success("成功");
    }
}
