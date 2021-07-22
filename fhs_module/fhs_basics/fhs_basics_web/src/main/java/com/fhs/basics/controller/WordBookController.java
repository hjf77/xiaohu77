package com.fhs.basics.controller;


import com.fhs.basics.dox.ServiceWordbookDO;
import com.fhs.basics.vo.ServiceWordbookVO;
import com.fhs.module.base.controller.ModelSuperController;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(tags = {"字典项管理"})
@ApiGroup(group = "group_default")
@RequestMapping("ms/wordbook")
public class WordBookController extends ModelSuperController<ServiceWordbookVO, ServiceWordbookDO> {


}
