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
@DataSource("file")
public class DiskFileStorage implements FileStorage {

    private static final Logger LOG = Logger.getLogger(DiskFileStorage.class);

    private static final String SEPARATOR = File.separator;

    /**
     * 获取文件对象
     *
     * @param serviceFile serviceFile
     * @param token       token(可为null)
     * @return serviceFile+token定位到的文件对象
     */
    private File getFile(PubFilePO serviceFile, String token) {
        String fileName = (null == token ? serviceFile.getFileId() : token) + serviceFile.getFileSuffix();
        return new File(EConfig.getPathPropertiesValue("fileSavePath") + SEPARATOR + serviceFile.getUploadDate() + SEPARATOR + serviceFile.getFileSuffix().replace(".", "") + SEPARATOR + fileName);
    }

    @Override
    public void uploadFile(PubFilePO serviceFile, MultipartFile fileData) {
        File file = getFile(serviceFile, null);
        try {
            FileUtils.copyInputStreamToFile(fileData.getInputStream(), file);
            serviceFile.setFileSize(file.length());
        } catch (IOException e) {
            LOG.error("文件上传失败", e);
        }
    }

    @Override
    public void uploadFile(PubFilePO serviceFile, File fileData) {
        File file = getFile(serviceFile, null);
        try {
            FileUtils.copyInputStreamToFile(new FileInputStream(fileData), file);
            serviceFile.setFileSize(file.length());
        } catch (IOException e) {
            LOG.error("文件上传失败", e);
        }
    }

    @Override
    public void uploadFileByToken(byte[] bytes, String token, PubFilePO serviceFile) {
        File file = getFile(serviceFile, token);
        try (FileOutputStream os = new FileOutputStream(file)) {
            os.write(bytes);
        } catch (IOException e) {
            LOG.error("文件上传失败", e);
        }
    }

    @Override
    public void downloadFile(PubFilePO serviceFile, HttpServletResponse response) {
        File file = getFile(serviceFile, null);
        if (file.exists()) {
            FileUtils.download(file, response, serviceFile.getFileName());
            return;
        }
        throw new ParamException("文件不存在：" + serviceFile.getFileId());
    }

    @Override
    public void downloadFileByToken(String token, PubFilePO serviceFile, HttpServletResponse response) {
        File file = getFile(serviceFile, token);
        if (file.exists()) {
            FileUtils.download(file, response, file.getName());
        }
    }

    @Override
    public boolean checkFileIsExist(String token, PubFilePO serviceFile) {
        return getFile(serviceFile, token).exists();
    }

    @Override
    public InputStream getFileInputStream(PubFilePO serviceFile) throws FileNotFoundException {
        return new FileInputStream(getFile(serviceFile, null));
    }
}
