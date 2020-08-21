package com.fhs.app.seata;

import com.fhs.common.utils.StringUtil;
import com.fhs.core.exception.ParamException;
import com.fhs.core.result.HttpResult;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeateDemoController {

    @Autowired
    private DemoApi demoApi;

    @GlobalTransactional
    @RequestMapping("/seata/demo/add")
    public HttpResult<Boolean> add(){
        String uuid = StringUtil.getUUID();
        System.out.println(uuid);
        demoApi.insert(uuid);
        if(1==1){
            //throw new ParamException("xxxx");
        }
        return HttpResult.success(true);
    }
}
