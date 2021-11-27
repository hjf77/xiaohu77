package com.fhs.pub.ueditor.upload;

import com.fhs.common.spring.SpringContextUtil;
import com.fhs.common.utils.DateUtils;
import com.fhs.common.utils.StringUtils;
import com.fhs.pub.ueditor.define.AppInfo;
import com.fhs.pub.ueditor.define.BaseState;
import com.fhs.pub.ueditor.define.FileType;
import com.fhs.pub.ueditor.define.State;
import com.fhs.pub.po.PubFilePO;

import com.fhs.pub.service.PubFileService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BinaryUploader {

    public static final State save(HttpServletRequest request, Map<String, Object> conf, String fileSavePath, String fileDownLoadPath) {
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile multipartFile = multipartRequest.getFile(conf.get("fieldName").toString());
            String originFileName = multipartFile.getOriginalFilename();
            String suffix = FileType.getSuffixByFilename(originFileName);
            long maxSize = ((Long) conf.get("maxSize")).longValue();
            if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
                return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
            }

            String currentDate = DateUtils.getCurrentDateStr("yyyy/MM/dd");
            String prefix = suffix.replace(".", "");
            String pathTemp = fileSavePath + currentDate + File.separator + prefix;// 上传文件地址
            File targetFile = new File(pathTemp);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            /************/
            // State storageState = StorageManager.saveFileByInputStream(multipartFile.getInputStream(),savePath,
            // maxSize);
            String fileId = StringUtils.getUUID();

            String filePath = pathTemp + File.separator + fileId + suffix;

            State storageState = StorageManager.saveFileByInputStream(multipartFile.getInputStream(), filePath, maxSize);
            if (storageState.isSuccess()) {
                storageState.putInfo("url",
                        fileDownLoadPath + "downLoad/fileFofFileId?fileId=" + fileId);
                storageState.putInfo("type", suffix);
                storageState.putInfo("original", originFileName);
                // 新增数据库
                PubFileService service = SpringContextUtil.getBeanByName(PubFileService.class);
                PubFilePO serviceFile = new PubFilePO();
                serviceFile.setFileName(originFileName);
                serviceFile.setFileSuffix(suffix);
                serviceFile.setFileId(fileId);
                serviceFile.setUploadDate(currentDate);
                service.insertSelective(serviceFile);

            }

            return storageState;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return new BaseState(false, AppInfo.IO_ERROR);
    }

    private static boolean validType(String type, String[] allowTypes) {
        List<String> list = Arrays.asList(allowTypes);
        return list.contains(type);
    }
}
