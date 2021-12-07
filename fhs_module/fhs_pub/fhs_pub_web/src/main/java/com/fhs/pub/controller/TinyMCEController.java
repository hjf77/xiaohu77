package com.fhs.pub.controller;


import com.fhs.core.config.EConfig;
import com.fhs.core.exception.ParamException;
import com.fhs.pub.service.FileServerBusiness;
import com.fhs.pub.vo.PubFileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * um编辑器图片上传处理
 *
 * @author wanglei
 * @since 2019-05-18 11:27:25
 */
@RequestMapping("tiny")
@Controller
public class TinyMCEController {
    @Autowired
    private FileServerBusiness fileServerBusiness;

    /**
     * 上传图片
     *
     * @param file 文件
     * @return 文件对象
     */
    @ResponseBody
    @PostMapping("upload")
    public Map<String, String> imageUp(MultipartFile file) {
        if (file == null) {
            throw new ParamException("文件不能为空");
        }
        PubFileVO upFile = fileServerBusiness.uploadFileForList(Arrays.asList(file)).get(0);
        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("location", EConfig.getPathPropertiesValue("basePath") + "downLoad/file?fileId=" + upFile.getFileId());
        return returnMap;
    }
}
