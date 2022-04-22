package com.fhs.basics.service;

import com.fhs.basics.po.PubFilePO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 文件储存
 *
 * @author user
 * @since 2019-05-18 11:24:25
 */
public interface FileStorage {

    /**
     * 根据ServiceFile和MultipartFile做文件上传
     *
     * @param serviceFile
     * @param fileData
     */

    void uploadFile(PubFilePO serviceFile, MultipartFile fileData);

    /**
     * 根据ServiceFile和inputStream做文件上传
     * @param serviceFile
     * @param inputStream
     */
    void uploadInputStream(PubFilePO serviceFile, InputStream inputStream);


    /**
     * 根据ServiceFile和MultipartFile做文件上传
     *
     * @param serviceFile
     * @param fileData
     */

    void uploadFile(PubFilePO serviceFile, File fileData);

    /**
     * 根据文件字节数组,serviceFile和文件名做文件上传
     *
     * @param bytes
     * @param serviceFile
     */

    void uploadFileByToken(byte[] bytes,  PubFilePO serviceFile);

    /**
     * 根据serviceFile下载文件
     *
     * @param serviceFile
     * @param response
     */

    void downloadFile(PubFilePO serviceFile, HttpServletResponse response);



    /**
     * 判断一个文件是否存在
     * @param serviceFile file
     * @return true 存在 false不存在
     */
    boolean checkFileIsExist(PubFilePO serviceFile);

    /**
     * 获取一个inputstreqam
     * @param serviceFile 文件
     * @return inputstream
     */
    InputStream getFileInputStream(PubFilePO serviceFile) throws FileNotFoundException;
}
