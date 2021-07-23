package com.fhs.pub.controller;

import com.fhs.common.utils.*;
import com.fhs.core.base.controller.BaseController;
import com.fhs.core.config.EConfig;
import com.fhs.core.exception.ParamException;
import com.fhs.core.valid.checker.ParamChecker;
import com.fhs.logger.Logger;
import com.fhs.module.base.controller.ModelSuperController;
import com.fhs.pub.dox.PubFileDO;
import com.fhs.pub.service.FileStorage;
import com.fhs.pub.service.PubFileService;
import com.fhs.pub.utils.ThumbnailatorUtils;
import com.fhs.pub.vo.PubFileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qiuhang
 * @des 文件下载action
 * @since 2019-05-18 11:25:25
 */
@RestController
@RequestMapping("downLoad")
@Api(tags = "文件下载和查询")
public class DownLoadController extends BaseController {

    private static final Logger LOG = Logger.getLogger(DownLoadController.class);

    @Autowired
    private PubFileService pubFileService;

    @Autowired
    private FileStorage fileStorage;



    /**
     * 根据文件id下载文件
     *
     * @return
     */
    @RequestMapping(value = "file", method = RequestMethod.GET)
    @ApiOperation("根据id下载文件")
    public void download(String fileId, HttpServletResponse response) {
        try {
            // 文件下载路径
            PubFileVO serviceFile = pubFileService.selectById(fileId);
            fileStorage.downloadFile(serviceFile,response);
        } catch (Exception e) {
            LOG.error(this, e);
            throw new ParamException("下载文件异常,可能是文件不存在");
        }
    }


    /**
     * 根据文件名称下载文件
     *
     * @return
     */
    @RequestMapping(value = "fileByName", method = RequestMethod.GET)
    @ApiOperation("文件名称下载文件--已废弃")
    public void downloadForName(HttpServletRequest request, HttpServletResponse response) {
        try {
            String fileName = request.getParameter("fileName");
            // 文件下载路径
            String fileId = fileName.substring(0, fileName.indexOf("."));
            PubFileVO serviceFile = pubFileService.selectById(fileId);
            fileStorage.downloadFile(serviceFile,response);
        } catch (Exception e) {
            LOG.error(this, e);
            throw new RuntimeException("下载文件异常:" + e.getMessage());
        }
    }

    /**
     * 文件列表
     * @param fileIds
     * @return
     */
    @RequestMapping(value = "listData", method = RequestMethod.GET)
    @ApiOperation("根据逗号分隔的id获取文件详情")
    public void listData(String fileIds) {
        ParamChecker.isNotNullOrEmpty(fileIds,"文件id不可为空");
        List<PubFileVO> list = pubFileService.findByIds(Arrays.asList(fileIds.split(",")));
        String json = JsonUtils.list2json(list);
        this.outJsonp(json);
    }


    /**
     * 根据文件id获取文件相关信息
     *
     * @return
     */
    @RequestMapping(value = "getFileById", method = RequestMethod.GET)
    @ResponseBody
    public PubFileVO obtainFileNameById(HttpServletRequest request) {
        String fileId = request.getParameter("fileId");
        PubFileVO serviceFile = pubFileService.selectById(fileId);
        return serviceFile;
    }

