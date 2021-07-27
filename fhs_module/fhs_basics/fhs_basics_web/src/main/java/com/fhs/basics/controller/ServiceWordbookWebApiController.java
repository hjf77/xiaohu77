package com.fhs.basics.controller;

import com.fhs.basics.service.ServiceWordBookService;
import com.fhs.common.utils.JsonUtils;
import com.fhs.core.base.controller.BaseController;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author user
 * @date 2020-05-18 16:53:08
 */
@RestController
@ApiGroup(group = "group_default")
@RequestMapping("webApi/wordbook")
public class ServiceWordbookWebApiController extends BaseController {

    @Autowired
    private ServiceWordBookService wordBookService;

    /**
     * 查询省市区外调接口
     */
    @GetMapping("getData")
    @ApiOperation("根据字典编码获取字典")
    public void getData(String wordbookGroupCode) {
         super.outJsonp(JsonUtils.list2json(wordBookService.getWordBookList(wordbookGroupCode)));
    }
}
