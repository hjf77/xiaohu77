package com.fhs.basics.service.impl;

import com.fhs.common.utils.FileUtils;
import com.fhs.core.config.EConfig;
import com.fhs.core.db.ds.DataSource;
import com.fhs.core.exception.ParamException;
import com.fhs.core.logger.Logger;
import com.fhs.basics.po.PubFilePO;
import com.fhs.basics.service.FileStorage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 硬盘文件存储
 */

@Service
@DataSource("basic")
public class DiskFileStorage implements FileStorage {

    private static final Logger LOG = Logger.getLogger(DiskFileStorage.class);

    private static final String SEPARATOR = File.separator;

    /**
     * 获取文件对象
     *
     * @param serviceFile serviceFile
     * @return serviceFile+token定位到的文件对象
     */
    private File getFile(PubFilePO serviceFile) {
        String fileName = serviceFile.getFileId() + serviceFile.getFileSuffix();
        return new File(EConfig.getPathPropertiesValue("fileSavePath") + SEPARATOR + serviceFile.getUploadDate() + SEPARATOR + serviceFile.getFileSuffix().replace(".", "") + SEPARATOR + fileName);
    }

    @Override
    public void uploadFile(PubFilePO serviceFile, MultipartFile fileData) {
        File file = getFile(serviceFile);
        try {
            FileUtils.copyInputStreamToFile(fileData.getInputStream(), file);
            serviceFile.setFileSize(file.length());
        } catch (IOException e) {
            LOG.error("文件上传失败", e);
        }
    }

    @Override
    public void uploadFile(PubFilePO serviceFile, File fileData) {
        File file = getFile(serviceFile);
        try {
            FileUtils.copyInputStreamToFile(new FileInputStream(fileData), file);
            serviceFile.setFileSize(file.length());
        } catch (IOException e) {
            LOG.error("文件上传失败", e);
        }
    }

    @Override
    public void uploadFileByToken(byte[] bytes,  PubFilePO serviceFile) {
        File file = getFile(serviceFile);
        try (FileOutputStream os = new FileOutputStream(file)) {
            os.write(bytes);
        } catch (IOException e) {
            LOG.error("文件上传失败", e);
        }
    }

    @Override
    public void downloadFile(PubFilePO serviceFile, HttpServletResponse response) {
        File file = getFile(serviceFile);
        if (file.exists()) {
            FileUtils.download(file, response, serviceFile.getFileName());
            return;
        }
        throw new ParamException("文件不存在：" + serviceFile.getFileId());
    }

    @Override
    public boolean checkFileIsExist( PubFilePO serviceFile) {
        return getFile(serviceFile).exists();
    }

    @Override
    public InputStream getFileInputStream(PubFilePO serviceFile) throws FileNotFoundException {
        return new FileInputStream(getFile(serviceFile));
    }
}
