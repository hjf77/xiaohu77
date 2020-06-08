package com.fhs.app.test;

import com.fhs.core.result.HttpResult;
import org.apache.catalina.connector.CoyoteAdapter;
import org.apache.catalina.connector.CoyoteReader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 */
@RestController
public class TestLocationController {
    @RequestMapping("/webApi/test/addLocations")
    public HttpResult<String> xx(@RequestBody String json){
        System.out.println(json);
        return HttpResult.success("ok");
    }


    @RequestMapping("/webApi/test/getReportData")
    public HttpResult<String> getReportData(HttpServletRequest request){
        String content = request.getParameter("data");
        System.out.println(content);
        return HttpResult.success("ok");
    }


}
