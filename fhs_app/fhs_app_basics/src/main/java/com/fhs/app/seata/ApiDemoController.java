package com.fhs.app.seata;

import com.fhs.basics.dox.SettAlipaySettDO;
import com.fhs.basics.service.SettAlipaySettService;
import com.fhs.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiDemoController {
    @Autowired
    private SettAlipaySettService settAlipaySettService;

    @RequestMapping("/seata/add")
    public void insert(String uuid){
        settAlipaySettService.add(SettAlipaySettDO.builder().appId(uuid).alipayKey("11")
                .appPrivateKey("22").appId("test").appKey("key").extendsCode("11").name("33").id(StringUtil.getUUID()).build());
    }
}
