package com.fhs.basics.controller;

import com.fhs.basics.service.FileServerBusiness;
import com.fhs.common.utils.CheckUtils;
import com.fhs.common.utils.DateUtils;
import com.fhs.common.utils.StringUtils;
import com.fhs.core.exception.ParamException;
import com.fhs.core.logger.Logger;
import com.fhs.basics.service.PubFileService;
import com.fhs.basics.vo.PubFileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author qiuhang
 * @des 文件上传controller
 * @since 2019-05-18 11:26:44
 */
@RestController
@RequestMapping("upload")
@Api(tags = "文件上传下载公共服务(文件服务)")
public class UploadController {

    private static final Logger LOG = Logger.getLogger(DownLoadController.class);

    @Autowired
    private FileServerBusiness fileServerBusiness;

    @Autowired
    private PubFileService fileService;

    /**
     * 文件
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "file", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    @ApiOperation("上传文件")
    public PubFileVO uploadFile(@RequestPart MultipartFile file, String ext,String fileId) {
        if (file == null) {
            throw new ParamException("文件为空");
        }
        LOG.infoMsg("开始上传文件,当前时间为{}", DateUtils.getCurrentDateStr(DateUtils.DATETIME_PATTERN));
        PubFileVO pubFileVO = fileServerBusiness.uploadFile(file,fileId);
        //如果给了扩展字段则更新扩展字段
        if (CheckUtils.isNotEmpty(ext)) {
            pubFileVO.setExt(ext);
            fileService.updateSelectiveById(pubFileVO);
        }
        LOG.infoMsg("结束上传文件,结束时间为{}", DateUtils.getCurrentDateStr(DateUtils.DATETIME_PATTERN));
        return pubFileVO;
    }

    /**
     * 删除文件
     *
     * @param fileId
     * @return
     */
    @RequestMapping(value = "deleteFile")
    @ApiOperation("删除文件")
    public void deleteFile(String fileId) {
        if (StringUtils.isEmpty(fileId)) {
            throw new ParamException("文件为空");
        }
        fileServerBusiness.deleteFile(fileId);
    }
}
