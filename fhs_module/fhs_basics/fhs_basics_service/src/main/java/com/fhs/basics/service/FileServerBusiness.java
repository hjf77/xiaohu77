package com.fhs.basics.service;

import com.fhs.basics.vo.PubFileVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * 文件服务器业务
 *
 * @author user
 * @since 2019-05-18 11:22:55
 */
public interface FileServerBusiness {
    /**
     * filep
     */
    String fileP = File.separator;


    /**
     * 上传文件
     *
     * @param fileData
     * @return
     */
    PubFileVO uploadFile(MultipartFile fileData);

    /**
     * 上传多个文件
     *
     * @param allFileData
     * @return
     */
    List<PubFileVO> uploadFileForList(List<MultipartFile> allFileData);
}
