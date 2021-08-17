package com.fhs.generate.controller;

import com.fhs.core.result.HttpResult;
import com.fhs.generate.service.GenerateCodeService;
import com.fhs.generate.vo.TableInfoVO;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 代码生成类
 * @author wanglei
 */
@RestController
@Api(tags = {"代码生成"})
@RequestMapping("ms/generateCode")
@ApiGroup(group = "group_default")
public class GenerateCodeController {

    @Autowired
    private GenerateCodeService generateCodeService;

    @PostMapping("generate")
    @ApiOperation("生成代码")
    public HttpResult<String> Generate(@RequestBody TableInfoVO tableInfoVO){
        generateCodeService.generateCode(tableInfoVO);
        return HttpResult.success("xx");
    }
}
