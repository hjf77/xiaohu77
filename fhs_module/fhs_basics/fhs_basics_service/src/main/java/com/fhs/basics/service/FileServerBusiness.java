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
     * 上传文件
     *
     * @param fileData
     * @return
     */
    PubFileVO uploadFile(MultipartFile fileData,String fileId);


}
