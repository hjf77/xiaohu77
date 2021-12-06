/*
package com.fhs.app.test;

import com.fhs.core.base.po.BasePO;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.trans.service.impl.TransService;
import com.fhs.front.po.UcenterFrontUserPO;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestTransController {
    @Autowired
    private TransService transService;

    @GetMapping("testTrans")
    public TestStudent testTrans(){
        TestStudent student =new TestStudent();
        student.setUserId("76ff71492496405d8acec56e7578b658");
        transService.transOne(student);
        return student;
    }

    @Data
    public static class TestStudent extends BasePO<TestStudent> {
        @Trans(type = TransType.SIMPLE,target = UcenterFrontUserPO.class,fields = "nickName")
        private String userId ;
    }
}
*/
