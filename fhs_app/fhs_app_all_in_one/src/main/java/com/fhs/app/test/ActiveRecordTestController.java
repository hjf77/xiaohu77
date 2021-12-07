package com.fhs.app.test;

import com.fhs.basics.po.UcenterMsUserPO;
import com.fhs.core.result.HttpResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/active")
public class ActiveRecordTestController {

    @RequestMapping("/test")
    public HttpResult test(){
       /*UcenterMsUserPO.builder().userId(10L).userName("wangelixxx222").password("1").userLoginName("wangelixxx").mobile("13455555555")
                .build().insert();
*/
        return HttpResult.success();
    }
}