    /**
     * 根据文件id下载压缩文件
     *
     * @param request
     * @param response
     */
    @GetMapping("downImgMin")
    @ApiOperation("获取指定分辨率的图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileId", paramType = "query", value = "文件id", required = true),
            @ApiImplicitParam(name = "imgFileWidth", paramType = "query", value = "图片宽度", required = false),
            @ApiImplicitParam(name = "imgFileHeight", paramType = "query", value = "图片高度", required = false),
    })
    public void downImgMin(HttpServletRequest request, HttpServletResponse response) {
        String fileId = ConverterUtils.toString(request.getParameter("fileId"));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("fileId", fileId);
        PubFileVO serviceFile = pubFileService.findBeanFromMap(map);
        if (serviceFile == null) {
            return;
        }

        int maxWidth = CheckUtils.isNullOrEmpty(request.getParameter("imgFileWidth"))
                ? ConverterUtils.toInt(EConfig.getOtherConfigPropertiesValue("imgFileWidth"))
                : ConverterUtils.toInt(request.getParameter("imgFileWidth"));
        int maxHeight = CheckUtils.isNullOrEmpty(request.getParameter("imgFileHeight"))
                ? ConverterUtils.toInt(EConfig.getOtherConfigPropertiesValue("imgFileWidth"))
                : ConverterUtils.toInt(request.getParameter("imgFileHeight"));

        // 文件后缀
        String suffix = serviceFile.getFileSuffix();
        // 文件下载路径
        String downFilePath = EConfig.getPathPropertiesValue("fileSavePath");
        // 文件上传时间
        String uploadDate = serviceFile.getUploadDate();
        // fileId
        fileId = serviceFile.getFileId();
        // 图片规格
        String fileIdWH = fileId + "_" + maxWidth + "_" + maxHeight + suffix;
        String filePath =  uploadDate + File.separator + suffix.replace(".", "") + File.separator;
        //兼容旧数据

        // 文件名
        String showFileName = serviceFile.getFileName();
        String minPath = downFilePath + filePath + fileIdWH;
        File file = new File(minPath);
        if (fileStorage.checkFileIsExist(minPath,serviceFile)) {
            fileStorage.downloadFileByToken(minPath,serviceFile,response);
        } else {
            byte[] fileByte;
            try (InputStream is = fileStorage.getFileInputStream(serviceFile)){
                fileByte = ThumbnailatorUtils
                        .zoom2Bytes(is, maxWidth, maxHeight);
                file.createNewFile();
                fileStorage.uploadFileByToken(fileByte,minPath,serviceFile);
                FileUtils.download(file, response, showFileName);
            } catch (IOException e) {
                e.printStackTrace();
                LOG.error(this, e);
            }
        }
    }



    /**
     * 根据文件id下载文件
     *
     * @return
     */
    @ApiOperation("根据文件id下载--废弃")
    @RequestMapping(value = "fileFofFileId", method = RequestMethod.GET)
    public void downloadFofFileId(HttpServletRequest request, HttpServletResponse response) {
        try {
            String fileId = request.getParameter("fileId");
            // 文件下载路径
            if (fileId.indexOf(".") > 0) {
                fileId = fileId.substring(0, fileId.indexOf("."));
            }
            PubFileVO serviceFile = pubFileService.selectById(fileId);
            fileStorage.downloadFile(serviceFile,response);
        } catch (Exception e) {
            throw new RuntimeException("下载文件异常:" + e.getMessage());
        }
    }


    /**
     * 图片转zip下载
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation("多个文件压缩zip下载--用的话请问后台")
    @RequestMapping(value = "downUploadZip", method = RequestMethod.GET)
    public void downUploadZip(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("fileIds", StringUtil.getStrToIn(request.getParameter("fileIds")));
            String fileName = request.getParameter("fileName");
            List<PubFileVO> list = pubFileService.findForListFromMap(param);
            //获取图片的路径
            String[] pngPathList = getPngPathList(list);
            //生成zip路径
            String path = EConfig.getPathPropertiesValue("fileSavePath")+fileName+".zip";
            //打包生成zip
            ZipUtil.zip(path,pngPathList);
            File file = new File(path);
            if (file.exists()){
                FileUtils.download(file, response, file.getName());
            }
            //zip传到客户端之后删除zip文件
            FileUtils.deleteFile(file.getAbsolutePath());

        } catch (Exception e) {
            throw new RuntimeException("下载文件异常:" + e.getMessage());
        }
    }

    /**
     * 获取图片路径
     * @param serviceFile
     * @return
     */
    private  String[] getPngPathList(List<PubFileVO> serviceFile) {
        String token = null;
        String[] arr = new String[serviceFile.size()];
        for (int i = 0;i<serviceFile.size();i++) {
            String fileName = (null == token ? serviceFile.get(i).getFileId() : token) + serviceFile.get(i).getFileSuffix();
            String saveFilePath = EConfig.getPathPropertiesValue("fileSavePath") + File.separator + serviceFile.get(i).getUploadDate() + File.separator + serviceFile.get(i).getFileSuffix().replace(".", "") + File.separator + fileName;
            arr[i] =  saveFilePath;
        }
        return arr;
    }

}
