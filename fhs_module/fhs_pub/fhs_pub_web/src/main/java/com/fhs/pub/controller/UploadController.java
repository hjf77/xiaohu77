package com.fhs.pub.controller;

import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.DateUtils;
import com.fhs.common.utils.JsonUtil;
import com.fhs.core.base.controller.BaseController;
import com.fhs.logger.Logger;
import com.fhs.pub.service.FileServerBusiness;
import com.fhs.pub.service.PubFileService;
import com.fhs.pub.vo.PubFileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @author qiuhang
 * @des 文件上传controller
 * @since 2019-05-18 11:26:44
 */
@RestController
@RequestMapping("upload")
@Api(tags = "文件上传下载公共服务(文件服务)")
public class UploadController extends BaseController {

    private static final Logger LOG = Logger.getLogger(DownLoadController.class);

    @Autowired
    private FileServerBusiness fileServerBusiness;

    @Autowired
    private PubFileService fileService;

    /**
     * 文件
     *
     * @param Filedata
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "file", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    @ApiOperation("上传文件")
    public void uploadFile(@RequestPart MultipartFile Filedata, String ext, HttpServletRequest request,
                           HttpServletResponse response) {
        if (Filedata == null) {
            super.outToClient(false);
            return;
        }
        LOG.infoMsg("开始上传文件,当前时间为{}", DateUtils.getCurrentDateStr(DateUtils.DATETIME_PATTERN));
        PubFileVO file = fileServerBusiness.uploadFileForList(Arrays.asList(Filedata)).get(0);
        //如果给了扩展字段则更新扩展字段
        if (CheckUtils.isNotEmpty(ext)) {
            file.setExt(ext);
            fileService.updateSelectiveById(file);
        }
        LOG.infoMsg("结束上传文件,结束时间为{}", DateUtils.getCurrentDateStr(DateUtils.DATETIME_PATTERN));
        super.outWriteJson(JsonUtil.bean2json(file));

    }


}
