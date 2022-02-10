package com.fhs.basics.service;

import com.fhs.basics.vo.PubFileVO;
import com.fhs.easycloud.anno.CloudApi;
import com.fhs.easycloud.anno.CloudMethod;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务器业务
 *
 * @author user
 * @since 2019-05-18 11:22:55
 */
@CloudApi(serviceName = "basic", primary = false)
public interface FileServerBusiness {
    
    /**
     * 上传文件
     *
     * @param fileData
     * @return
     */
    @CloudMethod
    PubFileVO uploadFile(MultipartFile fileData,String fileId);


}
