package com.fhs.basics.controller;


import com.fhs.basics.dox.ServiceWordbookDO;
import com.fhs.basics.dox.ServiceWordbookGroupDO;
import com.fhs.basics.dox.SettMsSystemDO;
import com.fhs.basics.service.SettMsSystemService;
import com.fhs.basics.vo.ServiceWordbookGroupVO;
import com.fhs.basics.vo.ServiceWordbookVO;
import com.fhs.basics.vo.SettMsSystemVO;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.module.base.controller.ModelSuperController;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@Api(tags = {"字典分组管理"})
@ApiGroup(group = "group_default")
@RequestMapping("ms/word_book_group")
public class WordBookGroupController extends ModelSuperController<ServiceWordbookGroupVO, ServiceWordbookGroupDO> {


}
