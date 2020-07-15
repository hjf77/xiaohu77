package com.fhs.app.test;

import com.fhs.basics.dox.UcenterMsUserDO;
import com.fhs.core.result.HttpResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/active")
public class ActiveRecordTestController {

    @RequestMapping("/test")
    public HttpResult test(){
       UcenterMsUserDO.builder().userId("10").userName("wangelixxx222").password("1").userLoginName("wangelixxx").mobile("13455555555")
                .build().insert();

        return HttpResult.success();
    }
}
