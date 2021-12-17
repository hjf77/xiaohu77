package com.fhs.demo.controller;

import com.fhs.basics.context.UserContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/ms/loginUser")
    public String getUserName(){
        return UserContext.getSessionuser().getUserName();
    }
}
