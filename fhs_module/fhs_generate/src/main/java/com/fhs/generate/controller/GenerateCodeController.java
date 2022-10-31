package com.fhs.generate.controller;

import com.fhs.common.utils.FileUtils;
import com.fhs.common.utils.ZipUtil;
import com.fhs.core.config.EConfig;
import com.fhs.generate.service.GenerateCodeService;
import com.fhs.generate.vo.GenerateCodeVO;
import com.fhs.generate.vo.TableInfoVO;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * 代码生成类
 *
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
    public void generate(@RequestBody @Validated GenerateCodeVO generateCodeVO, HttpServletResponse response) throws Exception {
        String sourceDir = generateCodeService.generateCode(generateCodeVO);
        String zipPath = EConfig.getPathPropertiesValue("fileSavePath") + "/vueCode";
        FileUtils.download( ZipUtil.compressedFile(sourceDir, zipPath), response, "vueCode.zip");
        FileUtils.deleteFile(zipPath);
        new File(sourceDir).delete();
    }


}
