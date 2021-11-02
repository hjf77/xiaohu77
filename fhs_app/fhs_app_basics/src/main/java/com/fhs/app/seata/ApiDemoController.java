package com.fhs.app.seata;

import com.fhs.basics.po.SettAlipaySettPO;
import com.fhs.basics.service.SettAlipaySettService;
import com.fhs.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiDemoController {
    @Autowired
    private SettAlipaySettService settAlipaySettService;

    @RequestMapping("/seata/add")
    public void insert(String uuid){
        settAlipaySettService.add(SettAlipaySettPO.builder().appId(uuid).alipayKey("11")
                .appPrivateKey("22").appId("test").appKey("key").extendsCode("11").name("33").id(StringUtils.getUUID()).build());
    }
}
